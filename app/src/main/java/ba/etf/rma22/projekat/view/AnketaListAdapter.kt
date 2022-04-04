import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.models.Anketa
import java.util.*

class AnketaListAdapter(private var ankete: MutableList<Anketa>) : RecyclerView.Adapter<AnketaListAdapter.AnketaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnketaViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.anketa_item, parent, false)
        return AnketaViewHolder(view)
    }
    override fun getItemCount(): Int = ankete.size
    override fun onBindViewHolder(holder: AnketaViewHolder, position: Int) {
        var a=ankete[position]
        holder.title.text = a.naziv
        holder.nazivIstrazivanja.text=a.nazivIstrazivanja

        if(a.progres>0 && a.progres<0.2){
            if(0F+0.1<a.progres) a.progres=0.2F else a.progres=0F
        }else if(a.progres>0.2 && a.progres<0.4){
            if(0.2+0.1<a.progres) a.progres=0.4F else a.progres=0.2F
        }else if(a.progres>0.4 && a.progres<0.6){
            if(0.4+0.1<a.progres) a.progres=0.6F else a.progres=0.4F
        }else if(a.progres>0.6 && a.progres<0.8){
            if(0.6+0.1<a.progres) a.progres=0.8F else a.progres=0.6F
        }else if(a.progres>0.8 && a.progres<1){
            if(0.8+0.1<a.progres) a.progres=1F else a.progres=0.8F
        }
        val pom:Int =(a.progres*10).toInt()
        holder.progresZavrsetka.progress=pom

        //stanje ankete
        var plava=0
        var crvena=0
        var zelena=0
        var zuta=0
        if(a.datumKraj> Date() && a.datumPocetak<Date()){
            //anketa je aktivna
            if(a.progres==1F && a.datumRada!=null)plava=1
            else if(a.progres<1F && a.datumRada!=null)zelena=1
        }else if(a.datumPocetak>Date())zuta=1
        else if(a.datumKraj<Date() && a.progres<1F)crvena=1

        val context: Context = holder.stanje.getContext()

        val calendar = Calendar.getInstance()

        val id:Int
        if(crvena==1){
            id = context.getResources()
                .getIdentifier("crvena", "drawable", context.getPackageName())
            holder.datum.text="Anketa zatvorena: "
            calendar.time = a.datumKraj
        }else if(zelena==1){
            id = context.getResources()
                .getIdentifier("zelena", "drawable", context.getPackageName())
            holder.datum.text="Vrijeme zatvaranja: "
            calendar.time = a.datumKraj
        }else if(plava==1){
            id = context.getResources()
                .getIdentifier("plava", "drawable", context.getPackageName())
            holder.datum.text="Anketa urađena: "
            calendar.time = a.datumRada!!
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
    }
    fun updateAnkete(a: List<Anketa>) {
        this.ankete = a.toMutableList()
        notifyDataSetChanged()
    }
    fun addAnketu(a:Anketa){
        this.ankete.add(a)
        notifyItemInserted(ankete.size - 1);
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