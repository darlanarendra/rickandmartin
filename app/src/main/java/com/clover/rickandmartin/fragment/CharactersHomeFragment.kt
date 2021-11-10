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
package com.clover.rickandmartin.fragment


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.clover.rickandmartin.R
import com.clover.rickandmartin.adapter.CharactersRecyclerViewAdapter
import com.clover.rickandmartin.models.CharactersResponse
import com.clover.rickandmartin.models.Result
import com.clover.rickandmartin.nychome.CharactersDataUseCase
import com.clover.rickandmartin.platform.BaseFragment
import com.clover.rickandmartin.platform.BaseViewModelFactory
import com.clover.rickandmartin.platform.LiveDataWrapper
import com.clover.rickandmartin.viewmodel.CharactersHomeViewModel
import kotlinx.android.synthetic.main.fragment_characters_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

/** RickAndMartin HomeFragment.
 * Provide Use case dependency.
 * @author Narendra Darla
 * @author www.test.com
 * @version 1.0
 * @since 1.0
 */
class CharactersHomeFragment : BaseFragment(), CoroutineScope {
    private val TAG = CharactersHomeFragment::class.java.simpleName
    val charactersDataUseCase: CharactersDataUseCase by inject()
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    lateinit var mRecyclerViewAdapter: CharactersRecyclerViewAdapter

    private val mBaseViewModelFactory: BaseViewModelFactory =
        BaseViewModelFactory(
            Dispatchers.Main, Dispatchers.IO
        ).apply {
            charactersDataUseCase = this@CharactersHomeFragment.charactersDataUseCase
        }

    private val mViewModel: CharactersHomeViewModel by lazy {
        ViewModelProvider(this, mBaseViewModelFactory)
            .get(CharactersHomeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.mViewModel.charactersResponse.observe(viewLifecycleOwner, this.mDataObserver)
        this.mViewModel.mLoadingLiveData.observe(viewLifecycleOwner, this.loadingObserver)
        this.mViewModel.mError.observe(viewLifecycleOwner,  { isError->
            if (isError)
                showAlert(R.string.error,R.string.ok, R.string.cancel)
            this.mViewModel.mError.postValue(false)
        })
        this.mViewModel.mSelectedId.observe(viewLifecycleOwner,  { id ->
            if (null != id && mViewModel.mSelectedCharacter) {
                val action =
                    CharactersHomeFragmentDirections.actionHomeFragmentToCharacterDetailsFragment(id)
                findNavController().navigate(action)
                mViewModel.mSelectedCharacter = false
            }
        })

        mRecyclerViewAdapter =
            CharactersRecyclerViewAdapter(requireActivity(), mViewModel, arrayListOf())
        landingListRecyclerView.adapter = mRecyclerViewAdapter
        landingListRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }



    private val mDataObserver = Observer<LiveDataWrapper<CharactersResponse>> { result ->
        when (result?.responseStatus) {
            LiveDataWrapper.RESPONSESTATUS.LOADING -> {
                // Loading data
            }
            LiveDataWrapper.RESPONSESTATUS.ERROR -> {
                // Error for http request
                logD(TAG, "LiveDataResult.Status.ERROR = ${result.response}")
                error_holder.visibility = View.VISIBLE
                showToast("Error in getting data")
            }
            LiveDataWrapper.RESPONSESTATUS.SUCCESS -> {
                // Data from API
                logD(TAG, "LiveDataResult.Status.SUCCESS = ${result.response}")
                val mainItemReceived = result.response as CharactersResponse
                processData(mainItemReceived)
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_characters_activity

    /**
     * Handle success data
     */
    private fun processData(listItems: CharactersResponse) {
        Handler(Looper.getMainLooper()).post {
            mRecyclerViewAdapter.updateListItems(listItems.results as ArrayList<Result>)
        }
    }

    /**
     * Hide / show loader
     */
    private val loadingObserver = Observer<Boolean> { visible ->
        // Show hide progress bar
        if (visible) {
            progress_circular.visibility = View.VISIBLE
        } else {
            progress_circular.visibility = View.INVISIBLE
        }
    }
}
