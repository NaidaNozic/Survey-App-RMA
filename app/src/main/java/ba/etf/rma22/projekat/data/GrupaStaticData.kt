package ba.etf.rma22.projekat.data

import ba.etf.rma22.projekat.data.models.Grupa

fun grupe():List<Grupa>{
    //svako istrazivanje ima min 2 grupe
    return listOf(
    Grupa("Grupa 1","Istraživanje broj 1"),
    Grupa("Grupa 2","Istraživanje broj 1"),
    Grupa("Grupa 3","Istraživanje broj 2"),
    Grupa("Grupa 4","Istraživanje broj 2"),
    Grupa("Grupa 5","Istraživanje broj 2"),
    Grupa("Grupa 5","Istraživanje broj 5"),
    Grupa("Grupa 6","Istraživanje broj 5"),
    Grupa("Grupa 7","Istraživanje broj 4"),
    Grupa("Grupa 8","Istraživanje broj 4"),
    Grupa("Grupa 9","Istraživanje broj 3"),
    Grupa("Grupa 10","Istraživanje broj 3")
    )
}
fun upisaneGrupe():List<Grupa>{
    return listOf(Grupa("Grupa 9","Istraživanje broj 3"),
        Grupa("Grupa 3","Istraživanje broj 2"),
        Grupa("Grupa 4","Istraživanje broj 2"),
        Grupa("Grupa 8","Istraživanje broj 4"))
}