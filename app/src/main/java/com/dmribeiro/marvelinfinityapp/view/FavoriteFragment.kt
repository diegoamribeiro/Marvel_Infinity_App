package com.dmribeiro.marvelinfinityapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmribeiro.marvelinfinityapp.R
import com.dmribeiro.marvelinfinityapp.databinding.FragmentFavoriteBinding
import com.dmribeiro.marvelinfinityapp.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var recyclerView: RecyclerView
    private val favoriteListAdapter = FavoriteAdapter()
    private val viewModel by viewModels<ListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)


        setupRecyclerView()
        readMoviesFromDatabase()
        return binding.root
    }


    private fun setupRecyclerView(){
        recyclerView = binding.rvFavoriteList
        recyclerView.apply {
            adapter = favoriteListAdapter
            layoutManager = GridLayoutManager(requireContext(), 1)
        }
    }

    private fun readMoviesFromDatabase(){
        viewModel.readFavoriteMovies.observe(viewLifecycleOwner, {favoriteMovies->
            if (favoriteMovies.isNotEmpty()){
                favoriteListAdapter.setData(favoriteMovies)
            }
        })
    }

}