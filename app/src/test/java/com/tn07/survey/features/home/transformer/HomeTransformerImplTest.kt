package com.tn07.survey.features.home.transformer

import android.content.res.Resources
import androidx.test.platform.app.InstrumentationRegistry
import com.tn07.survey.R
import com.tn07.survey.domain.entities.Survey
import com.tn07.survey.domain.entities.User
import com.tn07.survey.domain.exceptions.ConnectionException
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by toannguyen
 * Jul 23, 2021 at 19:07
 */
@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = Config.NONE,
    sdk = [Config.OLDEST_SDK]
)
class HomeTransformerImplTest {

    private lateinit var transformer: HomeTransformerImpl
    private lateinit var resources: Resources

    @Before
    fun setUp() {
        val context = Mockito.spy(InstrumentationRegistry.getInstrumentation().context)
        resources = Mockito.spy(context.resources)
        Mockito.`when`(context.resources).thenReturn(resources)
        Mockito.`when`(resources.getString(Mockito.anyInt() + R.string.app_name)).thenAnswer {
            val resId = it.arguments[0] as Int
            return@thenAnswer "mocked-str-id<$resId>"
        }

        transformer = HomeTransformerImpl(context)
    }

    @Test
    fun todayDateTime() {
        val expectedDateTime = SimpleDateFormat("EEEE, MMMM dd")
            .format(Date())
        val dateTime = transformer.todayDateTime
        Assert.assertEquals(expectedDateTime, dateTime)
    }

    @Test
    fun transformUser() {
        val user = Mockito.mock(User::class.java)
        Mockito.`when`(user.email).thenReturn("email@mail.com")
        Mockito.`when`(user.avatarUrl).thenReturn("https://mock-emailurl.com")

        val result = transformer.transformUser(user)

        Assert.assertEquals(user.email, result.email)
        Assert.assertEquals(user.avatarUrl, result.avatar)
    }

    @Test
    fun transformSurvey() {
        val survey = Mockito.mock(Survey::class.java)
        Mockito.`when`(survey.id).thenReturn("survey-id")
        Mockito.`when`(survey.title).thenReturn("survey-title")
        Mockito.`when`(survey.description).thenReturn("survey-description")
        Mockito.`when`(survey.coverImageUrl).thenReturn("https://mock-image.com/img")

        val result = transformer.transformSurvey(survey)

        Assert.assertEquals(survey.id, result.id)
        Assert.assertEquals(survey.title, result.title)
        Assert.assertEquals(survey.description, result.description)
        Assert.assertEquals(survey.coverImageUrl, result.backgroundImageUrl)
    }


    @Test
    fun transformErrorMessage_connectionException() {
        val exception = ConnectionException(SocketTimeoutException())
        val errorMessage = transformer.transformErrorMessage(exception)
        Assert.assertEquals(
            resources.getString(R.string.error_connection),
            errorMessage
        )
    }

    @Test
    fun transformErrorMessage_unexpectedError() {
        val exception = IllegalArgumentException("unknown")
        val errorMessage = transformer.transformErrorMessage(exception)
        Assert.assertEquals(
            "${exception.javaClass.name}: ${exception.localizedMessage}",
            errorMessage
        )
    }
}