import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.AnketaTaken
import ba.etf.rma22.projekat.data.models.Odgovor
import ba.etf.rma22.projekat.data.models.SveAnkete
import ba.etf.rma22.projekat.data.repositories.OdgovorRepository
import ba.etf.rma22.projekat.data.repositories.PitanjeAnketaRepository
import ba.etf.rma22.projekat.data.repositories.TakeAnketaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class AnketaListAdapter(private var ankete: List<Anketa>,
                        private val onItemClicked: (anketa:Anketa) -> Unit
) : RecyclerView.Adapter<AnketaListAdapter.AnketaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnketaViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.anketa_item, parent, false)
        return AnketaViewHolder(view)
    }
    override fun getItemCount(): Int = ankete.size
    override fun onBindViewHolder(holder: AnketaViewHolder, position: Int) {
        var a=ankete[position]

        holder.nazivIstrazivanja.text=a.nazivIstrazivanja
        holder.title.text = a.naziv
        var poceteAnkete:List<AnketaTaken> = listOf()
        var pocetaAnketa:AnketaTaken? =null
        var odgovori:List<Odgovor> = listOf()
        var brPitanja=0
        val job1=GlobalScope.launch (Dispatchers.IO) {
            poceteAnkete = TakeAnketaRepository.getPoceteAnkete()!!
        }
        runBlocking { job1.join() }
        pocetaAnketa=poceteAnkete.find { p->p.idAnkete==a.id }
      /*  val tmp:Int?

            tmp= pocetaAnketa?.progres
            if(tmp!=null && SveAnkete.upisanaIstrazivanja.find { i->i==a.nazivIstrazivanja }!=null
                && SveAnkete.upisaneGrupe.find { g->g==a.nazivGrupe }!=null){
                    a.progres=tmp
                Log.d("PROGRESS:",tmp.toString())
            }*/

        holder.progresZavrsetka.progress=a.progres/10

        //stanje ankete
        var plava=0
        var crvena=0
        var zelena=0
        var zuta=0


        if(a.datumRada!=null){
            //ako smo uradili anketu i predali (datumRada!=null && status=plav)
            if((a.datumKraj==null || a.datumKraj!!>Date()) && a.datumPocetak < Date())plava=1
        }
        else{
            val job=GlobalScope.launch (Dispatchers.IO) {
                if(pocetaAnketa!=null)
                    odgovori= pocetaAnketa?.let { OdgovorRepository.getOdgovoriAnketa( it.id) }!!
                else odgovori= listOf()
                brPitanja= PitanjeAnketaRepository.getPitanja(a.id)?.size ?: 0
            }
            runBlocking { job.join() }
            if(brPitanja==odgovori.size && brPitanja>0 && (a.datumKraj==null || a.datumKraj!!>Date()) && a.datumPocetak < Date())plava=1
            if((a.datumKraj==null || a.datumKraj!!>Date()) && a.datumPocetak < Date()){
                //aktivna anketa i moze se jos uraditi
                if (a.progres < 100) zelena = 1
            }else if(a.datumPocetak > Date()){
                zuta = 1 //buduca
            }else if((a.datumKraj !=null && a.datumKraj!! < Date()) && a.progres < 100) crvena = 1 //prosla
            else crvena=1
        }

        val context: Context = holder.stanje.getContext()

        val calendar = Calendar.getInstance()

        val id:Int
        if(crvena==1){
            id = context.getResources()
                .getIdentifier("crvena", "drawable", context.getPackageName())
            holder.datum.text="Anketa zatvorena: "
            if (a.datumKraj!=null) calendar.time = a.datumKraj
            else calendar.time=Date()
        }else if(zelena==1){
            id = context.getResources()
                .getIdentifier("zelena", "drawable", context.getPackageName())
            holder.datum.text="Vrijeme zatvaranja: "
            if (a.datumKraj!=null) calendar.time = a.datumKraj
            else calendar.time=Date()
        }else if(plava==1){
            id = context.getResources()
                .getIdentifier("plava", "drawable", context.getPackageName())
            holder.datum.text="Anketa urađena: "
            calendar.time = pocetaAnketa?.datumRada
        }else if(zuta==1){
            id = context.getResources()
                .getIdentifier("zuta", "drawable", context.getPackageName())
            holder.datum.text="Vrijeme aktiviranja: "
            calendar.time = a.datumPocetak
        }else {
            id = context.getResources()
                .getIdentifier("zuta", "drawable", context.getPackageName())
            holder.datum.text="Vrijeme aktiviranja: "
            calendar.time = a.datumPocetak
        }
        val god=calendar.get(Calendar.YEAR)
        val mjesec=calendar.get(Calendar.MONTH)
        val dan=calendar.get(Calendar.DAY_OF_MONTH)
        holder.datum1.text=dan.toString()+"."+mjesec+"."+god.toString()
        holder.stanje.setImageResource(id)
        holder.itemView.setOnClickListener{ onItemClicked(ankete[position]) }//ovo se desava kada se klikne na anketu
    }
    fun updateAnkete(a: List<Anketa>) {
        this.ankete = a
        notifyDataSetChanged()
    }
    inner class AnketaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.naziv)
        val nazivIstrazivanja: TextView = itemView.findViewById(R.id.nazivIstrazivanja)
        val progresZavrsetka: ProgressBar = itemView.findViewById(R.id.progresZavrsetka)
        val stanje: ImageView = itemView.findViewById(R.id.stanje)
        val datum: TextView = itemView.findViewById(R.id.datum)
        val datum1: TextView = itemView.findViewById(R.id.datum1)
    }
}