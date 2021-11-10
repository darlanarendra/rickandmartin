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
package com.clover.rickandmartin.platform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.clover.rickandmartin.nychome.CharactersDataUseCase
import com.clover.rickandmartin.viewmodel.CharactersHomeViewModel
import com.clover.rickandmartin.viewmodel.CharacterDetailsViewModel
import kotlinx.coroutines.CoroutineDispatcher

/** Base VM Factory for creation of different VM's
 * @author Narendra Darla
 * @author www.test.com
 * @version 1.0
 * @since 1.0
 */
class BaseViewModelFactory constructor(
    private val mainDispather: CoroutineDispatcher
    ,private val ioDispatcher: CoroutineDispatcher
) :
    ViewModelProvider.Factory {
    lateinit var charactersDataUseCase: CharactersDataUseCase
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CharactersHomeViewModel::class.java)) {
            CharactersHomeViewModel(mainDispather, ioDispatcher,charactersDataUseCase) as T
        }else if (modelClass.isAssignableFrom(CharacterDetailsViewModel::class.java)) {
            CharacterDetailsViewModel(mainDispather, ioDispatcher,charactersDataUseCase) as T
        } else {
            throw IllegalArgumentException("ViewModel Not configured")
        }
    }
}