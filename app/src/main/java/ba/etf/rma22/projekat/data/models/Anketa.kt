package ba.etf.rma22.projekat.data.models

import java.io.Serializable
import java.util.*

data class Anketa (
    val naziv: String,
    val nazivIstrazivanja: String,
    val datumPocetak: Date,
    var datumKraj: Date,
    var datumRada: Date?,
    val trajanje: Int,
    val nazivGrupe: String,
    var progres: Float,
    var pitanja:MutableMap<String,MutableList<String>> = mutableMapOf()//kljuc je tekst pitanja
):Serializable