package com.clover.rickandmartin.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.clover.rickandmartin.base.BaseUTTest
import com.clover.rickandmartin.di.configureTestAppComponent
import com.clover.rickandmartin.nychome.CharactersDataUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class CharactersDataUseCaseTest : BaseUTTest(){

    //Target
    private lateinit var charactersDataUseCase: CharactersDataUseCase


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    val count = 20
    @Before
    fun start(){
        super.setUp()
        //Start Koin with required dependencies
        startKoin{ modules(configureTestAppComponent(getMockWebServerUrl()))}
    }

    @Test
    fun test_character_data_usecase_returns_expected_characters_value()= runBlocking{
        mockNetworkResponseWithFileContent("characters_response.json", HttpURLConnection.HTTP_OK)
        charactersDataUseCase = CharactersDataUseCase()
        val dataReceived = charactersDataUseCase.getCharacters()?.results
        assertNotNull(dataReceived)
        assertEquals(dataReceived?.size, count)
    }
}