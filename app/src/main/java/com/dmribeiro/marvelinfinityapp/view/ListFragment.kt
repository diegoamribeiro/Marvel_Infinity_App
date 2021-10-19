package com.dmribeiro.marvelinfinityapp.view

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmribeiro.marvelinfinityapp.R
import com.dmribeiro.marvelinfinityapp.databinding.FragmentListBinding
import com.dmribeiro.marvelinfinityapp.remote.ResourceNetwork
import com.dmribeiro.marvelinfinityapp.utils.observeOnce
import com.dmribeiro.marvelinfinityapp.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.coroutines.launch
import android.os.Parcelable
import androidx.recyclerview.widget.LinearLayoutManager
import android.graphics.Movie
import android.preference.PreferenceManager
import android.preference.PreferenceManager.getDefaultSharedPreferences
import com.dmribeiro.marvelinfinityapp.model.MovieItem
import java.util.prefs.Preferences


@AndroidEntryPoint
class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: ListViewModel
    private lateinit var recyclerView: RecyclerView
    private var lastPosition: Int = 0
    private val mAdapter: ListAdapter by lazy { ListAdapter() }
    private lateinit var mLayoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mLayoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        viewModel = ViewModelProvider(requireActivity()).get(ListViewModel::class.java)
        binding = FragmentListBinding.inflate(layoutInflater, container, false)

        setupRecyclerView()

        binding.rvList.showShimmer()
        setHasOptionsMenu(true)
        requestDatabase()
        return binding.root
    }

    private fun setupRecyclerView() {
        recyclerView = binding.rvList
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager
            itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
            }
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    lastPosition = mLayoutManager.findFirstCompletelyVisibleItemPosition()
                }
            })
            val getPreferences: SharedPreferences = getDefaultSharedPreferences(requireContext())
            lastPosition = getPreferences.getInt("lastPosition", 0)
            recyclerView.scrollToPosition(lastPosition)
        }

        viewModel.readMovies.observe(viewLifecycleOwner, {data ->
            mAdapter.setData(data)
        })
    }


    private fun requestDatabase(){
        viewModel.readMovies.observeOnce(viewLifecycleOwner, {database ->
            if (database.isNotEmpty()){
                Log.d("***requestDataBase", "DataBase")
                mAdapter.setData(database)
                binding.rvList.hideShimmer()
            }else{
                requestApiData()
            }
        })
    }

    private fun loadDataFromCache(){
        viewModel.readMovies.observe(viewLifecycleOwner, { database->
            if (database.isNotEmpty()){
                mAdapter.setData(database)
            }
        })
    }

    private fun requestApiData(){
        viewModel.getAllRemoteMovies()
        viewModel.moviesResponse.observe( viewLifecycleOwner, { response ->
            Log.d("***requestApiData", "Api")
            when(response){
                is ResourceNetwork.Success ->{
                    binding.rvList.hideShimmer()
                    response.data.let {
                        mAdapter.setData(it!!)
                    }
                }
                is ResourceNetwork.Error ->{
                    binding.rvList.hideShimmer()
                    loadDataFromCache()
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
                is ResourceNetwork.Loading ->{
                    binding.rvList.showShimmer()
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_name -> viewModel.sortByTitle.observe(this, {mAdapter.setData(it)})
            R.id.menu_year -> viewModel.readMovies.observe(this, {mAdapter.setData(it)})
            R.id.menu_favorite -> findNavController().navigate(R.id.action_listFragment_to_favoriteFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null){
            searChThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null){
            searChThroughDatabase(newText)
        }
        return true
    }

    private fun searChThroughDatabase(query: String){
        val searchQuery = "%$query%"
        viewModel.searchDatabase(searchQuery).observe(this, {list->
            list?.let {
                mAdapter.setData(it)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        val preferences: SharedPreferences = getDefaultSharedPreferences(requireContext())
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putInt("lastPosition", lastPosition)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        val preferences: SharedPreferences = getDefaultSharedPreferences(requireContext())
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putInt("lastPosition", 0)
        editor.apply()
        recyclerView.scrollToPosition(lastPosition)
    }
}