package com.vaiki.fintechmvvm.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vaiki.fintechmvvm.R
import com.vaiki.fintechmvvm.databinding.ItemListBinding
import com.vaiki.fintechmvvm.model.films.Movie

class MoviesAdapter() : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    var onMovieItemClickListener: ((Movie) -> Unit)? = null


    private val differCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.filmId == newItem.filmId
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movie = differ.currentList[position]
        holder.bind(movie)

        holder.itemView.setOnClickListener {
            onMovieItemClickListener?.invoke(movie)
        }
    }


    override fun getItemCount(): Int = differ.currentList.size
    class MoviesViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(movie: Movie) {
            val genre = movie.genres[0].genre.replaceFirstChar{it.uppercase()}

            with(binding) {
                tvTitle.text = movie.nameRu
                tvGenreAndYear.text = "$genre (${movie.year})"
                Glide.with(itemView.context)
                    .load(movie.posterUrlPreview)
                    .centerCrop()
                    .placeholder(R.drawable.progress_animation)
                    .error(R.drawable.not_found)
                    .into(ivArticleImage)
            }

        }

    }

}


