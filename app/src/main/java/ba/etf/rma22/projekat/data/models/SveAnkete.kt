package ba.etf.rma22.projekat.data.models

import ba.etf.rma22.projekat.data.*
import java.util.*

class SveAnkete {
    companion object GlobalneAnkete{
        //ove ankete su globalne i mogu im atribute mijenjati
        var ankete= ankete().toMutableList()
    }
}