package com.vtnd.lus.data.repository.source.local.api.db.dao

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converter {
    @TypeConverter
    fun objectsToJson(objects: Any?): String? {
        return Gson().toJson(objects)
    }
}
