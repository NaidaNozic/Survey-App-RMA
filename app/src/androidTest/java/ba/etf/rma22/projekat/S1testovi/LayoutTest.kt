package ba.etf.rma22.projekat.S1testovi

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ba.etf.rma22.projekat.MainActivity
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LayoutTest {
    @get:Rule
    val mainLayout = ActivityScenarioRule<MainActivity>(MainActivity::class.java)
/*
    @Test
    fun relativeLayoutPositionsPocetna(){
        Espresso.onView(withId(R.id.filterAnketa)).check(
            ViewAssertions.matches(
                isCompletelyDisplayed()
            )
        )
        Espresso.onView(withId(R.id.listaAnketa)).check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.upisDugme)).check(ViewAssertions.matches(isCompletelyDisplayed()))

        Espresso.onView(withId(R.id.filterAnketa)).check(isCompletelyAbove(withId(R.id.listaAnketa)));
        Espresso.onView(withId(R.id.filterAnketa)).check(isCompletelyAbove(withId(R.id.upisDugme)));

        Espresso.onView(withId(R.id.upisDugme)).check(isPartiallyRightOf(withId(R.id.listaAnketa)));
        Espresso.onView(withId(R.id.upisDugme)).check(isPartiallyRightOf(withId(R.id.filterAnketa)));
    }

    @Test
    fun relativeLayoutPositionsUpis(){
        Espresso.onView(withId(R.id.upisDugme)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.odabirGodina)).check(
            ViewAssertions.matches(
                isCompletelyDisplayed()
            )
        )
        Espresso.onView(withId(R.id.odabirIstrazivanja)).check(
            ViewAssertions.matches(
                isCompletelyDisplayed()
            )
        )
        Espresso.onView(withId(R.id.odabirGrupa)).check(ViewAssertions.matches(isCompletelyDisplayed()))
        Espresso.onView(withId(R.id.dodajIstrazivanjeDugme)).check(
            ViewAssertions.matches(
                isCompletelyDisplayed()
            )
        )

        Espresso.onView(withId(R.id.odabirGodina)).check(isCompletelyAbove(withId(R.id.odabirIstrazivanja)))
        Espresso.onView(withId(R.id.odabirIstrazivanja)).check(isCompletelyAbove(withId(R.id.odabirGrupa)))
        Espresso.onView(withId(R.id.odabirGrupa)).check(isCompletelyAbove(withId(R.id.dodajIstrazivanjeDugme)))

    }*/
}