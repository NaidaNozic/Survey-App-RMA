package ba.etf.rma22.projekat.data

import ba.etf.rma22.projekat.data.models.Grupa
import ba.etf.rma22.projekat.data.models.Istrazivanje


fun upisanaIstrazivanja():List<Istrazivanje>{
        return listOf(Istrazivanje("Istraživanje broj 3",3),
            Istrazivanje("Istraživanje broj 2",2),
            Istrazivanje("Istraživanje broj 4", 4))
}
fun upisaneGrupe():List<Grupa>{
    return listOf(Grupa("Grupa 9","Istraživanje broj 3"),
        Grupa("Grupa 2","Istraživanje broj 1"),
        Grupa("Grupa 3","Istraživanje broj 2"),
        Grupa("Grupa 4","Istraživanje broj 2"),
        Grupa("Grupa 8","Istraživanje broj 4"))
}
