package com.tn07.survey.features.base

import android.widget.Toast

/**
 * Created by toannguyen
 * Jul 18, 2021 at 11:03
 */
fun BaseFragment.toast(messageId: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), messageId, length).show()
}

fun BaseFragment.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, length).show()
}