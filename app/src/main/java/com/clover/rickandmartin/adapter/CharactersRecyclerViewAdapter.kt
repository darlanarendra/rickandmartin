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
package com.clover.rickandmartin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.clover.rickandmartin.R
import com.clover.rickandmartin.models.Result
import com.clover.rickandmartin.viewmodel.CharactersHomeViewModel

/** Adapter for RickandMartin recyclerview.
 * @author Narendra Darla
 * @author www.test.com
 * @version 1.0
 * @since 1.0
 */
class CharactersRecyclerViewAdapter(
    val context: Context,
    viewModel: CharactersHomeViewModel, list: ArrayList<Result>
) : RecyclerView.Adapter<CharactersRecyclerViewAdapter.CharactersViewHolder>() {

    val TAG = CharactersRecyclerViewAdapter::class.java.simpleName
    var mItemList = list
    private val mOnItemClickListener: OnItemClickListener<Result>? = viewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        return CharactersViewHolder(
            LayoutInflater.from(context).inflate(R.layout.landing_list_view_item, parent, false)
        )
    }

    fun updateListItems(updatedList: ArrayList<Result>) {
        mItemList.clear()
        mItemList = updatedList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val model: Result = mItemList[position]
        holder.name.text = model.name
        holder.species.text = "Species : ${model.species}"
        holder.clicl_btn.setOnClickListener {
            showDetails(position)
        }
        holder.status.text = "Status : ${model.status}"
    }

    private fun showDetails(position: Int) {
        mOnItemClickListener?.onItemClick(mItemList.get(position))
    }

    class CharactersViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val name: AppCompatTextView = item.findViewById(R.id.name)
        val species: AppCompatTextView = item.findViewById(R.id.species)
        val clicl_btn: AppCompatImageView = item.findViewById(R.id.clicl_btn)
        val status: AppCompatTextView = item.findViewById(R.id.url_tv)
    }

    interface OnItemClickListener<Result> {
        fun onItemClick(item: com.clover.rickandmartin.models.Result)
    }
}