package com.dmribeiro.marvelinfinityapp.viewmodel

import androidx.lifecycle.*
import com.dmribeiro.marvelinfinityapp.model.FavoriteEntity
import com.dmribeiro.marvelinfinityapp.model.MovieItem
import com.dmribeiro.marvelinfinityapp.remote.ResourceNetwork
import com.dmribeiro.marvelinfinityapp.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: Repository
): ViewModel(){

    // Database Call
    var readMovies: LiveData<List<MovieItem>> = repository.local.getAllMoviesFromDataBase().asLiveData()
    val sortByTitle = repository.local.filterByTitle()
    val readFavoriteMovies: LiveData<List<FavoriteEntity>> = repository.local.readFavoritesMovies().asLiveData()

    private fun insertMoviesIntoDatabase(movieItem: MovieItem){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertMovies(movieItem)
        }
    }

    fun insertFavoriteMovie(favoriteEntity: FavoriteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteMovie(favoriteEntity)
        }


    fun deleteFavoriteMovie(favoriteEntity: FavoriteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteMovie(favoriteEntity)
    }

    fun deleteAllFavoriteMovie(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoritesMovies()
        }
    }

    private fun offLineCacheMovies(movieItem: List<MovieItem>){
        for (i in movieItem.indices){
            val item = movieItem[i]
            insertMoviesIntoDatabase(item)
        }
    }

    // Remote Call
    var moviesResponse: MutableLiveData<ResourceNetwork<List<MovieItem>>> = MutableLiveData()

    fun getAllRemoteMovies() = viewModelScope.launch(Dispatchers.IO) {
        getAllSafeMovies()
    }

    private suspend fun getAllSafeMovies() {
        moviesResponse.postValue(ResourceNetwork.Loading())
        val response = repository.remote.getAllMovies()
        moviesResponse.postValue(handleMovieResponse(response))
        val movieEntity = response.body()
        if (movieEntity != null){
            offLineCacheMovies(movieEntity)
        }
    }

    private fun handleMovieResponse(response: Response<List<MovieItem>>) : ResourceNetwork<List<MovieItem>>{
        moviesResponse.postValue(ResourceNetwork.Loading())
        return if (response.isSuccessful){
            val movies = response.body()
            ResourceNetwork.Success(movies!!)
        }else{
            ResourceNetwork.Error(response.message())
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<MovieItem>> {
        return repository.local.searchFromDatabase(searchQuery)
    }

}