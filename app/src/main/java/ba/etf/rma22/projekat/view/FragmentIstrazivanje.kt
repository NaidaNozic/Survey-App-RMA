package ba.etf.rma22.projekat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import ba.etf.rma22.projekat.PomocniInterfejs
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.models.Grupa
import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.models.Korisnik
import ba.etf.rma22.projekat.data.repositories.GrupaRepository
import ba.etf.rma22.projekat.data.repositories.IstrazivanjeRepository

class FragmentIstrazivanje: Fragment() {
    private lateinit var dodajIstrazivanjeDugme: Button
    private lateinit var spinnerGodine: Spinner
    private lateinit var spinnerIstrazivanja: Spinner
    private lateinit var spinnerGrupe: Spinner
    private var godine = mutableListOf("1","2","3","4","5")
    private var korisnik = Korisnik()
    private lateinit var sm: PomocniInterfejs


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_istrazivanje, container, false)


        //spinner za godine
        spinnerGodine=view.findViewById(R.id.odabirGodina)
        val arrayAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, godine)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGodine.adapter=arrayAdapter
        spinnerGodine.setSelection(korisnik.getPosljednjaGodina()-1)

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
        dodajIstrazivanjeDugme.setOnClickListener {
            val name = spinnerIstrazivanja.selectedItem.toString()
            val year = spinnerGodine.selectedItem.toString()
            val group = spinnerGrupe.selectedItem.toString()
            val istrazivanje = getIstrazivanjeByNameAndYear(name, year)
            val grupa1 = getGroupByNameAndIstrazivanje(group, name)


            if (istrazivanje != null) {
                korisnik.addIstrazivanja(istrazivanje)
                korisnik.setPosljednjeOdabranoIstrazivanje(istrazivanje)
                korisnik.setposljednjaGodina(spinnerGodine.selectedItem.toString().toInt())
            }
            if (grupa1 != null) {
                korisnik.addGrupu(grupa1)
                korisnik.setPosljednjeOdabranaGrupa(grupa1)
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
                updateSpinnerIstrazivanje(odabranaGodina)
            }
        }
        //selektovanje istrazivanja mijenja grupu
        spinnerIstrazivanja.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val odabrano=parent!!.getItemAtPosition(position).toString()
                updateSpinnerGrupe(odabrano)
            }
        }
        spinnerGrupe.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val odabrano=parent!!.getItemAtPosition(position).toString()
                if(odabrano=="Odaberite grupu:")  dodajIstrazivanjeDugme.isEnabled = false
                else   dodajIstrazivanjeDugme.isEnabled = true
            }
        }
        return view
    }
    fun updateSpinnerIstrazivanje(odabranaGodina:String){
        val istrazivanja=korisnik.getNeupisanaIstrazivanja1().toMutableList()
        istrazivanja.removeAll { i->i.godina!=odabranaGodina.toInt()}
        val novo=istrazivanja.map { p->p.naziv }.toMutableList()
        novo.add(0,"Odaberite istraživanje:")
        if(novo.size==0){
            spinnerIstrazivanja.adapter=null
            spinnerGrupe.adapter=null
        }
        else {
            val arrayAdapter1 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, novo)
            arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerIstrazivanja.adapter=arrayAdapter1
        }
    }
    fun updateSpinnerGrupe(odabranoIstr:String){
        val tmp1:MutableList<String>
        if(spinnerIstrazivanja.selectedItem.toString()=="Odaberite istraživanje:") {
            dodajIstrazivanjeDugme.isEnabled = false
            tmp1= mutableListOf("Odaberite grupu:")
        }else {
            dodajIstrazivanjeDugme.isEnabled = true
            val tmp = korisnik.getGrupePoNeupisanimIstrazivanjima1().toMutableList()
            tmp.removeAll { g -> korisnik.getupisaneGrupe().contains(g) }
            tmp.removeAll { g -> g.nazivIstrazivanja != odabranoIstr }
            tmp1 = tmp.map { g -> g.naziv }.toMutableList()
            tmp1.add(0, "Odaberite grupu:")
        }
        spinnerGrupe = requireView().findViewById(R.id.odabirGrupa)
        val arrayAdapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tmp1)
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGrupe.adapter=arrayAdapter2

    }
    private fun getIstrazivanjeByNameAndYear(name:String,year:String): Istrazivanje? {
        return IstrazivanjeRepository.getIstrazivanjeByGodina(Integer.parseInt(year)).find { i->i.naziv==name  }
    }
    private fun getGroupByNameAndIstrazivanje(name:String, istrazivanje:String): Grupa?{
        return GrupaRepository.getGroupsByIstrazivanje(istrazivanje).find { g->g.naziv==name }
    }
    companion object {
        fun newInstance(): FragmentIstrazivanje = FragmentIstrazivanje()
    }
}