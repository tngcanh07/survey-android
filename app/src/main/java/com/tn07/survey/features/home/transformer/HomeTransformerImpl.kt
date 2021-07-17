package com.tn07.survey.features.home.transformer

import com.tn07.survey.domain.entities.User
import com.tn07.survey.features.home.uimodel.HomeState
import com.tn07.survey.features.home.uimodel.UserUiModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 17, 2021 at 17:43
 */
class HomeTransformerImpl @Inject constructor() : HomeTransformer {
    private val simpleDateFormat = SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault())

    override fun transformUser(user: User): UserUiModel {
        return UserUiModel(
            email = user.email,
            avatar = user.avatarUrl
        )
    }

    override fun transformInitState(user: UserUiModel): HomeState.HomePage {
        return HomeState.HomePage(
            user = user,
            dateTime = simpleDateFormat.format(Date())
        )

    }
}