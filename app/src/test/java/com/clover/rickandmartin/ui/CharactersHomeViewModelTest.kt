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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.clover.rickandmartin.base.BaseUTTest
import com.clover.rickandmartin.di.configureTestAppComponent
import com.clover.rickandmartin.models.Result
import com.clover.rickandmartin.nychome.CharactersDataUseCase
import com.clover.rickandmartin.viewmodel.CharactersHomeViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import java.lang.reflect.Type

@RunWith(JUnit4::class)
class CharactersHomeViewModelTest: BaseUTTest(){


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var mHomeViewModel: CharactersHomeViewModel

    val mDispatcher = Dispatchers.Unconfined

    @MockK
    lateinit var charactersDataUseCase: CharactersDataUseCase


    @Before
    fun start(){
        super.setUp()
        //Used for initiation of Mockk
        MockKAnnotations.init(this)
        //Start Koin with required dependencies
        startKoin{ modules(configureTestAppComponent(getMockWebServerUrl()))}
    }

    @Test
    fun test_view_model_data_populates_expected_value(){

        mHomeViewModel = CharactersHomeViewModel(mDispatcher,mDispatcher, charactersDataUseCase)
        val sampleResponse = getJson("characters_response.json")
        val listType: Type = object : TypeToken<ArrayList<Result>>() {}.type
        coEvery { charactersDataUseCase.getCharacters() } returns Gson().fromJson(sampleResponse, listType)
        mHomeViewModel.charactersResponse.observeForever {  }
        mHomeViewModel.getCharacters()
        assert(mHomeViewModel.charactersResponse.value != null)
    }

}