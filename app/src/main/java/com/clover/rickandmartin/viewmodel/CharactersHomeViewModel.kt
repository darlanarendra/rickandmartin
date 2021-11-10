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
import com.clover.rickandmartin.adapter.CharactersRecyclerViewAdapter
import com.clover.rickandmartin.models.CharactersResponse
import com.clover.rickandmartin.models.Result
import com.clover.rickandmartin.nychome.CharactersDataUseCase
import com.clover.rickandmartin.platform.LiveDataWrapper
import kotlinx.coroutines.*
import org.koin.core.KoinComponent

/**CharactersHomeViewModel.
 * Handles connecting with corresponding Use Case.
 * Getting back data to View.
 * @author Narendra Darla
 * @author www.test.com
 * @version 1.0
 * @since 1.0
 */
class CharactersHomeViewModel(
    mainDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    val charactersDataUseCase: CharactersDataUseCase
) : ViewModel(), KoinComponent, CharactersRecyclerViewAdapter.OnItemClickListener<Result> {

    var charactersResponse = MutableLiveData<LiveDataWrapper<CharactersResponse>>()
    var mSelectedId = MutableLiveData<Result>()
    var mError = MutableLiveData<Boolean>()
    var mSelectedCharacter = false
    val mLoadingLiveData = MediatorLiveData<Boolean>()
    private val job = SupervisorJob()
    val mUiScope = CoroutineScope(mainDispatcher + job)
    val mIoScope = CoroutineScope(ioDispatcher + job)

    init {
        getCharacters()
    }

    //can be called from View explicitly if required
    //Keep default scope
    fun getCharacters() {
        if (charactersResponse.value == null) {
            mUiScope.launch {
                charactersResponse.value = LiveDataWrapper.loading()
                setLoadingVisibility(true)
                try {
                    val data = mIoScope.async {
                        return@async charactersDataUseCase.getCharacters()
                    }.await()
                    data?.let {
                        charactersResponse.value = LiveDataWrapper.success(it)
                    }
                    setLoadingVisibility(false)
                } catch (e: Exception) {
                    e.printStackTrace()
                    setLoadingVisibility(false)
                    charactersResponse.value = LiveDataWrapper.error(e)
                    mError.postValue(true)
                }
            }
        }
    }


    private fun process(id: Result) {
        mSelectedCharacter = true
        mSelectedId.postValue(id)
    }

    private fun setLoadingVisibility(visible: Boolean) {
        mLoadingLiveData.postValue(visible)
    }

    override fun onCleared() {
        super.onCleared()
        this.job.cancel()
    }

    override fun onItemClick(item: Result) {
        process(item)
    }

}