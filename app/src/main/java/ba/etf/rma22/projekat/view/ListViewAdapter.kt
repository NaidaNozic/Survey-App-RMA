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

        val tw=view.findViewById<TextView>(R.id.odgovor)
        tw.text=items[position]
        //ako je anketa od prije vec predana (datumRada!=null) onda se ne mogu mijenjati odgovori
        if(fragment.anketa.datumRada==null && fragment.anketa.datumKraj> Date())
        view.setOnClickListener{
            //korisnik ne moze mijenjati odgovore na vec odgovorena pitanja
            if(SveAnkete().getOdgovore(fragment.anketa.naziv,fragment.anketa.nazivIstrazivanja,fragment.pitanje.text.toString())==null) {
                tw.setTextColor(Color.parseColor("#0000FF"))
                SveAnkete().postaviPitanjeIOdabranOdgovor(
                    fragment.anketa.naziv, fragment.anketa.nazivIstrazivanja,
                    fragment.pitanje.text.toString(), tw.text.toString()
                )
            }
        }
        if(fragment.prijasnjiOdgovori?.find { o->o==tw.text }!=null){
            tw.setTextColor(Color.parseColor("#0000FF"))
        }
        return view
    }
}