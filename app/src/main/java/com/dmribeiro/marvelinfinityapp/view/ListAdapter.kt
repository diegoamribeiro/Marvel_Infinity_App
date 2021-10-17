package com.dmribeiro.marvelinfinityapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dmribeiro.marvelinfinityapp.databinding.ItemMovieBinding
import com.dmribeiro.marvelinfinityapp.model.MovieItem
import com.dmribeiro.marvelinfinityapp.utils.DiffUtilGeneric

class ListAdapter : RecyclerView.Adapter<ListViewHolder>() {

    private var movieList = emptyList<MovieItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.apply {
            tvTitle.text = movieList[position].title
            tvYear.text = movieList[position].year
//            tvGenre.text = movieList[position].genre
            Glide.with(ivCover)
                .load(movieList[position].poster)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(ivCover)
        }
        holder.itemView.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToDetailsFragment(movieList[position])
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun setData(list: List<MovieItem>){
        val movieDiffUtil = DiffUtilGeneric(movieList, list)
        val movieResult = DiffUtil.calculateDiff(movieDiffUtil)
        this.movieList = list
        movieResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = movieList.size
}

class ListViewHolder(val binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root)
