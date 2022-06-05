package ba.etf.rma22.projekat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.fragment.app.Fragment
import ba.etf.rma22.projekat.data.models.*
import ba.etf.rma22.projekat.data.repositories.OdgovorRepository
import ba.etf.rma22.projekat.view.*
import ba.etf.rma22.projekat.viewmodel.IstrazivanjeIGrupaViewModel
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() , PomocniInterfejs {
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ViewPageAdapter
    private lateinit var fragmentPredaj: FragmentPredaj
    private var grupe: MutableList<Grupa> = mutableListOf()
    private var istrazivanja: MutableList<Istrazivanje> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        IstrazivanjeIGrupaViewModel().getGrupe(onSuccess = ::onSuccess, onError = ::onError)
        IstrazivanjeIGrupaViewModel().getIstrazivanja(onSuccess = ::onSuccess1, onError = ::onError)

        viewPager = findViewById(R.id.pager)
        val fragments = arrayListOf(FragmentAnkete(), FragmentIstrazivanje(grupe,istrazivanja))
        adapter = ViewPageAdapter(fragments, this)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        if(adapter.createFragment(0) is FragmentAnkete)adapter.refreshFragment(1, FragmentIstrazivanje(grupe,istrazivanja))
                    }
                }
                if(adapter.createFragment(0) is FragmentPitanje && position!=adapter.itemCount-1){
                    adapter.refreshFragment(adapter.itemCount-1,fragmentPredaj)
                }
            }
        })
    }
    override fun izmijeniFragmente() {
        viewPager = findViewById(R.id.pager)
        adapter.refreshFragment(0, FragmentAnkete())
        adapter.refreshFragment(1, FragmentIstrazivanje(grupe,istrazivanja))
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
            if (zapocetaAnketa != null && OdgovorRepository.getOdgovoriAnketa(zapocetaAnketa.id)?.size==p.size) {
                anketa.datumRada = Date()
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
        adapter= ViewPageAdapter(arrayListOf(FragmentAnkete(), FragmentIstrazivanje(grupe,istrazivanja)),this)
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
        for(g in grupe)this.grupe.add(g)
    }
    fun onSuccess1(istrazivanja:List<Istrazivanje>){
        for(i in istrazivanja)this.istrazivanja.add(i)
    }
    fun onError() {
    }
}