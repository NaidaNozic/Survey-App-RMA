package ba.etf.rma22.projekat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.fragment.app.Fragment
import ba.etf.rma22.projekat.data.models.*
import ba.etf.rma22.projekat.data.repositories.*
import ba.etf.rma22.projekat.view.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() , PomocniInterfejs {
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ViewPageAdapter
    private lateinit var fragmentPredaj: FragmentPredaj
    private val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
    private val br: BroadcastReceiver = MyBroadcastReceiver()

    companion object Companion {
        private lateinit var context: Context

        fun setContext(con: Context) {
            context = con
        }
        fun getContext():Context{
            return context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContext(this)

       // context.deleteDatabase("RMA22DB")
        if(intent?.action == Intent.ACTION_VIEW)
            postaviHash(intent)
        viewPager = findViewById(R.id.pager)

        val fragments = arrayListOf(FragmentAnkete(), FragmentIstrazivanje(/*this.grupe,this.istrazivanja*/))
        adapter = ViewPageAdapter(fragments, this)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        if(adapter.createFragment(0) is FragmentAnkete)adapter.refreshFragment(1, FragmentIstrazivanje(/*grupe,istrazivanja*/))
                    }
                }
                if(adapter.createFragment(0) is FragmentPitanje && position!=adapter.itemCount-1){
                    adapter.refreshFragment(adapter.itemCount-1,fragmentPredaj)
                }
            }
        })
    }
    private fun postaviHash(intent: Intent) {
        var poruka=""
        intent.getStringExtra("payload")?.let {
            runBlocking {
                poruka=it
                AccountRepository.postaviHash(poruka)
            }
           // Toast.makeText(this, poruka, Toast.LENGTH_SHORT).show()
        }
    }
    override fun izmijeniFragmente() {
        viewPager = findViewById(R.id.pager)
        adapter.refreshFragment(0, FragmentAnkete())
        adapter.refreshFragment(1, FragmentIstrazivanje(/*grupe,istrazivanja*/))
        viewPager.adapter = adapter
        viewPager.currentItem=0
    }

    override fun izmijeniSaFragmentPorukom() {
        viewPager = findViewById(R.id.pager)
        adapter.refreshFragment(0, FragmentAnkete())
        adapter.refreshFragment(1, FragmentPoruka())
        viewPager.adapter = adapter
        viewPager.currentItem=1
    }

    override fun passDataAndGoToPoruka(poruka:String) {
        val bundle=Bundle()
        bundle.putString("poruka",poruka)
        var fragmentPoruka:Fragment = FragmentPoruka()
        fragmentPoruka.arguments=bundle

        viewPager = findViewById(R.id.pager)
        adapter.refreshFragment(0, FragmentAnkete())
        adapter.refreshFragment(1,fragmentPoruka)
        //ostale obrisati
        var i=2
        while(i<adapter.itemCount){
            adapter.remove(i)
        }
        viewPager.adapter = adapter
        viewPager.currentItem=1
    }

    override fun openPitanja(p:List<Pitanje>,anketa: Anketa,zapocetaAnketa:AnketaTaken?) {
        val job=GlobalScope.launch (Dispatchers.IO){
            if(InternetConnection.prisutna){
                var odg= zapocetaAnketa?.let { OdgovorRepository.getOdgovoriAnketa(it.AnketumId) }
                if (zapocetaAnketa != null && odg?.size==p.size) {
                    anketa.datumRada = Date()
                }
                if(odg!=null)
                    for(l in odg) OdgovorRepository.writeOdgovore(getContext(), Odgovor1(l.odgovoreno,l.anketaTaken,l.pitanjeId))
            }else{
                var odgovori= zapocetaAnketa?.let {OdgovorRepository.getOdgovoriAnketa(context,it.id)}
                    if(odgovori != null && zapocetaAnketa != null && odgovori.size==p.size){
                        anketa.datumRada = Date()
                    }
            }
        }
        runBlocking { job.join() }
        val fragments= arrayListOf<Fragment>()
        for(i in 0..p.size){
            val bundle=Bundle()
            val f:Fragment
            if(i==p.size)  {
                fragmentPredaj= FragmentPredaj()
                bundle.putSerializable("anketa",anketa)
                bundle.putInt("brojPitanja",p.size)
                bundle.putSerializable("zapocetaAnketa",zapocetaAnketa)
                fragmentPredaj.arguments=bundle
                fragments.add(fragmentPredaj)
            }else {
                f= FragmentPitanje()
                bundle.putSerializable("anketa",anketa)
                bundle.putSerializable("pitanje",p[i])
                bundle.putSerializable("zapocetaAnketa",zapocetaAnketa)
                val opcije:ArrayList<String> = arrayListOf()
                opcije.addAll(p[i].opcije)
                bundle.putStringArrayList("odgovori",opcije)
                f.arguments=bundle
                fragments.add(f)
            }
        }
        viewPager = findViewById(R.id.pager)
        adapter= ViewPageAdapter(arrayListOf(FragmentAnkete(), FragmentIstrazivanje(/*grupe,istrazivanja*/)),this)
        adapter.refreshFragment(0,fragments.get(0))
        adapter.refreshFragment(1,fragments.get(1))
        for(i in 2 until fragments.size)
            adapter.add(i,fragments.get(i))
        viewPager.adapter = adapter
        viewPager.currentItem=0
    }

    override fun getItemCount(): Int {
        return adapter.itemCount
    }
    fun onSuccess(grupe:List<Grupa>){
       // for(g in grupe)this.grupe.add(g)
        SveAnkete.sveGrupe=grupe.toMutableList()
        Log.d("SVE GRUPE:",SveAnkete.sveGrupe.size.toString())
    }
    fun onSuccess1(istrazivanja:List<Istrazivanje>){
       // for(i in istrazivanja)this.istrazivanja.add(i)
        SveAnkete.svaIstrazivanja=istrazivanja.toMutableList()
        Log.d("SVE ISTRAZIAVNJA:",SveAnkete.svaIstrazivanja.size.toString())
    }
    fun onError() {
        val toast = Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT)
        toast.show()
    }
    override fun onResume() {
        super.onResume()
        registerReceiver(br, intentFilter)
    }

    override fun onPause() {
        unregisterReceiver(br)
        super.onPause()
    }
}