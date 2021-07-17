package com.tn07.survey.features.home.transformer

import com.tn07.survey.domain.entities.User
import com.tn07.survey.features.home.uimodel.UserUiModel
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 17, 2021 at 17:43
 */
class HomeTransformerImpl @Inject constructor() : HomeTransformer {

    override fun transformUser(user: User): UserUiModel {
        return UserUiModel(
            email = user.email,
            avatar = user.avatarUrl
        )
    }
}