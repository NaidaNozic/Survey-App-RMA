package ba.etf.rma22.projekat.data.repositories

import ba.etf.rma22.projekat.data.models.Pitanje
import ba.etf.rma22.projekat.data.pitanja
import ba.etf.rma22.projekat.data.pitanjaPoAnketi

class PitanjeAnketaRepository {
    fun getPitanja(nazivAnkete: String, nazivIstrazivanja: String): List<Pitanje>{
        val rez = mutableListOf<Pitanje>()
        val pitanjeAnketa= pitanjaPoAnketi().toMutableList()
        pitanjeAnketa.removeAll { p->p.anketa!=nazivAnkete }
        val pitanja= pitanja()
        for(p in pitanjeAnketa){
            pitanja.find { p1->p1.naziv==p.naziv }?.let { rez.add(it) }
        }
        return rez
    }
}