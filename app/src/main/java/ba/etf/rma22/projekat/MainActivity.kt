package ba.etf.rma22.projekat

import AnketaListAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Korisnik
import ba.etf.rma22.projekat.data.repositories.AnketaRepository
import ba.etf.rma22.projekat.data.repositories.GrupaRepository
import ba.etf.rma22.projekat.data.repositories.IstrazivanjeRepository
import ba.etf.rma22.projekat.viewmodel.AnketaListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var ankete: RecyclerView
    private lateinit var anketeAdapter: AnketaListAdapter
    private var anketeListViewModel = AnketaListViewModel()
    private lateinit var spinner: Spinner
    private lateinit var button: FloatingActionButton

    private var korisnik=Korisnik()

    lateinit var istrazivanje: String
    private var elementi_spinnera = arrayOf(
        "Sve moje ankete",
        "Sve ankete",
        "Urađene ankete",
        "Buduće ankete",
        "Prošle ankete"
    )
    var startForResult = registerForActivityResult(
        StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.getResultCode() === RESULT_OK) {
                korisnik=result.getData()!!.getSerializableExtra("rezultat")as Korisnik
                //refreshati listu anketa
                promjenaAnketa(spinner.selectedItem.toString())
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        istrazivanje="Prazno"
        //button
        button=findViewById(R.id.upisDugme)

        korisnik.setUpisane(IstrazivanjeRepository.getUpisani())
        korisnik.setupisaneGrupe(GrupaRepository.getUpisani())

        button.setOnClickListener{
            val intent = Intent(this, UpisIstrazivanje::class.java)
            intent.putExtra("poruka",korisnik)
            startForResult.launch(intent)
        }

        //spinner
        spinner=findViewById(R.id.filterAnketa)
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, elementi_spinnera)
        //postavljam layout koji ce se koristiti kada se elementi spinnera pojave
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(arrayAdapter)

        //klik na spinner-a
        spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var odabran=parent!!.getItemAtPosition(position).toString();
                promjenaAnketa(odabran)
            }
        }
        //ankete
        ankete = findViewById(R.id.listaAnketa)
        ankete.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        anketeAdapter = AnketaListAdapter(mutableListOf())//praznu listu mu prosljedjujemo inicijalno
        ankete.adapter = anketeAdapter
        anketeAdapter.updateAnkete(anketeListViewModel.getAnkete())
    }
    fun promjenaAnketa(o:String){
        if(o=="Sve moje ankete"){
            anketeAdapter.updateAnkete(AnketaRepository.getMyAnkete(korisnik.getUpisanaIstrazivanja(),
                korisnik.getupisaneGrupe()))
        }else if(o=="Sve ankete"){
            anketeAdapter.updateAnkete(AnketaRepository.getAll())
        }else if(o=="Urađene ankete"){
            anketeAdapter.updateAnkete(AnketaRepository.getDone(korisnik.getupisaneGrupe()))
        }else if(o=="Buduće ankete"){
            anketeAdapter.updateAnkete(AnketaRepository.getFuture(korisnik.getupisaneGrupe()))
        }else if(o=="Prošle ankete"){
            anketeAdapter.updateAnkete(AnketaRepository.getNotTaken(korisnik.getupisaneGrupe()))
        }
    }
}