package ba.etf.rma22.projekat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import ba.etf.rma22.projekat.data.models.Grupa
import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.models.Korisnik
import ba.etf.rma22.projekat.data.repositories.GrupaRepository
import ba.etf.rma22.projekat.data.repositories.IstrazivanjeRepository
import ba.etf.rma22.projekat.viewmodel.KorisnikListViewModel


class UpisIstrazivanje : AppCompatActivity() {
    private lateinit var dodajIstrazivanjeDugme:Button
    private lateinit var spinnerGodine: Spinner
    private lateinit var spinnerIstrazivanja: Spinner
    private lateinit var spinnerGrupe: Spinner
    private var godine = mutableListOf(1,2,3,4,5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upis_istrazivanje)

        var korisnik=getIntent().getSerializableExtra("poruka")as? Korisnik

        //spinner za godine
        spinnerGodine=findViewById(R.id.odabirGodina)
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, godine)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGodine!!.setAdapter(arrayAdapter)
        spinnerGodine.setSelection(korisnik!!.getPosljednjaGodina()-1)

        //spinner za istrazivanja
        spinnerIstrazivanja=findViewById(R.id.odabirIstrazivanja)
        val arrayAdapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, korisnik!!.getNeupisanaIstrazivanja())
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerIstrazivanja!!.setAdapter(arrayAdapter1)

        //spiner za grupe
        var tmp=korisnik!!.getGrupePoNeupisanimIstrazivanjima().toMutableList()
        tmp.removeAll { g-> korisnik!!.getupisaneGrupe().map { g1->g1.naziv }.toMutableList().contains(g)}
        spinnerGrupe=findViewById(R.id.odabirGrupa)
        val arrayAdapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item,tmp)
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGrupe!!.setAdapter(arrayAdapter2)

        //klik na upisi me
        dodajIstrazivanjeDugme=findViewById(R.id.dodajIstrazivanjeDugme)
        dodajIstrazivanjeDugme.setOnClickListener{
            var name=spinnerIstrazivanja.selectedItem.toString()
            var year=spinnerGodine.selectedItem.toString()
            var group=spinnerGrupe.selectedItem.toString()
            var Istrazivanje1=getIstrazivanjeByNameAndYear(name,year)
            var grupa1=getGroupByNameAndIstrazivanje(group,name)

            if(Istrazivanje1!=null) {
                korisnik.addIstrazivanja(Istrazivanje1)
                korisnik.setPosljednjeOdabranoIstrazivanje(Istrazivanje1)
                korisnik.setposljednjaGodina(spinnerGodine.selectedItem.toString().toInt())
            }
            if(grupa1!=null) {
                korisnik.addGrupu(grupa1)
                korisnik.setPosljednjeOdabranaGrupa(grupa1)
            }

            val intent=Intent()
            intent.putExtra("rezultat",korisnik)
            setResult(RESULT_OK,intent)
            finish()
        }

    }
    fun getIstrazivanjeByNameAndYear(name:String,year:String): Istrazivanje? {
        return IstrazivanjeRepository.getIstrazivanjeByGodina(Integer.parseInt(year)).find { i->i.naziv==name  }
    }
    fun getGroupByNameAndIstrazivanje(name:String, istrazivanje:String):Grupa?{
        return GrupaRepository.getGroupsByIstrazivanje(istrazivanje).find { g->g.naziv==name }
    }
}