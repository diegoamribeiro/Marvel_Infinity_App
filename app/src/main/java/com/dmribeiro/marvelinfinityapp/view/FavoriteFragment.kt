package com.dmribeiro.marvelinfinityapp.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dmribeiro.marvelinfinityapp.R
import com.dmribeiro.marvelinfinityapp.databinding.FragmentFavoriteBinding
import com.dmribeiro.marvelinfinityapp.viewmodel.ListViewModel
import com.google.android.material.snackbar.Snackbar
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
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun confirmRemoval(){
        val dialog = AlertDialog.Builder(requireContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK)
        dialog.setPositiveButton("Yes"){_,_ ->
            viewModel.deleteAllFavoriteMovie()
        }
        dialog.setNegativeButton("No"){_, _, ->}
        dialog.setTitle("Confirm removal")
        dialog.setMessage("Are you sure delete All?")
        dialog.create()
        dialog.show()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete_all_to_favorites -> {
                if (recyclerView.isNotEmpty()){
                    confirmRemoval()
                    setupRecyclerView()
                }else{
                    showSnackBar("Nothing to Show", resources.getColor(R.color.black, resources.newTheme()))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSnackBar(message: String, color: Int) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setAction("OK"){}
            .setBackgroundTint(color).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorites_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}