package ba.etf.rma22.projekat.data

import ba.etf.rma22.projekat.data.models.Istrazivanje

fun istrazivanja():List<Istrazivanje>{
    return listOf(
        Istrazivanje("Istraživanje broj 1",1),
        Istrazivanje("Istraživanje broj 2",2), Istrazivanje("Istraživanje broj 3",3),
        Istrazivanje("Istraživanje broj 4",4), Istrazivanje("Istraživanje broj 5",5),
        Istrazivanje("Istraživanje broj 4",2), Istrazivanje("Istraživanje broj 2",4),
        Istrazivanje("Istraživanje broj 1",2), Istrazivanje("Istraživanje broj 3",2)
    )
}