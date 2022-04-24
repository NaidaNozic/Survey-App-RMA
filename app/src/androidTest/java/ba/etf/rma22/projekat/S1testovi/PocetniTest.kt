package ba.etf.rma22.projekat.S1testovi

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ba.etf.rma22.projekat.MainActivity
import org.junit.Rule
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class PocetniTest {
    @get:Rule
    val intentsTestRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)
/*
    @Test
    fun postojiSveNaPocetnoj() { //prolazi

        onView(withId(R.id.filterAnketa)).check(matches(isDisplayed()))
        onView(withId(R.id.listaAnketa)).check(matches(isDisplayed()))
        onView(withId(R.id.upisDugme)).check(matches(isDisplayed()))

        var listaOdabira = listOf<String>(
            "Sve moje ankete",
            "Sve ankete",
            "Urađene ankete",
            "Buduće ankete",
            "Prošle ankete"
        )

        for (odabir in listaOdabira) {
            onView(withId(R.id.filterAnketa)).perform(click())
            onData(allOf(Is(instanceOf(String::class.java)), Is(odabir))).perform(click())
        }

    }

    @Test
    fun popuniAnketeGetDone() { //prolazi

        onView(withId(R.id.filterAnketa)).perform(click())
        onData(allOf(Is(instanceOf(String::class.java)), Is("Sve moje ankete"))).perform(click())
        onView(withId(R.id.filterAnketa)).check(matches(withSpinnerText("Sve moje ankete")))
        val ankete = AnketaRepository.getMyAnkete()
        onView(withId(R.id.listaAnketa)).check(hasItemCount(ankete.size))
        for (anketa in ankete) {
            itemTest(R.id.listaAnketa, anketa)
        }

    }

    @Test
    fun godineTest() { //prolazi
        onView(withId(R.id.upisDugme)).perform(click())
        var listaOdabira = listOf<String>("1", "2", "3", "4", "5")
        for (odabir in listaOdabira) {
            onView(withId(R.id.odabirGodina)).perform(click())
            onData(allOf(Is(instanceOf(String::class.java)), Is(odabir))).perform(click())
        }
    }

    @Test
    fun filtriranjeTest(){ //prolazi
        var listaOdabira = listOf<String>(
            "Sve moje ankete",
            "Sve ankete",
            "Urađene ankete",
            "Buduće ankete",
            "Prošle ankete"
        )
        var kolikoAnketa = 0
        for (odabir in listaOdabira) {
            onView(withId(R.id.filterAnketa)).perform(click())
            onData(allOf(Is(instanceOf(String::class.java)), Is(odabir))).perform(click())
            var ankete = emptyList<Anketa>()
            when(odabir){
                "Sve moje ankete" -> ankete=AnketaRepository.getMyAnkete()
                "Sve ankete" -> ankete=AnketaRepository.getAll()
                "Urađene ankete" -> ankete=AnketaRepository.getDone()
                "Buduće ankete" -> ankete=AnketaRepository.getFuture()
                "Prošle ankete" -> ankete=AnketaRepository.getNotTaken()
            }
            kolikoAnketa+=ankete.size
            onView(withId(R.id.listaAnketa)).check(hasItemCount(ankete.size))
            var posjeceni:MutableList<Int> = mutableListOf()
            for (anketa in ankete) {
                itemTestNotVisited(R.id.listaAnketa, anketa,posjeceni)
            }
        }
        val ukupno = AnketaRepository.getAll().size
        kolikoAnketa-=ukupno
        assertThat(kolikoAnketa, allOf(Is(greaterThan(0))))
    }*/
}