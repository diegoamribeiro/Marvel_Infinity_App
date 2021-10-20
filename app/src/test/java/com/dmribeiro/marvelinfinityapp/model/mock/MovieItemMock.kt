package com.dmribeiro.marvelinfinityapp.model.mock

import com.dmribeiro.marvelinfinityapp.model.MovieItem
import retrofit2.Response

object MovieItemMock{
    fun mockMovieItemSuccess(): Response<List<MovieItem>> {
        val movieItem = MovieItem(
            0,
            title = "Iron Man",
            year = "2008",
            rated = "PG-13",
            released = "02 May 2008",
            runtime = "126 min",
            genre = "Action, Adventure, Sci-Fi",
            director = "Jon Favreau",
            writer = "Mark Fergus (screenplay), Hawk Ostby (screenplay), Art Marcum (screenplay), Matt Holloway (screenplay), Stan Lee (characters), Don Heck (characters), Larry Lieber (characters), Jack Kirby (characters)",
            actors = "Robert Downey Jr., Terrence Howard, Jeff Bridges, Gwyneth Paltrow",
            plot = "After being held captive in an Afghan cave, billionaire engineer Tony Stark creates a unique weaponized suit of armor to fight evil.",
            poster = "https://m.media-amazon.com/images/M/MV5BMTczNTI2ODUwOF5BMl5BanBnXkFtZTcwMTU0NTIzMw@@._V1_SX500.jpg"
        )

        return Response.success(listOf(movieItem))
    }
}