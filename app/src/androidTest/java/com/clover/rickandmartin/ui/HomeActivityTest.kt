/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.rickandmartin.ui

import android.os.SystemClock
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.clover.rickandmartin.R
import com.clover.rickandmartin.base.BaseUITest
import com.clover.rickandmartin.di.generateTestAppComponent
import com.clover.rickandmartin.helpers.recyclerItemAtPosition
import com.clover.rickmartin.activity.HomeActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import java.net.HttpURLConnection

@RunWith(AndroidJUnit4::class)
class HomeActivityTest : BaseUITest() {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(HomeActivity::class.java, true, false)

    val mNameTestOne = "Rick Sanchez"


    @Before
    fun start() {
        super.setUp()
        loadKoinModules(generateTestAppComponent(getMockWebServerUrl()).toMutableList())
    }

    @Test
    fun test_recyclerview_elements_for_expected_response() {
        mActivityTestRule.launchActivity(null)

        mockNetworkResponseWithFileContent("success_resp_list.json", HttpURLConnection.HTTP_OK)

        //Wait for MockWebServer to get back with response
        SystemClock.sleep(5000)

        //Check if item at 0th position is having 0th element in json
        onView(withId(R.id.landingListRecyclerView))
            .check(
                matches(
                    recyclerItemAtPosition(
                        0,
                        ViewMatchers.hasDescendant(withText(mNameTestOne))
                    )
                )
            )

        onView(withId(R.id.landingListRecyclerView))
            .check(
                matches(
                    recyclerItemAtPosition(
                        1,
                        ViewMatchers.hasDescendant(withText("Morty Smith"))
                    )
                )
            )


        onView(withId(R.id.landingListRecyclerView))
            .check(
                matches(
                    recyclerItemAtPosition(
                        2,
                        ViewMatchers.hasDescendant(withText("Summer Smith"))
                    )
                )
            )
        onView(withId(R.id.landingListRecyclerView))
            .check(
                matches(
                    recyclerItemAtPosition(
                        2,
                        ViewMatchers.hasDescendant(withText("Beth Smith"))
                    )
                )
            )

    }
}