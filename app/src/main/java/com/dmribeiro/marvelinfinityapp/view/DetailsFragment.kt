package com.dmribeiro.marvelinfinityapp.view

import android.graphics.Color.green
import android.graphics.Color.red
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dmribeiro.marvelinfinityapp.R
import com.dmribeiro.marvelinfinityapp.databinding.FragmentDetailsBinding
import com.dmribeiro.marvelinfinityapp.model.FavoriteEntity
import com.dmribeiro.marvelinfinityapp.viewmodel.ListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel by viewModels<ListViewModel>()
    private var isMovieSaved = false
    private var savedMovieId = 0

    private lateinit var menuItem: MenuItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)

        setHasOptionsMenu(true)
        setupInfo()

        return binding.root
    }

    private fun setupInfo(){
        binding.tvCurrentTitle.text = args.currentMovie.title
        binding.tvOverview.text = args.currentMovie.plot
        binding.tvCurrentGenre.text = args.currentMovie.genre
        binding.tvCurrentRelease.text = args.currentMovie.released
        binding.tvCurrentDirector.text = args.currentMovie.director
        binding.tvCurrentRuntime.text = args.currentMovie.runtime
        binding.tvCurrentActor.text = args.currentMovie.actors
        binding.tvCurrentWriters.text = args.currentMovie.writer
        binding.tvCurrentRated.text = args.currentMovie.rated
        Glide.with(binding.ivCurrentDetail)
            .load(args.currentMovie.poster)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.ivCurrentDetail)

        Log.d("***Ids", args.currentMovie.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.details_fragment_menu, menu)
        menuItem = menu.findItem(R.id.save_to_favorites)
        checkSavedRecipes(menuItem)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.save_to_favorites && !isMovieSaved) {
            saveToFavorites(item)
        }else if (item.itemId == R.id.save_to_favorites && isMovieSaved){
            removeFromFavorite(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavorites(item: MenuItem) {
        val favoriteEntity = FavoriteEntity(
            0,
            args.currentMovie
        )
        viewModel.insertFavoriteMovie(favoriteEntity)
        changeMenuItemColor(item, R.color.vermilion)
        showSnackBar("Saved into favorites!", resources.getColor(R.color.green))
        isMovieSaved = true
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        viewModel.readFavoriteMovies.observe(viewLifecycleOwner, { favoriteEntity ->
            try {
                for (savedRecipe in favoriteEntity) {
                    if (savedRecipe.movie.id == args.currentMovie.id) {
                        changeMenuItemColor(menuItem, R.color.vermilion)
                        savedMovieId = savedRecipe.id
                        isMovieSaved = true
                    }
                }
            } catch (e: Exception) {
                Log.d("DetailsFragment", e.message.toString())
            }
        })
    }

    private fun removeFromFavorite(item: MenuItem){
        val favoriteEntity = FavoriteEntity(savedMovieId, args.currentMovie)
        viewModel.deleteFavoriteMovie(favoriteEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from favorites!", resources.getColor(R.color.vermilion))
        isMovieSaved = false
    }

    private fun showSnackBar(message: String, color: Int) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setAction("OK"){}
            .setBackgroundTint(color).show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(requireContext(), color))
    }

    override fun onDestroy() {
        super.onDestroy()
        changeMenuItemColor(menuItem, R.color.white)
    }


}