package ba.etf.rma22.projekat.data

import ba.etf.rma22.projekat.data.models.Grupa

fun grupe():List<Grupa>{
    //svako istrazivanje ima min 2 grupe
    return listOf(
    Grupa(1,"Grupa 1",1),
    Grupa(2,"Grupa 2",1),
    Grupa(3,"Grupa 3",2),
    Grupa(4,"Grupa 4",2),
    Grupa(5,"Grupa 5",3),
    Grupa(6,"Grupa 5",3),
    Grupa(7,"Grupa 6",4),
    Grupa(8,"Grupa 7",4),
    Grupa(9,"Grupa 8",5),
    Grupa(10,"Grupa 9",5),
    Grupa(11,"Grupa 10",6)
    )
}
fun upisaneGrupe():List<Grupa>{
    return listOf(Grupa(10,"Grupa 9",5),
        Grupa(3,"Grupa 3",2),
        Grupa(4,"Grupa 4",2),
        Grupa(9,"Grupa 8",5))
}