package ba.etf.rma22.projekat

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ba.etf.rma22.projekat.UtilTestClass.Companion.hasItemCount
import ba.etf.rma22.projekat.UtilTestClass.Companion.itemTest
import ba.etf.rma22.projekat.data.models.SveAnkete
import ba.etf.rma22.projekat.data.repositories.AnketaRepository
import ba.etf.rma22.projekat.data.repositories.SveAnketeRepository
import com.google.android.apps.common.testing.accessibility.framework.replacements.TextUtils
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Description
import org.hamcrest.Matchers.hasToString
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.regex.Matcher
import org.hamcrest.CoreMatchers.`is` as Is


@RunWith(AndroidJUnit4::class)
class MySpirala2AndroidTest {

    @get:Rule
    val intentsTestRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    @Test
    fun Test1(){
        onView(withId(R.id.pager)).perform(ViewPager2Actions.scrollToPosition(1))
        onView(withId(R.id.odabirGodina)).perform(click())
        onData(allOf(Is(instanceOf(String::class.java)), Is("2"))).perform(click())
        onView(withId(R.id.odabirIstrazivanja)).perform(click())
        onData(allOf(Is(instanceOf(String::class.java)), Is("Istraživanje broj 3"))).perform(click())
        onView(withId(R.id.odabirGrupa)).perform(click())
        onData(allOf(Is(instanceOf(String::class.java)), Is("Grupa 10"))).perform(click())

        onView(withId(R.id.dodajIstrazivanjeDugme)).perform(click())
        //test fragmentPoruka
        onView(withId(R.id.tvPoruka))
            .check(matches(hasValueEqualTo("Uspješno ste upisani u grupu Grupa 10 istraživanja Istraživanje broj 3!")))

        onView(withId(R.id.pager)).perform(ViewPager2Actions.scrollToPosition(0))
        var ankete = AnketaRepository.getMyAnkete().toMutableList()
        SveAnketeRepository.dajAnketu("Anketa 2","Istraživanje broj 3","Grupa 10")?.let { ankete.add(it) }
        for (anketa in ankete) {
            itemTest(R.id.listaAnketa, anketa)
        }

        ankete = AnketaRepository.getNotTaken().toMutableList()
        for (anketa in ankete) {
            itemTest(R.id.listaAnketa, anketa)
        }

        ankete = AnketaRepository.getFuture().toMutableList()
        SveAnketeRepository.dajAnketu("Anketa 2","Istraživanje broj 3","Grupa 10")?.let { ankete.add(it) }
        for (anketa in ankete) {
            itemTest(R.id.listaAnketa, anketa)
        }

    }
    @Test
    fun Test2(){
        onView(withId(R.id.listaAnketa)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                CoreMatchers.allOf(
                    ViewMatchers.hasDescendant(ViewMatchers.withText("Anketa 14")),
                    ViewMatchers.hasDescendant(ViewMatchers.withText("Istraživanje broj 3"))
                ), click()))
        onData(hasToString(startsWith("Nil")))
            .inAdapterView(withId(R.id.odgovoriLista)).atPosition(0)
            .perform(click())
        onView(withId(R.id.dugmeZaustavi)).perform(click())
        var anketa=AnketaRepository.getMyAnkete().find { a->a.naziv=="Anketa 14" && a.nazivIstrazivanja=="Istraživanje broj 3" }
        assert(anketa?.progres.toString()=="0.4")

        onView(withId(R.id.listaAnketa)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                CoreMatchers.allOf(
                    ViewMatchers.hasDescendant(ViewMatchers.withText("Anketa 14")),
                    ViewMatchers.hasDescendant(ViewMatchers.withText("Istraživanje broj 3"))
                ), click()))
        onView(withId(R.id.pager)).perform(ViewPager2Actions.scrollToPosition(1))
        onData(hasToString(startsWith("366")))
            .inAdapterView(withId(R.id.odgovoriLista)).atPosition(0)
            .perform(click())

        onView(withId(R.id.pager)).perform(ViewPager2Actions.scrollToPosition(2))
        onView(withId(R.id.progresTekst)).check(matches(hasValueEqualTo("100%")))
        onView(withId(R.id.dugmePredaj)).perform(click())

        //provjera da li je progres ankete zaista promijenjen
        anketa=AnketaRepository.getMyAnkete().find { a->a.naziv=="Anketa 14" && a.nazivIstrazivanja=="Istraživanje broj 3" }
        assert(anketa?.progres.toString()=="1.0")

        //test fragmentPoruka
       onView(withId(R.id.tvPoruka))
            .check(matches(hasValueEqualTo("Završili ste anketu Anketa 14 u okviru istraživanja Istraživanje broj 3")))
        onView(withId(R.id.pager)).perform(ViewPager2Actions.scrollToPosition(0))
    }

    fun hasValueEqualTo(content: String): TypeSafeMatcher<View?> {
        return object : TypeSafeMatcher<View?>() {
            override fun describeTo(description: Description) {
                description.appendText("$content")
            }

            override fun matchesSafely(view: View?): Boolean {
                if (view !is TextView && view !is EditText) {
                    return false
                }
                if (view != null) {
                    val text: String
                    text = if (view is TextView) {
                        view.text.toString()
                    } else {
                        (view as EditText).text.toString()
                    }
                    return text.equals(content, ignoreCase = true)
                }
                return false
            }
        }
    }
}