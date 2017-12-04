package com.tao.base.android

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.tao.base.R
import com.tao.base.di.ActivityScope
import com.tao.base.domain.entities.GameOverview
import kotlinx.android.synthetic.main.list_item_game.view.*
import javax.inject.Inject


@ActivityScope
class HotnessAdapter
    @Inject constructor(): RecyclerView.Adapter<HotnessAdapter.GameViewHolder>() {

    var games = emptyList<GameOverview>()
        set(value) {
            field = value
            notifyDataSetChanged() // TODO: Replace with DiffUtil
        }

    var clickListener: (GameOverview) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder = GameViewHolder(parent)

    override fun onBindViewHolder(holder: GameViewHolder?, position: Int) {
        games.getOrNull(position)?.let { holder?.bind(it) }
    }

    override fun getItemCount(): Int = games.size

    inner class GameViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.list_item_game)) {
        fun bind(gameOverview: GameOverview) = with(itemView) {
            Glide.with(this)
                    .load(gameOverview.thumbnail)
                    .into(hotness_game_thumbnail)

            hotness_game_name.text = gameOverview.name
            hotness_game_published.text = gameOverview.yearPublished

            setOnClickListener { clickListener(gameOverview) }
        }
    }
}