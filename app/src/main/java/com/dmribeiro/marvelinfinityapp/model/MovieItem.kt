package com.dmribeiro.marvelinfinityapp.model
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dmribeiro.marvelinfinityapp.utils.Constants.MOVIE_TABLE
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = MOVIE_TABLE)
data class MovieItem(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @SerializedName("actors")
    val actors: String,
    @SerializedName("director")
    val director: String,
    @SerializedName("genre")
    val genre: String,
    @SerializedName("plot")
    val plot: String,
    @SerializedName("poster")
    val poster: String,
    @SerializedName("rated")
    val rated: String,
    @SerializedName("released")
    val released: String,
    @SerializedName("runtime")
    val runtime: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("writer")
    val writer: String,
    @SerializedName("year")
    val year: String
): Parcelable