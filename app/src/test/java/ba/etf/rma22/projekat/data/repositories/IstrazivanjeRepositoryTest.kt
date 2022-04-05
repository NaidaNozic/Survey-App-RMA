package ba.etf.rma22.projekat.data.repositories

import ba.etf.rma22.projekat.data.istrazivanja
import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.repositories.IstrazivanjeRepository
import ba.etf.rma22.projekat.data.upisanaIstrazivanja
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class IstrazivanjeRepositoryTest {
    private lateinit var svaIstrazivanja:List<Istrazivanje>
    private lateinit var svaUpisana:List<Istrazivanje>

    @Before
    fun setUp() {
        svaIstrazivanja=istrazivanja()
        svaUpisana=upisanaIstrazivanja()
    }

    @Test
    fun getIstrazivanjeByGodina() {
        var lista=IstrazivanjeRepository.getIstrazivanjeByGodina(2)
        assertTrue(lista.size==4)
        var ocekivana= listOf(Istrazivanje("Istraživanje broj 2",2),
            Istrazivanje("Istraživanje broj 4",2),Istrazivanje("Istraživanje broj 1",2))
        assertTrue(lista.containsAll(ocekivana))
    }

    @Test
    fun getAll() {
        var lista=IstrazivanjeRepository.getAll()
        assertTrue(lista.size==svaIstrazivanja.size)
        assertTrue(svaIstrazivanja.containsAll(lista))
    }

    @Test
    fun getUpisani() {
        var lista=IstrazivanjeRepository.getUpisani()
        assertTrue(lista.size==svaUpisana.size)
        assertTrue(svaUpisana.containsAll(lista))
    }
}

