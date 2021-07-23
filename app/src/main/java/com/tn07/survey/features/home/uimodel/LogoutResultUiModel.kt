package com.tn07.survey.features.home.uimodel

/**
 * Created by toannguyen
 * Jul 17, 2021 at 16:29
 */
sealed interface LogoutResultUiModel {
    object Success : LogoutResultUiModel
    object Error : LogoutResultUiModel
}