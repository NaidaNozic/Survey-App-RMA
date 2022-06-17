package ba.etf.rma22.projekat.view

import AnketaListAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ba.etf.rma22.projekat.MainActivity
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.models.*
import ba.etf.rma22.projekat.data.repositories.*
import ba.etf.rma22.projekat.viewmodel.AnketaListViewModel
import ba.etf.rma22.projekat.viewmodel.PitanjaViewModel
import kotlinx.coroutines.*
import java.util.*


class FragmentAnkete : Fragment(){
    private lateinit var ankete: RecyclerView
    private lateinit var anketeAdapter: AnketaListAdapter
    private var anketeListViewModel = AnketaListViewModel()
    private lateinit var spinner: Spinner
    private lateinit var sm: PomocniInterfejs
    private lateinit var anketa:Anketa
    private var elementiSpinnera = arrayOf(
        "Sve moje ankete",
        "Sve ankete",
        "Urađene ankete",
        "Buduće ankete",
        "Prošle ankete"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_ankete, container, false)

        //spinner
        spinner=view.findViewById(R.id.filterAnketa)
        val arrayAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, elementiSpinnera)
        //postavljam layout koji ce se koristiti kada se elementi spinnera pojave
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter=arrayAdapter

        //klik na spinner-a
        spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val odabran=parent!!.getItemAtPosition(position).toString()
                promjenaAnketa(odabran)
            }
        }
        //ankete
        ankete = view.findViewById(R.id.listaAnketa)
        ankete.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        anketeAdapter = AnketaListAdapter(mutableListOf()) { anketa -> showAnketa(anketa) }
        ankete.adapter = anketeAdapter
        if(InternetConnection.prisutna) {
            anketeListViewModel.getMyAnkete(onSuccess = ::onSuccess1, onError = ::onError)
        }
        else{
            var ankete: List<Anketa>
            runBlocking { ankete=AnketaRepository.getAll() }
          /*  var aa= mutableListOf<Anketa>()
            aa=ankete.toMutableList()
            aa.removeAll { a->a.upisana==0 }*/
            anketeAdapter.updateAnkete(ankete.toMutableList())
        }
        preuzmiSveGrupe()
        preuzmiSvaIstrazivanja()
        return view
    }
    companion object {
        fun newInstance(): FragmentAnkete = FragmentAnkete()
    }
    fun preuzmiSveGrupe(){
        runBlocking {
            SveAnkete.sveGrupe= IstrazivanjeIGrupaRepository.getGrupe()?.toMutableList() ?: mutableListOf()
        }
    }
    fun preuzmiSvaIstrazivanja(){
        runBlocking {
            SveAnkete.svaIstrazivanja=IstrazivanjeIGrupaRepository.getIstrazivanja()?.toMutableList() ?: mutableListOf()
        }
    }

    fun promjenaAnketa(o:String){
        if(o=="Sve moje ankete"){
            if(InternetConnection.prisutna)
                anketeListViewModel.getMyAnkete( onSuccess = ::onSuccess1, onError = ::onError)
            else {
                var ankete: List<Anketa>?
                runBlocking { ankete= AnketaRepository.getUpisane() }
                var aa= mutableListOf<Anketa>()
                aa= ankete?.toMutableList() ?:mutableListOf<Anketa>()
                aa.removeAll { a->a.upisana==0 }
                ankete?.let { anketeAdapter.updateAnkete(it.toMutableList()) }
            }
        }else if(o=="Sve ankete"){
            if(InternetConnection.prisutna)
            anketeListViewModel.getAnketeByOffset(onSuccess = ::onSuccess1, onError = ::onError)
          else{
                var ankete: List<Anketa>
                runBlocking { ankete=AnketaRepository.getAll() }
                anketeAdapter.updateAnkete(ankete)
            }
        }else if(o=="Urađene ankete"){
            if(InternetConnection.prisutna){
                anketeListViewModel.getDone( onSuccess = ::onSuccess1, onError = ::onError)
            }else{
                var ankete: List<Anketa>
                runBlocking { ankete= AnketaRepository.getDone()!! }
                anketeAdapter.updateAnkete(ankete)
            }
        }else if(o=="Buduće ankete"){
            anketeListViewModel.getFuture(onSuccess = ::onSuccess1, onError = ::onError)
        }else if(o=="Prošle ankete"){
            anketeListViewModel.getNotTaken(onSuccess = ::onSuccess1, onError = ::onError)
        }
    }
    private fun showAnketa(anketa: Anketa){
        //buduce ankete ne moze otvoriti
        if(anketa.datumPocetak> Date())return
        //samo ankete na koje je upisan moze otvoriti
        this.anketa=anketa
        sm = activity as PomocniInterfejs
        PitanjaViewModel().getPitanjaByAnketa(anketa.id,onSuccess = ::onSuccess, onError = ::onError)
    }
    fun onSuccess(pitanja:List<Pitanje>){
    var zapocetaAnketa:AnketaTaken? =null
        val job=GlobalScope.launch (Dispatchers.IO){
            if(InternetConnection.prisutna){
                var result = TakeAnketaRepository.getPoceteAnkete()
                if(result!=null)for(r in result){
                    TakeAnketaRepository.writeTakenAnkete(MainActivity.getContext(),
                        AnketaTaken(r.id,r.student,r.progres,r.datumRada,r.AnketumId,"")
                    )
                }
                zapocetaAnketa=result?.find { a->a.AnketumId==anketa.id}
                if(zapocetaAnketa==null){zapocetaAnketa=TakeAnketaRepository.zapocniAnketu(anketa.id) }
            }else{
                var result=TakeAnketaRepository.getAll(MainActivity.getContext())
                zapocetaAnketa=result?.find { a->a.AnketumId==anketa.id}
            }
        }
        runBlocking {
            job.join()
        }
        if(zapocetaAnketa==null) {
            onError()
            return
        }
        sm.openPitanja(pitanja,anketa,zapocetaAnketa)
    }
    fun onError() {
        val toast = Toast.makeText(context, "Niste upisani na ovu anketu", Toast.LENGTH_SHORT)
        toast.show()
    }
    fun onSuccess1(ankete:List<Anketa>){
        anketeAdapter.updateAnkete(ankete)
    }
}