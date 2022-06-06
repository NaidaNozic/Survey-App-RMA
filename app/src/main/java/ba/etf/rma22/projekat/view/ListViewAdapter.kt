package ba.etf.rma22.projekat.view

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.models.SveAnkete
import ba.etf.rma22.projekat.data.repositories.OdgovorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


class ListViewAdapter(
    context:Context,
    private val resources:Int,
    private var items:Array<String?>,fragment: FragmentPitanje
):ArrayAdapter<String>(context,resources,items) {
    private var fragment=fragment

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater=LayoutInflater.from(context)
        val view:View= inflater.inflate(resources,null)
        var predana=false
        val tw=view.findViewById<TextView>(R.id.odgovor)
        tw.text=items[position]

        //ako je anketa od prije vec predana onda se ne mogu mijenjati odgovori
        val job= GlobalScope.launch (Dispatchers.IO){
            if(OdgovorRepository.getOdgovoriAnketa(fragment.zapocetaAnketa.AnketumId)?.size==fragment.brojPitanja)predana=true
        }
        runBlocking { job.join() }
        if(predana==false && (fragment.anketa.datumKraj==null || fragment.anketa.datumKraj!! > Date()))
        view.setOnClickListener{
            if(fragment.prijasnjiOdgovori?.find { o->o.odgovoreno==position+1 && o.pitanjeId==fragment.pitanje.id } ==null &&
               fragment.prijasnjiOdgovori?.find { o->o.pitanjeId==fragment.pitanje.id } ==null &&
                    SveAnkete.odgovoriPrijePredavanja.get(fragment.pitanje.id)==null &&
                (fragment.anketa.datumKraj==null || fragment.anketa.datumKraj!! >Date())){
                    //jedino se moze odgovarati na trenutno aktivne ankete
                        //samo je jedan odgovor moguce oznaciti
                tw.setTextColor(Color.parseColor("#0000FF"))
                SveAnkete.odgovoriPrijePredavanja.put(fragment.pitanje.id,position)
            }
        }
        if(fragment.prijasnjiOdgovori?.find { o->o.odgovoreno==position && o.pitanjeId==fragment.pitanje.id } !=null)
            tw.setTextColor(Color.parseColor("#0000FF"))
        return view
    }
}