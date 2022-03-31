package ba.etf.rma22.projekat

import AnketaListAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ba.etf.rma22.projekat.viewmodel.AnketaListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    private lateinit var ankete: RecyclerView
    private lateinit var anketeAdapter: AnketaListAdapter
    private var anketeListViewModel = AnketaListViewModel()
    private lateinit var spinner: Spinner
    private lateinit var button: FloatingActionButton


    lateinit var istrazivanje: String
    private var rezultat:String?=""
    private var elementi_spinnera = arrayOf("Sve moje ankete", "Sve ankete",
        "Urađene ankete", "Buduće ankete", "Prošle (neurađene) ankete")

    var startForResult = registerForActivityResult(
        StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.getResultCode() === RESULT_OK) {
                rezultat=result.getData()!!.getStringExtra("rezultat")
                istrazivanje=rezultat!!.split("::")[1]
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        istrazivanje="Prazno"
        //button
        button=findViewById(R.id.upisDugme)
        button.setOnClickListener{
            val intent = Intent(this, UpisIstrazivanje::class.java)
            intent.putExtra("poruka",istrazivanje)
            startForResult.launch(intent)
        }

        //spinner
        spinner=findViewById(R.id.filterAnketa)
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, elementi_spinnera)
        //postavljam layout koji ce se koristiti kada se elementi spinnera pojave
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.setAdapter(arrayAdapter)

        //ankete
        ankete = findViewById(R.id.listaAnketa)
        ankete.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        anketeAdapter = AnketaListAdapter(listOf())//praznu listu mu prosljedjujemo inicijalno
        ankete.adapter = anketeAdapter
        anketeAdapter.updateAnkete(anketeListViewModel.getAnkete())
    }
}