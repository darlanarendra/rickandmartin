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
package com.clover.rickandmartin.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.clover.rickandmartin.models.Result
import com.clover.rickandmartin.models.detail.CharacterDetailsResponse
import com.clover.rickandmartin.nychome.CharactersDataUseCase
import com.clover.rickandmartin.platform.LiveDataWrapper
import kotlinx.coroutines.*
import org.koin.core.KoinComponent

/** CharacterDetails View Model.
 * @author Narendra Darla
 * @author www.test.com
 * @version 1.0
 * @since 1.0
 */

class CharacterDetailsViewModel(
    mainDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    val charactersDataUseCase: CharactersDataUseCase
) : ViewModel(), KoinComponent {

    val TAG = CharacterDetailsViewModel::class.java.simpleName
    var name = MutableLiveData<String>()
    var type = MutableLiveData<String>()
    var dimension = MutableLiveData<String>()
    var residents = MutableLiveData<String>()
    private val job = SupervisorJob()
    val mUiScope = CoroutineScope(mainDispatcher + job)
    val mIoScope = CoroutineScope(ioDispatcher + job)
    var mCharacterDetailsResponse = MutableLiveData<LiveDataWrapper<CharacterDetailsResponse>>()
    var mError = MutableLiveData<Boolean>()
    val mLoadingLiveData = MediatorLiveData<Boolean>()

    fun getCharacterDetails(result:Result){
        if (mCharacterDetailsResponse.value == null) {
        mUiScope.launch {
                mCharacterDetailsResponse.value = LiveDataWrapper.loading()
                setLoadingVisibility(true)
                try {
                    val data = mIoScope.async {
                        return@async charactersDataUseCase.getCharacterDetails(result.id.toString())
                    }.await()
                    data?.let {
                        processResponse(it)
                    }
                    setLoadingVisibility(false)
                } catch (e: Exception) {
                    e.printStackTrace()
                    setLoadingVisibility(false)
                    mCharacterDetailsResponse.value = LiveDataWrapper.error(e)
                    mError.postValue(true)
                }
            }
        }
   }

    private fun setLoadingVisibility(visible: Boolean) {
        mLoadingLiveData.postValue(visible)
    }

    private fun processResponse(it: CharacterDetailsResponse) {
        name.value = (LiveDataWrapper.success(it).response?.name)
        type.value = (LiveDataWrapper.success(it).response?.type.toString())
        residents.value = (LiveDataWrapper.success(it).response?.residents?.size.toString())
        dimension.value = (LiveDataWrapper.success(it).response?.dimension.toString())
    }
}
