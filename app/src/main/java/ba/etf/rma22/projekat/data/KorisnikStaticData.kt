package ba.etf.rma22.projekat.data

import ba.etf.rma22.projekat.data.models.Grupa
import ba.etf.rma22.projekat.data.models.Istrazivanje


fun upisanaIstrazivanja():List<Istrazivanje>{
        return listOf(Istrazivanje("Istraživanje broj 1",1),
            Istrazivanje("Istraživanje broj 2",2))
}
fun upisaneGrupe():List<Grupa>{
    return listOf(Grupa("Grupa 1","Istraživanje broj 1"),
        Grupa("Grupa 2","Istraživanje broj 1"),
        Grupa("Grupa 3","Istraživanje broj 2"),
        Grupa("Grupa 4","Istraživanje broj 2"))
}
