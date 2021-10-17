package com.dmribeiro.marvelinfinityapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dmribeiro.marvelinfinityapp.model.FavoriteEntity
import com.dmribeiro.marvelinfinityapp.model.MovieItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movieItem: MovieItem)

    @Query("SELECT * FROM MOVIE_TABLE ORDER BY year ASC")
    fun readAllMovies(): Flow<List<MovieItem>>

    @Query("SELECT * FROM MOVIE_TABLE ORDER BY title ASC")
    fun filterByTitle(): LiveData<List<MovieItem>>

    @Query("SELECT * FROM MOVIE_TABLE WHERE title LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<MovieItem>>

    @Insert
    suspend fun insertFavoriteMovie(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM FAVORITES_TABLE ORDER BY id ASC")
    fun readFavoritesMovie(): Flow<List<FavoriteEntity>>

    @Delete
    suspend fun deleteFavoriteMovie(favoriteEntity: FavoriteEntity)

    @Query("DELETE FROM FAVORITES_TABLE")
    suspend fun deleteAllFavoritesMovies()


//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertCategory(categoriesEntity: CategoriesEntity)
//
//    @Query("SELECT * FROM CATEGORIES_TABLE ORDER BY id ASC")
//    fun readCategories(): Flow<List<CategoriesEntity>>

}