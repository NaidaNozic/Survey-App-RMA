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
import ba.etf.rma22.projekat.data.models.Korisnik
import ba.etf.rma22.projekat.data.repositories.AnketaRepository
import ba.etf.rma22.projekat.data.repositories.GrupaRepository
import ba.etf.rma22.projekat.data.repositories.IstrazivanjeRepository
import ba.etf.rma22.projekat.viewmodel.AnketaListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    private lateinit var ankete: RecyclerView
    private lateinit var anketeAdapter: AnketaListAdapter
    private var anketeListViewModel = AnketaListViewModel()
    private lateinit var spinner: Spinner
    private lateinit var button: FloatingActionButton

    private var korisnik=Korisnik()

    lateinit var istrazivanje: String
    private var elementi_spinnera = arrayOf("Sve ankete","Sve moje ankete",
        "Urađene ankete", "Buduće ankete", "Prošle (neurađene) ankete")

    var startForResult = registerForActivityResult(
        StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.getResultCode() === RESULT_OK) {
                korisnik=result.getData()!!.getSerializableExtra("rezultat")as Korisnik
                //refreshati listu anketa
                var noveAnkete=anketeListViewModel.getAnkete().toMutableList()
                noveAnkete.removeAll { a->a.nazivIstrazivanja!=korisnik.getPosljednjeOdabranoIstrazivanje() ||
                                          a.nazivGrupe!=korisnik.getPosljednjeOdabranaGrupa()}
                anketeAdapter.updateAnkete(noveAnkete)
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
        spinner!!.setAdapter(arrayAdapter)

        //klik na spinner-a
        spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var odabran=parent!!.getItemAtPosition(position).toString();
                if(odabran=="Sve moje ankete"){
                    anketeAdapter.updateAnkete(AnketaRepository.getMyAnkete())
                }else if(odabran=="Sve ankete"){
                    anketeAdapter.updateAnkete(AnketaRepository.getAll())
                }else if(odabran=="Urađene ankete"){
                    anketeAdapter.updateAnkete(AnketaRepository.getDone())
                }else if(odabran=="Buduće ankete"){
                    anketeAdapter.updateAnkete(AnketaRepository.getFuture())
                }else if(odabran=="Prošle (neurađene) ankete"){
                    anketeAdapter.updateAnkete(AnketaRepository.getNotTaken())
                }
            }
        }

        //ankete
        ankete = findViewById(R.id.listaAnketa)
        ankete.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        anketeAdapter = AnketaListAdapter(listOf())//praznu listu mu prosljedjujemo inicijalno
        ankete.adapter = anketeAdapter
        anketeAdapter.updateAnkete(anketeListViewModel.getAnkete())
    }
}