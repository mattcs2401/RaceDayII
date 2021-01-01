package com.mcssoft.racedayii.entity.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "race_day_details")
class RaceMeetingDBEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id") var id: Long? = null    // value inserted by Room.

    @ColumnInfo(name = "MtgId") var mtgId: String = ""
    @ColumnInfo(name = "WeatherChanged") var weatherChanged: String = ""
    @ColumnInfo(name = "MeetingCode") var meetingCode: String = ""   // eg BR
    @ColumnInfo(name = "VenueName") var venueName: String = ""
    @ColumnInfo(name = "HiRaceNo") var hiRaceNo: String = ""
    @ColumnInfo(name = "MeetingType") var meetingType: String = ""   // e.g. R, T, G, S
    @ColumnInfo(name = "TrackChanged") var trackChanged: String = ""
    @ColumnInfo(name = "NextRaceNo") var nextRaceNo: String = ""     // may not always be there.
    @ColumnInfo(name = "SortOrder") var sortOrder: String = ""
    @ColumnInfo(name = "Abandoned") var abandoned: String = ""       // e.g. N or Y.
}
