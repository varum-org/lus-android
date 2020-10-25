package com.vtnd.lus.data.repository.source.local.api.db

import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vtnd.lus.data.repository.source.local.api.db.dao.Converter
import com.vtnd.lus.data.repository.source.local.api.db.dao.UserDao

@TypeConverters(Converter::class)
abstract class DatabaseManager : RoomDatabase() {
    abstract fun userDao(): UserDao
}
