package com.tn07.survey.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tn07.survey.data.db.dao.SurveyDao
import com.tn07.survey.data.db.entity.SurveyEntity

/**
 * Created by toannguyen
 * Jul 24, 2021 at 22:50
 */
private const val DATABASE_NAME = "surveys.db"
private const val DATABASE_VERSION = 1

internal fun createSurveyDatabase(
    application: Application
): SurveyDatabase {
    return Room
        .databaseBuilder(application, SurveyDatabase::class.java, DATABASE_NAME)
        .build()
}

@Database(
    entities = [SurveyEntity::class],
    version = DATABASE_VERSION
)
abstract class SurveyDatabase : RoomDatabase() {
    abstract fun surveyDao(): SurveyDao
}
