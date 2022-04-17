package ba.etf.rma22.projekat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import ba.etf.rma22.projekat.PomocniInterfejs
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.SveAnkete

class FragmentPredaj: Fragment() {
    private lateinit var text:TextView
    private lateinit var button:Button
    private lateinit var sm: PomocniInterfejs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_predaj, container, false)
        text=view.findViewById(R.id.progresTekst)
        button=view.findViewById(R.id.dugmePredaj)

        sm = activity as PomocniInterfejs
        val bundle=arguments
        var progres=(bundle?.getSerializable("anketa") as Anketa).pitanja.size.toFloat()/(sm.getItemCount()-1)//racunam novi progres
            progres=zaokruziProgres(progres)
            text.text=(progres*100).toString().split(".")[0]+"%"

        button.setOnClickListener {
            val anketa: Anketa?

                anketa = (bundle.getSerializable("anketa") as Anketa)
                SveAnkete().izmijeniProgres(anketa.naziv, anketa.nazivIstrazivanja, progres)
                SveAnkete().izmijeniDatumKraj(anketa.naziv)//postavlja datum na danasnji (anketa postaje zavrsena)

                sm.passDataAndGoToPoruka("Završili ste anketu "+anketa.naziv+" u okviru istraživanja " +anketa.nazivIstrazivanja)

        }
        return view
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
    companion object {
        fun newInstance(): FragmentPredaj = FragmentPredaj()
    }
}