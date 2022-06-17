package ba.etf.rma22.projekat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import ba.etf.rma22.projekat.MainActivity
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.models.*
import ba.etf.rma22.projekat.data.repositories.OdgovorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class FragmentPredaj: Fragment() {
    private lateinit var text:TextView
    private lateinit var button:Button
    private lateinit var sm: PomocniInterfejs
    private lateinit var zapocetaAnketa:AnketaTaken

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_predaj, container, false)
        text = view.findViewById(R.id.progresTekst)
        button = view.findViewById(R.id.dugmePredaj)
        if(!InternetConnection.prisutna)button.isEnabled=false
        val bundle = arguments
        sm=activity as PomocniInterfejs

        val a = bundle?.getSerializable("anketa") as Anketa
        val zapocetaAnketa = bundle?.getSerializable("zapocetaAnketa") as AnketaTaken
        //prosla anketa se ne moze predati
        if(a.datumKraj!=null && a.datumKraj!! <Date() && zapocetaAnketa.progres<1F) button.isEnabled=false
        //prethodno zavrsena anketa se takodjer ne moze predati
        if(a.datumRada!=null)button.isEnabled=false
        var brojOdgovorenihPitanja: Int? = 0

        val job1= GlobalScope.launch (Dispatchers.IO){
            if(InternetConnection.prisutna){
                brojOdgovorenihPitanja = OdgovorRepository.getOdgovoriAnketa(zapocetaAnketa.AnketumId)?.size
            }else{
                brojOdgovorenihPitanja=OdgovorRepository.getOdgovoriAnketa(MainActivity.getContext(),zapocetaAnketa.id)?.size
            }
        }
        runBlocking { job1.join() }

        var progres: Float
        brojOdgovorenihPitanja= brojOdgovorenihPitanja?.plus(SveAnkete.odgovoriPrijePredavanja.size)
        if (brojOdgovorenihPitanja != null) progres =
            brojOdgovorenihPitanja!!.toFloat() / (sm.getItemCount() - 1)//racunam novi progres
        else progres = 0F
        progres = zaokruziProgres(progres)
        text.text = (progres * 100).toString().split(".")[0] + "%"


        button.setOnClickListener {
            var odgovoriKojiNisuPredani=SveAnkete.odgovoriPrijePredavanja
            val job= GlobalScope.launch (Dispatchers.IO){
                for(o in odgovoriKojiNisuPredani){
                    OdgovorRepository.postaviOdgovorAnketa(zapocetaAnketa.id,o.key,o.value)
                }
                SveAnkete.odgovoriPrijePredavanja= mutableMapOf()
            }
            runBlocking { job.join() }
            sm.passDataAndGoToPoruka("Završili ste anketu " + a.naziv + " u okviru istraživanja " + a.nazivIstrazivanja)
        }
        return view
    }
    private fun zaokruziProgres(progres:Float):Float{
        var rez:Float=progres
        if(progres>0 && progres<0.2){
            if(0F+0.1<progres) rez=0F else rez=0.2F
        }else if(progres>0.2 && progres<0.4){
            if(0.2+0.1<progres) rez=0.2F else rez=0.4F
        }else if(progres>0.4 && progres<0.6){
            if(0.4+0.1<progres) rez=0.4F else rez=0.6F
        }else if(progres>0.6 && progres<0.8){
            if(0.6+0.1<progres) rez=0.6F else rez=0.8F
        }else if(progres>0.8 && progres<1){
            if(0.8+0.1<progres) rez=0.8F else rez=1F
        }
        return rez
    }
    companion object {
        fun newInstance(): FragmentPredaj = FragmentPredaj()
    }
}