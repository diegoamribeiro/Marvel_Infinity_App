package com.dmribeiro.marvelinfinityapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dmribeiro.marvelinfinityapp.databinding.ItemFavoriteMovieBinding
import com.dmribeiro.marvelinfinityapp.databinding.ItemMovieBinding
import com.dmribeiro.marvelinfinityapp.model.FavoriteEntity
import com.dmribeiro.marvelinfinityapp.model.MovieItem
import com.dmribeiro.marvelinfinityapp.utils.DiffUtilGeneric

class FavoriteAdapter : RecyclerView.Adapter<FavoriteViewHolder>() {

    var movieList = emptyList<FavoriteEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.binding.apply {
            tvTitle.text = movieList[position].movie.title
            tvYear.text = movieList[position].movie.year
//            tvGenre.text = movieList[position].genre
            Glide.with(ivCover)
                .load(movieList[position].movie.poster)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(ivCover)
        }
        holder.itemView.setOnClickListener {
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailsFragment(movieList[position].movie)
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun setData(list: List<FavoriteEntity>){
        val movieDiffUtil = DiffUtilGeneric(movieList, list)
        val movieResult = DiffUtil.calculateDiff(movieDiffUtil)
        this.movieList = list
        movieResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = movieList.size
}

class FavoriteViewHolder(val binding: ItemFavoriteMovieBinding): RecyclerView.ViewHolder(binding.root)
