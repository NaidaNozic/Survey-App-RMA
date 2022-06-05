package ba.etf.rma22.projekat.data

import ba.etf.rma22.projekat.data.models.Istrazivanje

fun istrazivanja():List<Istrazivanje>{
    return listOf(
        Istrazivanje(1,"Istraživanje broj 1",1),
        Istrazivanje(2,"Istraživanje broj 2",2), Istrazivanje(10,"Istraživanje broj 3",3),
        Istrazivanje(3,"Istraživanje broj 2",3),
        Istrazivanje(4,"Istraživanje broj 4",4), Istrazivanje(9,"Istraživanje broj 5",5),
        Istrazivanje(5,"Istraživanje broj 4",2), Istrazivanje(8,"Istraživanje broj 2",4),
        Istrazivanje(6,"Istraživanje broj 1",2), Istrazivanje(7,"Istraživanje broj 3",2)
    )
}
fun upisanaIstrazivanja():List<Istrazivanje>{
    return listOf(Istrazivanje(10,"Istraživanje broj 3",3),
        Istrazivanje(2,"Istraživanje broj 2",2),
        Istrazivanje(4,"Istraživanje broj 4", 4))
}