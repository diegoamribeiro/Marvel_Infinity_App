package com.dmribeiro.marvelinfinityapp.remote

import com.dmribeiro.marvelinfinityapp.model.MovieItem
import retrofit2.Response
import javax.inject.Inject

class RemotedataSource @Inject constructor(
    private val movieApi: MovieApi
) {

    suspend fun getAllMovies(): Response<List<MovieItem>>{
        return movieApi.getAllMovies()
    }

}