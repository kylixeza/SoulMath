package com.ramiyon.soulmath.data.source.local.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ramiyon.soulmath.data.source.local.database.enitity.DailyXpEntity
import com.ramiyon.soulmath.data.source.local.database.enitity.LeaderboardEntity
import com.ramiyon.soulmath.data.source.local.database.enitity.StudentEntity

@Database(entities = [StudentEntity::class, DailyXpEntity::class, LeaderboardEntity::class], version = 3)
abstract class SoulMathDatabase: RoomDatabase() {

    abstract fun soulMathDao(): SoulMathDao

}