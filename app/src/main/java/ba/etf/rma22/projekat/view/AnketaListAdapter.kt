import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.models.*
import ba.etf.rma22.projekat.data.repositories.OdgovorRepository
import ba.etf.rma22.projekat.data.repositories.PitanjeAnketaRepository
import ba.etf.rma22.projekat.data.repositories.TakeAnketaRepository
import kotlinx.coroutines.*
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
        var poceteAnkete:List<AnketaTaken>? = listOf()
        var pocetaAnketa:AnketaTaken? =null
        var odgovori:List<Odgovor1> = listOf()
        var brPitanja=0
        var job1: Job = Job()
        if(InternetConnection.prisutna) {
             job1 = GlobalScope.launch(Dispatchers.IO) {
                poceteAnkete = TakeAnketaRepository.getPoceteAnkete()
                //spasiti pocete ankete u bazu
                if(poceteAnkete!=null)
                    for (ap in poceteAnkete!!) TakeAnketaRepository.writeTakenAnkete(holder.nazivIstrazivanja.context,
                                           AnketaTaken(ap.id,ap.student,ap.progres,ap.datumRada,ap.AnketumId,""))

            }
            runBlocking { job1.join() }
        }
        else {
            //dohvatam podatke iz baze
            var job3 = GlobalScope.launch(Dispatchers.IO){
                poceteAnkete = TakeAnketaRepository.getAll(holder.title.context)
            }
            runBlocking { job3.join() }
        }
        pocetaAnketa= poceteAnkete?.find { p->p.AnketumId==a.id }
        if (pocetaAnketa != null) {
            holder.progresZavrsetka.progress=pocetaAnketa.progres/10
        }else holder.progresZavrsetka.progress=0

        //stanje ankete
        var plava=0
        var crvena=0
        var zelena=0
        var zuta=0

        if(a.datumRada!=null){
            //ako smo uradili anketu i predali (datumRada!=null && status=plav)
            if((a.datumKraj==null || a.datumKraj!!>Date()) && a.datumPocetak < Date())plava=1
        }else {
            val job = GlobalScope.launch(Dispatchers.IO) {
                if (pocetaAnketa != null) {
                    if (InternetConnection.prisutna) {
                        odgovori =
                            pocetaAnketa?.let { OdgovorRepository.getOdgovoriAnketa(it.AnketumId) }!!
                        for (o in odgovori)
                            OdgovorRepository.writeOdgovore(
                                holder.title.context,
                                Odgovor1(o.odgovoreno, o.anketaTaken, o.pitanjeId, "")
                            )
                    } else {
                        odgovori = pocetaAnketa?.let {
                            OdgovorRepository.getOdgovoriAnketa(holder.title.context, it.id)
                        }
                    }
                } else odgovori = listOf()

                if (InternetConnection.prisutna) {
                    var pitanja2 = PitanjeAnketaRepository.getPitanja(a.id)
                    brPitanja = pitanja2?.size ?: 0
                    if (pitanja2 != null)
                        for (p in pitanja2) {
                            PitanjeAnketaRepository.writePitanja(
                                holder.title.context,
                                Pitanje(p.id, p.naziv, p.tekstPitanja, p.opcije)
                            )
                            PitanjeAnketaRepository.writePitanjeAnketa(
                                holder.title.context,
                                PitanjeAnketa(a.id, p.id)
                            )
                        }
                } else {
                    var pitanjaAnkete =
                        PitanjeAnketaRepository.getAllPitanjeAnketaById(holder.title.context, a.id)
                    var pitanja2 = mutableListOf<Pitanje>()
                    for (p in pitanjaAnkete) {
                        var pitanje2 = PitanjeAnketaRepository.getPitanjaById(
                            holder.title.context,
                            p.PitanjeId
                        )
                        pitanja2.add(pitanje2)
                    }
                    brPitanja = pitanja2.size
                }
            }
            runBlocking { job.join() }
            if (brPitanja == odgovori.size && brPitanja > 0 && (a.datumKraj == null || a.datumKraj!! > Date()) && a.datumPocetak < Date()) plava =
                1
            else if ((a.datumKraj == null || a.datumKraj!! > Date()) && a.datumPocetak < Date()) {
                //aktivna anketa i moze se jos uraditi
                if (a.progres < 100) zelena = 1
            } else if (a.datumPocetak > Date()) {
                zuta = 1 //buduca
            } else if ((a.datumKraj != null && a.datumKraj!! < Date()) && a.progres < 100) crvena =
                1 //prosla
            else crvena = 1

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