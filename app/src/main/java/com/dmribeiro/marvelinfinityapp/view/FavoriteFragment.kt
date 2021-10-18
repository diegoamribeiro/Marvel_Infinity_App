package com.dmribeiro.marvelinfinityapp.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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

        viewModel.emptyDatabase.observe(viewLifecycleOwner,  {
            showEmptyDatabaseView(it)
        })
        setupRecyclerView()
        return binding.root
    }

    private fun showEmptyDatabaseView(emptyDatabase: Boolean){
        if (emptyDatabase){
            binding.tvNoData.visibility = View.VISIBLE
            binding.ivNoData.visibility = View.VISIBLE
        }else{
            binding.tvNoData.visibility = View.INVISIBLE
            binding.ivNoData.visibility = View.INVISIBLE
        }
    }

    private fun setupRecyclerView(){
        recyclerView = binding.rvFavoriteList
        recyclerView.apply {
            adapter = favoriteListAdapter
            layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        }

        viewModel.readFavoriteMovies.observe(viewLifecycleOwner, {data ->
            viewModel.verifyEmptyList(data)
            favoriteListAdapter.setData(data)
        })
    }
}