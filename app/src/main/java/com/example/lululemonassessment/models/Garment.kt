package com.example.lululemonassessment.models

import androidx.room.*
import java.time.LocalDateTime

@Entity(tableName = "garment")
data class Garment(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "creationDate")
    var creationDate: LocalDateTime
)
