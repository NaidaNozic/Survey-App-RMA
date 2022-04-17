package ba.etf.rma22.projekat

import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Pitanje

interface PomocniInterfejs {
    fun izmijeniFragmente()
    fun izmijeniSaFragmentPorukom()
    fun passDataAndGoToPoruka(poruka:String)
    fun openPitanja(pitanja:List<Pitanje>,anketa: Anketa)
    fun getItemCount():Int
}