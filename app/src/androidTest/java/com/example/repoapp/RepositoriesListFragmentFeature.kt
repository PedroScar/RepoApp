package com.example.repoapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.repoapp.model.api.idlingResource
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsNot.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoriesListFragmentFeature : BaseUITest() {

    @Test
    fun displayRepositoriesList() {
        onView(isRoot()).perform(waitDisplayed(R.id.iv_image, 5000))
            .check(matches(isDisplayed()))
    }

    @Test
    fun displayRecyclerViewItem() {
        onView(isRoot()).perform(waitDisplayed(R.id.tv_repo_name, 5000))
            .check(matches(isDisplayed()))
            .check(matches(not(withText(""))))

        onView(isRoot()).perform(waitDisplayed(R.id.tv_author, 5000))
            .check(matches(isDisplayed()))
            .check(matches(not(withText(""))))

        onView(isRoot()).perform(waitDisplayed(R.id.it_fork, 5000))
            .check(matches(isDisplayed()))

        onView(isRoot()).perform(waitDisplayed(R.id.it_star, 5000))
            .check(matches(isDisplayed()))
    }

    @Test
    fun hidesLoader() {
        onView(withId(R.id.l_loader)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun displaysLoaderWhileFetchingTheRepositories() {
        IdlingRegistry.getInstance().unregister(idlingResource)

        onView(withId(R.id.l_loader)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
}
