package ba.etf.rma22.projekat

import AnketaListAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ba.etf.rma22.projekat.data.models.Korisnik
import ba.etf.rma22.projekat.data.repositories.AnketaRepository
import ba.etf.rma22.projekat.data.repositories.GrupaRepository
import ba.etf.rma22.projekat.data.repositories.IstrazivanjeRepository
import ba.etf.rma22.projekat.viewmodel.AnketaListViewModel


class FragmentAnkete : Fragment(){

    private lateinit var ankete: RecyclerView
    private lateinit var anketeAdapter: AnketaListAdapter
    private var anketeListViewModel = AnketaListViewModel()
    private lateinit var spinner: Spinner

    private var korisnik= Korisnik()

    lateinit var istrazivanje: String
    private var elementi_spinnera = arrayOf(
        "Sve moje ankete",
        "Sve ankete",
        "Urađene ankete",
        "Buduće ankete",
        "Prošle ankete"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_ankete, container, false)

        istrazivanje="Prazno"

        //spinner
        spinner=view.findViewById(R.id.filterAnketa)
        val arrayAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, elementi_spinnera)
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
        ankete = view.findViewById(R.id.listaAnketa)
        ankete.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        anketeAdapter = AnketaListAdapter(mutableListOf())//praznu listu mu prosljedjujemo inicijalno
        ankete.adapter = anketeAdapter
        anketeAdapter.updateAnkete(anketeListViewModel.getAnkete())
        return view
    }

    companion object {
        fun newInstance(): FragmentAnkete = FragmentAnkete()
    }

    fun promjenaAnketa(o:String){
        if(o=="Sve moje ankete"){
            anketeAdapter.updateAnkete(
                AnketaRepository.getMyAnkete(korisnik.getUpisanaIstrazivanja(),korisnik.getupisaneGrupe()))
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