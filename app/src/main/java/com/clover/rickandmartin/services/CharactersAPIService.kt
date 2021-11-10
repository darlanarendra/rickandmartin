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
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/** CharactersAPIService Retrofit API.
 * @author Narendra Darla
 * @author www.test.com
 * @version 1.0
 * @since 1.0
 */
interface CharactersAPIService {
    @GET("api/character")
    fun getCharacters(): Call<CharactersResponse>

    @GET("api/location/{id}")
    fun getCharacterDetails(@Path("id") id: String): Call<CharacterDetailsResponse>
}