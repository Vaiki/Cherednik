package com.vaiki.fintechmvvm.model.films


data class Movie(

    val filmId: Int,
    val genres: List<Genre>,
    val nameRu: String,
    val posterUrl: String,
    val posterUrlPreview: String,
    val rating: String,
    val year: String,
    val countries: List<Country>
)
