package com.tao.base.domain.entities


data class GameOverview(val gameId: Long,
                        val rank: Int,
                        val name: String,
                        val thumbnail: String,
                        val yearPublished: String)

data class Game(val gameId: Long,
                val rank: Int,
                val name: String,
                val thumbnail: String,
                val yearPublished: String,
                val description: String,
                val image: String,
                val minPlayers: Int,
                val maxPlayers: Int,
                val playingTime: Int,
                val mechanics: List<String>,
                val isExpansion: Boolean,
                val bggRating: Double,
                val averageRating: Double,
                val expansions: List<Expansion>? = emptyList())
