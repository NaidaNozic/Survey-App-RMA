package ba.etf.rma22.projekat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class FragmentPoruka: Fragment() {
    private lateinit var textView: TextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_poruka, container, false)
        textView=view.findViewById(R.id.tvPoruka)
        val bundle=arguments
        if(bundle!=null){
            textView.text="Uspješno ste upisani u grupu "+bundle.getString("grupa")+" istraživanja "+
                    bundle.getString("istrazivanje")+"!"
        }
        return view
    }
    companion object {
        fun newInstance(): FragmentPoruka = FragmentPoruka()
    }
}