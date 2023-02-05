package com.vaiki.fintechmvvm.ui

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vaiki.fintechmvvm.R
import com.vaiki.fintechmvvm.databinding.FragmentRVBinding
import com.vaiki.fintechmvvm.model.films.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RVFragment : Fragment(R.layout.fragment_r_v) {

    private var _binding: FragmentRVBinding? = null
    private val binding: FragmentRVBinding
        get() = _binding!!

    lateinit var movieAdapter: MoviesAdapter
    private val viewModel by viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRVBinding.bind(view)
        initRecyclerView()
        viewModel.movieList.observe(viewLifecycleOwner) { result ->
            when (result?.status) {
                Result.Status.SUCCESS -> {
                    result.data?.films?.let { list ->
                        movieAdapter.differ.submitList(list)
                    }
                    hideProgressBar()
                    hideErrorConnection()
                }
                Result.Status.ERROR -> {
                    result.message?.let {
                        showError()
                        Log.d("MSG", it)
                    }
                }
                else -> {
                    showProgressBar()
                    hideErrorConnection()
                }
            }
        }
        binding.btnRetry.setOnClickListener {
            viewModel.fetchMovies()
        }
    }


    private fun initRecyclerView() {
        movieAdapter = MoviesAdapter()
        binding.rvFilmsList.adapter = movieAdapter
        setupClickListener()
    }

    private fun showProgressBar() {
        binding.loading.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.loading.visibility = View.INVISIBLE
    }

    private fun hideErrorConnection() {
        binding.llConnectionMsg.visibility = View.INVISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showError() {
        binding.llConnectionMsg.visibility = View.VISIBLE
        hideProgressBar()
    }

    private fun getScreenOrientation(): Int {
        return when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> R.id.fv_container
            Configuration.ORIENTATION_LANDSCAPE -> R.id.fv_container2
            else -> 0
        }
    }

    private fun launchFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.popBackStack()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(getScreenOrientation(), fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupClickListener() {
        movieAdapter.onMovieItemClickListener = {
            Log.d("MOVIE", it.filmId.toString())
            launchFragment(DetailsFragment.newInstance(it.filmId))
        }
    }
}