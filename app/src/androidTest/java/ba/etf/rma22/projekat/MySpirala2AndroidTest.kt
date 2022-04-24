package ba.etf.rma22.projekat

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ba.etf.rma22.projekat.UtilTestClass.Companion.hasItemCount
import ba.etf.rma22.projekat.UtilTestClass.Companion.itemTest
import ba.etf.rma22.projekat.data.models.SveAnkete
import ba.etf.rma22.projekat.data.repositories.AnketaRepository
import ba.etf.rma22.projekat.data.repositories.SveAnketeRepository
import com.google.android.apps.common.testing.accessibility.framework.replacements.TextUtils
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.Description
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
            .check(matches(hasValueEqualTo("Uspješno ste upisani u grupu Grupa 10 istraživanja Istraživanje broj 3!")));

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

    fun hasValueEqualTo(content: String): TypeSafeMatcher<View?> {
        return object : TypeSafeMatcher<View?>() {
            override fun describeTo(description: Description) {
                description.appendText("Has EditText/TextView the value:  $content")
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