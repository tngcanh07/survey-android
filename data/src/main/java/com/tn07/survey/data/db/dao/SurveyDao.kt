package com.tn07.survey.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tn07.survey.data.db.entity.SURVEY_TABLE_NAME
import com.tn07.survey.data.db.entity.SurveyEntity
import io.reactivex.rxjava3.core.Maybe

/**
 * Created by toannguyen
 * Jul 24, 2021 at 22:59
 */
@Dao
interface SurveyDao {
    @Query("SELECT * FROM $SURVEY_TABLE_NAME")
    fun getAllSurveys(): Maybe<List<SurveyEntity>>

    @Query("DELETE FROM $SURVEY_TABLE_NAME")
    fun deleteAllSurveys()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSurveys(surveys: List<SurveyEntity>)
}