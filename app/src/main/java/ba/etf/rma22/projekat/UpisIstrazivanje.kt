package ba.etf.rma22.projekat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ba.etf.rma22.projekat.data.models.Grupa
import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.models.Korisnik
import ba.etf.rma22.projekat.data.repositories.GrupaRepository
import ba.etf.rma22.projekat.data.repositories.IstrazivanjeRepository


class UpisIstrazivanje : AppCompatActivity() {
    private lateinit var dodajIstrazivanjeDugme:Button
    private lateinit var spinnerGodine: Spinner
    private lateinit var spinnerIstrazivanja: Spinner
    private lateinit var spinnerGrupe: Spinner
    private var godine = mutableListOf("1","2","3","4","5")
    private lateinit var korisnik: Korisnik

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upis_istrazivanje)

         korisnik=getIntent().getSerializableExtra("poruka")as Korisnik

        //spinner za godine
        spinnerGodine=findViewById(R.id.odabirGodina)
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, godine)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGodine.setAdapter(arrayAdapter)
        spinnerGodine.setSelection(korisnik.getPosljednjaGodina()-1)

        //spinner za istrazivanja
        spinnerIstrazivanja=findViewById(R.id.odabirIstrazivanja)
        val arrayAdapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("Odaberite istraživanje:"))
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerIstrazivanja.setAdapter(arrayAdapter1)

        //spiner za grupe
        spinnerGrupe=findViewById(R.id.odabirGrupa)
        val arrayAdapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item,listOf("Odaberite grupu:"))
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGrupe.setAdapter(arrayAdapter2)

        //klik na upisi me
        dodajIstrazivanjeDugme=findViewById(R.id.dodajIstrazivanjeDugme)
        dodajIstrazivanjeDugme.setOnClickListener {
                var name = spinnerIstrazivanja.selectedItem.toString()
                var year = spinnerGodine.selectedItem.toString()
                var group = spinnerGrupe.selectedItem.toString()
                var Istrazivanje1 = getIstrazivanjeByNameAndYear(name, year)
                var grupa1 = getGroupByNameAndIstrazivanje(group, name)

                if (Istrazivanje1 != null) {
                    korisnik.addIstrazivanja(Istrazivanje1)
                    korisnik.setPosljednjeOdabranoIstrazivanje(Istrazivanje1)
                    korisnik.setposljednjaGodina(spinnerGodine.selectedItem.toString().toInt())
                }
                if (grupa1 != null) {
                    korisnik.addGrupu(grupa1)
                    korisnik.setPosljednjeOdabranaGrupa(grupa1)
                }

                val intent = Intent()
                intent.putExtra("rezultat", korisnik)
                setResult(RESULT_OK, intent)
                finish()
        }
        //selektovanje godine mijenja istrazivanja
        spinnerGodine.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var odabranaGodina=parent!!.getItemAtPosition(position).toString();
                updateSpinnerIstrazivanje(odabranaGodina)
            }
        }
        //selektovanje istrazivanja mijenja grupu
        spinnerIstrazivanja.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var odabrano=parent!!.getItemAtPosition(position).toString()
                updateSpinnerGrupe(odabrano)
            }
        }
        spinnerGrupe.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var odabrano=parent!!.getItemAtPosition(position).toString()
                if(odabrano=="Odaberite grupu:")  dodajIstrazivanjeDugme.isEnabled = false
                else   dodajIstrazivanjeDugme.isEnabled = true
            }
        }
    }
    fun updateSpinnerIstrazivanje(odabranaGodina:String){
        var istrazivanja=korisnik.getNeupisanaIstrazivanja1().toMutableList()
        istrazivanja.removeAll { i->i.godina!=odabranaGodina.toInt()}
        var novo=istrazivanja.map { p->p.naziv }.toMutableList()
        novo.add(0,"Odaberite istraživanje:")
        if(novo.size==0){
            spinnerIstrazivanja.setAdapter(null)
            spinnerGrupe.setAdapter(null)
        }
        else {
            val arrayAdapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, novo)
            arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerIstrazivanja.setAdapter(arrayAdapter1)
        }
    }
    fun updateSpinnerGrupe(odabranoIstr:String){
        var tmp1:MutableList<String>
        if(spinnerIstrazivanja.selectedItem.toString()=="Odaberite istraživanje:") {
            dodajIstrazivanjeDugme.isEnabled = false
            tmp1= mutableListOf("Odaberite grupu:")
        }else {
            dodajIstrazivanjeDugme.isEnabled = true
            var tmp = korisnik.getGrupePoNeupisanimIstrazivanjima1().toMutableList()
            tmp.removeAll { g -> korisnik.getupisaneGrupe().contains(g) }
            tmp.removeAll { g -> g.nazivIstrazivanja != odabranoIstr }
            tmp1 = tmp.map { g -> g.naziv }.toMutableList()
            tmp1.add(0, "Odaberite grupu:")
        }
            spinnerGrupe = findViewById(R.id.odabirGrupa)
            val arrayAdapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, tmp1)
            arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGrupe.setAdapter(arrayAdapter2)

    }
    fun getIstrazivanjeByNameAndYear(name:String,year:String): Istrazivanje? {
        return IstrazivanjeRepository.getIstrazivanjeByGodina(Integer.parseInt(year)).find { i->i.naziv==name  }
    }
    fun getGroupByNameAndIstrazivanje(name:String, istrazivanje:String):Grupa?{
        return GrupaRepository.getGroupsByIstrazivanje(istrazivanje).find { g->g.naziv==name }
    }
}