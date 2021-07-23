package com.tn07.survey.features.common

import android.widget.Toast
import com.tn07.survey.features.base.BaseFragment

/**
 * Created by toannguyen
 * Jul 18, 2021 at 11:03
 */
fun BaseFragment.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, length).show()
}