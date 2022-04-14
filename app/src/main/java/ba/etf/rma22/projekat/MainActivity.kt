package ba.etf.rma22.projekat

import android.app.Fragment
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2


class MainActivity : AppCompatActivity() {
    // private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerAdapter: ViewPageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.view_pager)
        val fragments = arrayListOf(FragmentAnkete(), FragmentIstrazivanje())
        val adapter = ViewPageAdapter(fragments, this)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        //adapter.refreshFragment(0, FragmentAnkete())
                       // adapter.addFragment(FragmentIstrazivanje())
                    }
                }
            }
        })
    }
}