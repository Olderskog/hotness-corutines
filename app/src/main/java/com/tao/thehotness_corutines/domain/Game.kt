package com.tao.thehotness_corutines.domain

data class GameOverview(val gameId: Long,
                        val rank: Int,
                        val name: String,
                        val thumbnail: String,
                        val yearPublished: String)
