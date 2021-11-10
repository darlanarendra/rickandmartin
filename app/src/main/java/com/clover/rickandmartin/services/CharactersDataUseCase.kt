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
package com.clover.rickandmartin.nychome

import com.clover.rickandmartin.models.CharactersResponse
import com.clover.rickandmartin.models.detail.CharacterDetailsResponse
import com.clover.rickandmartin.repository.CharactersHomeRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

/* * Use Case class for handling CharactersDataUseCase .
 * Requests data from Repo.
 * Process received data into required model and reverts back to ViewModel.
 * @author Narendra Darla
 * @author www.test.com
 * @version 1.0
 * @since 1.0
 */
class CharactersDataUseCase : KoinComponent {

    val charactersHomeRepository : CharactersHomeRepository by inject()

    suspend fun getCharacters(): CharactersResponse? {
        return charactersHomeRepository.getCharacters()
    }

    suspend fun getCharacterDetails(id:String): CharacterDetailsResponse? {
        return charactersHomeRepository.getCharacterDetails(id)
    }

}