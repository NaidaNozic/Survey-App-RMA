package ba.etf.rma22.projekat.view

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.coroutines.CoroutineScope

class ViewPageAdapter(val items:ArrayList<Fragment>, activity: AppCompatActivity):FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return items[position]
    }
    fun add(index: Int, fragment: Fragment) {
        items.add(index, fragment)
        notifyItemChanged(index)
    }

    fun refreshFragment(index: Int, fragment: Fragment) {
        items[index] = fragment
        notifyItemChanged(index)
    }

    fun remove(index: Int) {
        items.removeAt(index)
        notifyItemChanged(index)
    }

    override fun getItemId(position: Int): Long {
        return items[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return items.find { it.hashCode().toLong() == itemId } != null
    }
}