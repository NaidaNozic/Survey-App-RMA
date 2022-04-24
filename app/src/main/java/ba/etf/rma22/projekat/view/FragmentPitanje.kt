package ba.etf.rma22.projekat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.SveAnkete
import java.util.*


class FragmentPitanje: Fragment() {
    lateinit var pitanje:TextView
    private lateinit var button:Button
    private lateinit var odgovori:ListView
    private lateinit var sm: PomocniInterfejs
    var prijasnjiOdgovori:MutableList<String>? = null
    lateinit var anketa:Anketa

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pitanje, container, false)
        pitanje=view.findViewById(R.id.tekstPitanja)
        odgovori=view.findViewById(R.id.odgovoriLista)
        button=view.findViewById(R.id.dugmeZaustavi)
        val bundle=arguments
        anketa=bundle?.getSerializable("anketa")as Anketa

        pitanje.text=bundle.getString("pitanje")

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
        prijasnjiOdgovori=SveAnkete().getOdgovore(anketa.naziv,anketa.nazivIstrazivanja,pitanje.text.toString())

        val adapter=ListViewAdapter(view.context, R.layout.list_item,listt,this)
            odgovori.adapter = adapter


        button.setOnClickListener{
            sm = activity as PomocniInterfejs
            //izmijeniti progres
            if(anketa.datumRada==null && anketa.datumKraj> Date()) {
                var progres =
                    anketa.pitanja.size.toFloat() / (sm.getItemCount() - 1)//racunam novi progres
                progres = zaokruziProgres(progres)
                SveAnkete().izmijeniProgres(anketa.naziv, anketa.nazivIstrazivanja, progres)
            }
            sm.izmijeniFragmente()
        }
        return view
    }
    companion object {
        fun newInstance(): FragmentPitanje = FragmentPitanje()
    }
    private fun zaokruziProgres(progres:Float):Float{
        var rez:Float=progres
        if(progres>0 && progres<0.2){
            if(0F+0.1<progres) rez=0.2F else rez=0F
        }else if(progres>0.2 && progres<0.4){
            if(0.2+0.1<progres) rez=0.4F else rez=0.2F
        }else if(progres>0.4 && progres<0.6){
            if(0.4+0.1<progres) rez=0.6F else rez=0.4F
        }else if(progres>0.6 && progres<0.8){
            if(0.6+0.1<progres) rez=0.8F else rez=0.6F
        }else if(progres>0.8 && progres<1){
            if(0.8+0.1<progres) rez=1F else rez=0.8F
        }
        return rez
    }
}