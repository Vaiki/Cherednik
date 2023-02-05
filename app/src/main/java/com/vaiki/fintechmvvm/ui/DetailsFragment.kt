package com.vaiki.fintechmvvm.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.vaiki.fintechmvvm.model.films.Result
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.vaiki.fintechmvvm.R
import com.vaiki.fintechmvvm.databinding.FragmentDetailsBinding
import com.vaiki.fintechmvvm.model.movie.MovieDescription
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() = _binding!!

    private var idMovie: Int = 0
    private val viewModel by viewModels<MainViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailsBinding.bind(view)
        parseParams()
        subscribeUi()
        backPressed()
    }

    private fun backPressed() {
        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun parseParams() {
        idMovie = requireArguments().getInt(MOVIE_ID)
        viewModel.getMovieDetail(idMovie)
    }

    private fun subscribeUi() {
        viewModel.movie.observe(viewLifecycleOwner, Observer { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.let {
                        updateUi(it)
                    }
                    hideProgressBar()
                }

                Result.Status.ERROR -> {
                    result.message?.let {
                        showError(it)
                    }
                    hideProgressBar()
                }

                Result.Status.LOADING -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun showProgressBar() {
        binding.loading.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.loading.visibility = View.GONE
    }

    private fun updateUi(movie: MovieDescription) {
        val listGenre: MutableList<String> = mutableListOf()
        val listCountry = mutableListOf<String>()
        movie.genres.forEach {
            listGenre.add(it.genre)
        }
        movie.countries.forEach {
            listCountry.add(it.country)
        }

        with(binding) {
            tvTitle.text = movie.nameRu
            tvDescription.text = movie.description
            tvCountry.text = listCountry.joinToString(", ")
            tvGenre.text = listGenre.joinToString(", ")
            Glide.with(this@DetailsFragment)
                .load(movie.posterUrl)
                .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.not_found)
                .into(ivPoster)
        }
    }

    private fun showError(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
        hideProgressBar()
    }

    companion object {
        private const val MOVIE_ID = "movie_id"
        fun newInstance(movieId: Int): DetailsFragment {
            return DetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(MOVIE_ID, movieId)
                }
            }
        }
    }

}