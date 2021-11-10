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

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.clover.rickandmartin.contracts.AlertCallBack
import com.clover.rickandmartin.contracts.AppLogger
import com.google.android.material.dialog.MaterialAlertDialogBuilder



/** Base Fragment for all Fragments.
 * Handles Alert, Toast, Logger etc
 * @author Narendra Darla
 * @author www.test.com
 * @version 1.0
 * @since 1.0
 */
abstract class BaseFragment: Fragment(), AppLogger, AlertCallBack {

    // OVERRIDE ---
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    // ABSTRACT ---
    abstract fun getLayoutId(): Int

    private var mCallBackAlertDialog: AlertDialog? = null

    fun showAlert(message :Int, positiveBtnText: Int, negativeBtnText:Int
    ) {
        activity?.let {
            MaterialAlertDialogBuilder(it)
                .setMessage(message)
                .setPositiveButton(positiveBtnText, null)
                .setNegativeButton(negativeBtnText, null)
                .setCancelable(true)
                .show()
        }
    }

    fun showToast(msg: String) {
        requireActivity().runOnUiThread {
            val toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }



    override fun negativeAlertCallBack() {
        mCallBackAlertDialog!!.dismiss()
    }

    override fun positiveAlertCallBack() {
        mCallBackAlertDialog!!.dismiss()
    }

    override fun logD(tag: String, message: String) {
        Log.d(tag,message)
    }

    override fun logE(tag: String, message: String) {
        Log.e(tag,message)
    }

    override fun logV(tag: String, message: String) {
        Log.v(tag,message)
    }

    override fun logI(tag: String, message: String) {
        Log.i(tag,message)
    }
}