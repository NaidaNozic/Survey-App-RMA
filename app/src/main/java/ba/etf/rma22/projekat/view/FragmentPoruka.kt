package ba.etf.rma22.projekat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ba.etf.rma22.projekat.R

class FragmentPoruka: Fragment() {
    private lateinit var textView: TextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_poruka, container, false)
        textView=view.findViewById(R.id.tvPoruka)
        val bundle=arguments
        if(bundle!=null){
            textView.text=bundle.getString("poruka")
        }
        return view
    }
    companion object {
        fun newInstance(): FragmentPoruka = FragmentPoruka()
    }
}