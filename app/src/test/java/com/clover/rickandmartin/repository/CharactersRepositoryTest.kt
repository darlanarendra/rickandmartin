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
package com.clover.rickandmartin.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.clover.rickandmartin.base.BaseUTTest
import com.clover.rickandmartin.di.configureTestAppComponent
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import org.koin.test.inject
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class CharactersRepositoryTest : BaseUTTest(){

    //Target
    private lateinit var mRepo: CharactersHomeRepository
    //Inject api service created with koin
    val mAPIService : CharactersHomeRepository by inject()
    //Inject Mockwebserver created with koin
    val mockWebServer : MockWebServer by inject()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

     val MResultCount = 20

    @Before
    fun start(){
        super.setUp()
        startKoin{ modules(configureTestAppComponent(getMockWebServerUrl()))}
    }

    @Test
    fun test_character_repo_retrieves_expected_data() =  runBlocking<Unit>{
        mockNetworkResponseWithFileContent("characters_response.json", HttpURLConnection.HTTP_OK)
        mRepo = CharactersHomeRepository()
        val dataReceived = mRepo.getCharacters()
        assertNotNull(dataReceived)
        assertEquals(dataReceived?.results?.size, MResultCount)
    }

    @Test
    fun test_character_repo_retrieves_expected_character_details_data() =  runBlocking<Unit>{
        mockNetworkResponseWithFileContent("character_details.json", HttpURLConnection.HTTP_OK)
        mRepo = CharactersHomeRepository()
        val dataReceived = mRepo.getCharacterDetails("4")
        assertNotNull(dataReceived)
        assertEquals(dataReceived?.name,"Worldender's lair")
    }
}