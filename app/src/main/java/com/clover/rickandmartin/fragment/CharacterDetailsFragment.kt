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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.clover.rickandmartin.R
import com.clover.rickandmartin.databinding.FragmentCharacterDetailsBinding.inflate
import com.clover.rickandmartin.nychome.CharactersDataUseCase
import com.clover.rickandmartin.platform.BaseFragment
import com.clover.rickandmartin.platform.BaseViewModelFactory
import com.clover.rickandmartin.viewmodel.CharacterDetailsViewModel
import kotlinx.android.synthetic.main.fragment_character_details.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext


/** Show CharacterDetails
 * Provide Use case dependency.
 * @author Narendra Darla
 * @author www.test.com
 * @version 1.0
 * @since 1.0
 */
class CharacterDetailsFragment : BaseFragment(), CoroutineScope {

    val TAG = CharacterDetailsFragment::class.java.simpleName
    val charactersDataUseCase: CharactersDataUseCase by inject()
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val mBaseViewModelFactory: BaseViewModelFactory =
        BaseViewModelFactory(
            Dispatchers.Main, Dispatchers.IO
        ).apply {
            charactersDataUseCase = this@CharacterDetailsFragment.charactersDataUseCase
        }

    private val mViewModel: CharacterDetailsViewModel by lazy {
        ViewModelProvider(this, mBaseViewModelFactory)
            .get(CharacterDetailsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = mViewModel
        val character = CharacterDetailsFragmentArgs.fromBundle(requireArguments())
        val characterId = character.selectedId
        mViewModel.getCharacterDetails(characterId)
        Glide.with(this).load(character.selectedId.image).into(binding.image);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.mViewModel.mLoadingLiveData.observe(viewLifecycleOwner, this.loadingObserver)
    }


    private val loadingObserver = Observer<Boolean> { visible ->
        // Show hide progress bar
        if (visible) {
            progress_circular.visibility = View.VISIBLE
        } else {
            progress_circular.visibility = View.INVISIBLE
        }
    }

    override fun getLayoutId() = R.layout.fragment_character_details
}
