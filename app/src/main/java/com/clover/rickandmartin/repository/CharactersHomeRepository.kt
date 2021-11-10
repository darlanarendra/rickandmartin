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


import com.clover.rickandmartin.models.CharactersResponse
import com.clover.rickandmartin.models.detail.CharacterDetailsResponse
import com.clover.rickandmartin.nychome.CharactersAPIService
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.awaitResponse


/** Repository for RickandMartin.
 * Requests data from either Network or DB.
 * @author Narendra Darla
 * @author www.test.com
 * @version 1.0
 * @since 1.0
 */
class CharactersHomeRepository : KoinComponent {
    val TAG = CharactersHomeRepository::class.java.simpleName
    val charactersAPIService: CharactersAPIService by inject()
    suspend fun getCharacters(): CharactersResponse? {
        val dataReceived = charactersAPIService.getCharacters().awaitResponse().body()
        return dataReceived
    }


    suspend fun getCharacterDetails(id:String): CharacterDetailsResponse? {
        val dataReceived = charactersAPIService.getCharacterDetails(id).awaitResponse().body()
        return dataReceived
    }

}
