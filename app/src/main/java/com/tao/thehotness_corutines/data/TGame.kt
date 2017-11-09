package com.tao.thehotness_corutines.data

data class TGameOverview(val gameId: Long,
                         val rank: Int,
                         val name: String,
                         val thumbnail: String,
                         val yearPublished: String)

data class TGame(val gameId: String,
                 val rank: Int,
                 val name: String,
                 val thumbnail: String,
                 val yearPublished: String,
                 val description: String?,
                 val image: String?,
                 val minPlayers: Int?,
                 val maxPlayers: Int?,
                 val playingTime: Int?,
                 val mechanics: List<String>?,
                 val isExpansion: Boolean?,
                 val bggRating: Double?,
                 val averageRating: Double?)