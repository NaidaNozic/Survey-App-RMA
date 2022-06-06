package ba.etf.rma22.projekat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.models.*
import ba.etf.rma22.projekat.data.repositories.OdgovorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking



class FragmentPitanje: Fragment() {
    lateinit var pitanjenaEkranu:TextView
    lateinit var pitanje:Pitanje
    private lateinit var button:Button
    private lateinit var odgovori:ListView
    private lateinit var sm: PomocniInterfejs
    var prijasnjiOdgovori:List<Odgovor>? = null
    lateinit var anketa:Anketa
    lateinit var zapocetaAnketa:AnketaTaken
    var brojPitanja:Int =0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pitanje, container, false)
        pitanjenaEkranu=view.findViewById(R.id.tekstPitanja)
        odgovori=view.findViewById(R.id.odgovoriLista)
        button=view.findViewById(R.id.dugmeZaustavi)
        sm = activity as PomocniInterfejs
        brojPitanja=sm.getItemCount()-1
        val bundle=arguments
        anketa=bundle?.getSerializable("anketa")as Anketa
        this.zapocetaAnketa=bundle?.getSerializable("zapocetaAnketa")as AnketaTaken
        pitanje=bundle?.getSerializable("pitanje")as Pitanje
        pitanjenaEkranu.text=pitanje.tekstPitanja

        val listItems=bundle.getStringArrayList("odgovori")
        val n: Int? = listItems?.size
        var n1 =0
        if(n!=null)n1=n
        val listt= arrayOfNulls<String>(n1)
        if (listItems != null) {
            for(i in 0 until n1){
                listt[i]=listItems[i]
            }
        }
        val job=GlobalScope.launch (Dispatchers.IO){
            prijasnjiOdgovori=OdgovorRepository.getOdgovoriAnketa(zapocetaAnketa.AnketumId)
        }
        runBlocking { job.join() }
        val adapter=ListViewAdapter(view.context, R.layout.list_item,listt,this)
            odgovori.adapter = adapter

        button.setOnClickListener{
            sm = activity as PomocniInterfejs
            //brise odabrane odgovore jer ih nije predao, vec je kliknuo na zaustavi anketu
            SveAnkete.odgovoriPrijePredavanja= mutableMapOf()
            sm.izmijeniFragmente()
        }
        return view
    }
    companion object {
        fun newInstance(): FragmentPitanje = FragmentPitanje()
    }
}