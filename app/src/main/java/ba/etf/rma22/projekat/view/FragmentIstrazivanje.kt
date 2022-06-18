package ba.etf.rma22.projekat.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.models.*
import ba.etf.rma22.projekat.data.repositories.IstrazivanjeIGrupaRepository
import ba.etf.rma22.projekat.viewmodel.IstrazivanjeIGrupaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class FragmentIstrazivanje(): Fragment() {
    private lateinit var dodajIstrazivanjeDugme: Button
    private lateinit var spinnerGodine: Spinner
    private lateinit var spinnerIstrazivanja: Spinner
    private lateinit var spinnerGrupe: Spinner
    private var godine = mutableListOf("1","2","3","4","5")
    private var grupe:MutableList<Grupa> = SveAnkete.sveGrupe
    private var upisaneGrupe:MutableList<Grupa> = mutableListOf()
    private var istrazivanja:MutableList<Istrazivanje> =mutableListOf()
    private lateinit var sm: PomocniInterfejs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_istrazivanje, container, false)

        runBlocking {
            SveAnkete.sveGrupe= IstrazivanjeIGrupaRepository.getGrupe()?.toMutableList() ?: mutableListOf()
            SveAnkete.svaIstrazivanja=IstrazivanjeIGrupaRepository.getIstrazivanja()?.toMutableList() ?: mutableListOf()
        }
        istrazivanja=SveAnkete.svaIstrazivanja
        grupe=SveAnkete.sveGrupe

        //spinner za godine
        spinnerGodine=view.findViewById(R.id.odabirGodina)
        val arrayAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, godine)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGodine.adapter=arrayAdapter
        spinnerGodine.setSelection(SveAnkete.posljednjaOdabranaGodina-1)

        //spinner za istrazivanja
        spinnerIstrazivanja=view.findViewById(R.id.odabirIstrazivanja)
        val arrayAdapter1 = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, listOf("Odaberite istraživanje:"))
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerIstrazivanja.adapter=arrayAdapter1

        //spiner za grupe
        spinnerGrupe=view.findViewById(R.id.odabirGrupa)
        val arrayAdapter2 = ArrayAdapter(view.context, android.R.layout.simple_spinner_item,listOf("Odaberite grupu:"))
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGrupe.adapter=arrayAdapter2

        //klik na upisi me
        dodajIstrazivanjeDugme=view.findViewById(R.id.dodajIstrazivanjeDugme)
        if(!InternetConnection.prisutna)dodajIstrazivanjeDugme.isEnabled=false
        dodajIstrazivanjeDugme.setOnClickListener {
            val name = spinnerIstrazivanja.selectedItem.toString()
            val year = spinnerGodine.selectedItem.toString()
            val group = spinnerGrupe.selectedItem.toString()
            val istrazivanje = getIstrazivanjeByNameAndYear(name, year)
            val grupa1 = getIdIstrazivanjaByName(name)?.let { it1 -> getGroupByNameAndIstrazivanje(group, it1) }

            if (istrazivanje != null) {
            }
            if (grupa1 != null) {
                val job= GlobalScope.launch (Dispatchers.IO){
                    IstrazivanjeIGrupaViewModel().upisiUGrupu(grupa1.id)
                    //za sada ne znamo sta se desava ako upis ne uspije
                }
                runBlocking { job.join() }
            }
            sm = activity as PomocniInterfejs
            sm.passDataAndGoToPoruka("Uspješno ste upisani u grupu "+group+" istraživanja "+istrazivanje?.naziv+"!")
        }
        //selektovanje godine mijenja istrazivanja
        spinnerGodine.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val odabranaGodina=parent!!.getItemAtPosition(position).toString()
                SveAnkete.posljednjaOdabranaGodina=odabranaGodina.toInt()
                updateSpinnerIstrazivanje(odabranaGodina)
            }
        }
        //selektovanje istrazivanja mijenja grupu
        spinnerIstrazivanja.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val odabrano=parent!!.getItemAtPosition(position).toString()
                IstrazivanjeIGrupaViewModel().getUpisaneGrupe(onSuccess = ::onSuccess, onError = ::onError)
                updateSpinnerGrupe(odabrano)
            }
        }
        spinnerGrupe.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val odabrano=parent!!.getItemAtPosition(position).toString()
                if(odabrano=="Odaberite grupu:")  dodajIstrazivanjeDugme.isEnabled = false
                else if(!InternetConnection.prisutna)dodajIstrazivanjeDugme.isEnabled=false
                else dodajIstrazivanjeDugme.isEnabled = true
            }
        }
        return view
    }
    fun updateSpinnerIstrazivanje(odabranaGodina:String){
        var i2:MutableList<Istrazivanje> = mutableListOf()
        i2.addAll(this.istrazivanja)
        i2.removeAll { z->z.godina!=odabranaGodina.toInt() }
        for(z in i2){
         if(SveAnkete.upisanaIstrazivanja.find { q->q==z.naziv }!=null)i2.remove(z)
        }
        var istrazivanje1= i2.map { i->i.naziv }.toMutableList()
        istrazivanje1.add(0,"Odaberite istraživanje:")
        if(this.istrazivanja.size==0){
            spinnerIstrazivanja.adapter=null
            spinnerGrupe.adapter=null
            Log.d("ISTRAZIVANJA U SPINNERU11:",istrazivanje1.size.toString())
        }
        else {
            Log.d("ISTRAZIVANJA U SPINNERU:",istrazivanje1.size.toString())
            val arrayAdapter1 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, istrazivanje1)
            arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerIstrazivanja.adapter=arrayAdapter1
        }
    }
    fun updateSpinnerGrupe(odabranoIstr:String){
        var grupe1:MutableList<String> = mutableListOf("Odaberite grupu:")
        if(spinnerIstrazivanja.selectedItem.toString()=="Odaberite istraživanje:") {
            dodajIstrazivanjeDugme.isEnabled = false
            if(!InternetConnection.prisutna)dodajIstrazivanjeDugme.isEnabled=false
        } else{
            dodajIstrazivanjeDugme.isEnabled = true
            var g: MutableList<Grupa> = mutableListOf()
            g.addAll(this.grupe)
            g.removeAll { g1 -> g1.idIstrazivanja != getIdIstrazivanjaByName(odabranoIstr) }
            g.removeAll(upisaneGrupe) //moram dobiti sve grupe u koje nije upisan

            grupe1 = g.map { gg -> gg.naziv }.toMutableList()
            grupe1.add(0,"Odaberite grupu:")
        }
        spinnerGrupe = requireView().findViewById(R.id.odabirGrupa)
        Log.d("GRUPE U SPINNERU:",grupe1.size.toString())
        val arrayAdapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, grupe1)
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGrupe.adapter=arrayAdapter2
    }
    private fun getIdIstrazivanjaByName(name:String):Int?{
        return istrazivanja.find { i->i.naziv==name }?.id
    }
    private fun getIstrazivanjeByNameAndYear(name:String,year:String): Istrazivanje? {
        return this.istrazivanja.find { i->i.godina==year.toInt() && i.naziv==name }
    }
    private fun getGroupByNameAndIstrazivanje(name:String, idIstrazivanja:Int): Grupa?{
        return this.grupe.find { g->g.naziv==name &&  g.idIstrazivanja==idIstrazivanja}
    }
    fun onSuccess(grupe:List<Grupa>){
        this.upisaneGrupe=grupe.toMutableList()
    }
    fun returnGrupe(grupe:List<Grupa>):List<Grupa>{
        return grupe
    }
    fun onError() {
        val toast = Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
        toast.show()
    }
}