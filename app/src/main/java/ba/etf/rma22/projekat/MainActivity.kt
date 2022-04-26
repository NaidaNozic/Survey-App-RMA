package ba.etf.rma22.projekat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.fragment.app.Fragment
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Pitanje
import ba.etf.rma22.projekat.view.*


class MainActivity : AppCompatActivity() , PomocniInterfejs {
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ViewPageAdapter
    private lateinit var fragmentPredaj: FragmentPredaj

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.pager)
        val fragments = arrayListOf(FragmentAnkete(), FragmentIstrazivanje())
        adapter = ViewPageAdapter(fragments, this)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        if(adapter.createFragment(0) is FragmentAnkete)adapter.refreshFragment(1, FragmentIstrazivanje())
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
        adapter.refreshFragment(1, FragmentIstrazivanje())
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

    override fun openPitanja(p:List<Pitanje>,anketa: Anketa) {
        val fragments= arrayListOf<Fragment>()
        for(i in 0..p.size){
            val bundle=Bundle()
            val f:Fragment
            if(i==p.size)  {
                fragmentPredaj= FragmentPredaj()
                bundle.putSerializable("anketa",anketa)
                fragmentPredaj.arguments=bundle
                fragments.add(fragmentPredaj)
            }else {
                f= FragmentPitanje()
                bundle.putSerializable("anketa",anketa)
                bundle.putString("pitanje",p[i].tekst)
                val opcije:ArrayList<String> = arrayListOf()
                opcije.addAll(p[i].opcije)
                bundle.putStringArrayList("odgovori",opcije)
                f.arguments=bundle
                fragments.add(f)
            }
        }
        viewPager = findViewById(R.id.pager)
        adapter= ViewPageAdapter(arrayListOf(FragmentAnkete(), FragmentIstrazivanje()),this)
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
}