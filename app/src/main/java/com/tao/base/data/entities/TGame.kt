package com.tao.base.data.entities

open class TExpansion(val gameId: Long,
                      val name: String)

open class TGameOverview(val gameId: Long,
                         val rank: Int?,
                         val name: String,
                         val thumbnail: String?,
                         val yearPublished: String?)

class TGame(gameId: Long,
            rank: Int?,
            name: String,
            thumbnail: String?,
            yearPublished: String,
            val description: String?,
            val image: String?,
            val minPlayers: Int?,
            val maxPlayers: Int?,
            val playingTime: Int?,
            val mechanics: List<String>?,
            val isExpansion: Boolean?,
            val bggRating: Double?,
            val averageRating: Double?,
            val expansions: List<TExpansion>?) : TGameOverview(gameId, rank, name, thumbnail, yearPublished)