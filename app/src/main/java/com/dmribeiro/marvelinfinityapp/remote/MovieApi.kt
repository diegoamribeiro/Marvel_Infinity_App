package com.dmribeiro.marvelinfinityapp.remote

import com.dmribeiro.marvelinfinityapp.model.MovieItem
import retrofit2.Response
import retrofit2.http.GET

interface MovieApi {

    @GET("saga")
    suspend fun getAllMovies(): Response<List<MovieItem>>

}