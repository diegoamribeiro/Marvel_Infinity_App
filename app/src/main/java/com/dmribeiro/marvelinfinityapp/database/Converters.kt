package com.dmribeiro.marvelinfinityapp.database

import androidx.room.TypeConverter
import com.dmribeiro.marvelinfinityapp.model.MovieItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromMovieToString(movieItem: MovieItem): String{
        return gson.toJson(movieItem)
    }

    @TypeConverter
    fun fromStringToMovie(movieItem: String): MovieItem{
        val listType = object : TypeToken<MovieItem>(){}.type
        return gson.fromJson(movieItem, listType)
    }

}