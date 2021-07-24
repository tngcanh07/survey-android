package com.tn07.survey.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tn07.survey.domain.entities.Survey

/**
 * Created by toannguyen
 * Jul 24, 2021 at 22:59
 */

internal const val SURVEY_TABLE_NAME = "Surveys"

@Entity(tableName = SURVEY_TABLE_NAME)
class SurveyEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey
    override val id: String,

    @ColumnInfo(name = "title")
    override val title: String,

    @ColumnInfo(name = "description")
    override val description: String,

    @ColumnInfo(name = "coverImageUrl")
    override val coverImageUrl: String
) : Survey