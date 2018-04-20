package com.tao.base.android

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.tao.base.R
import com.tao.base.android.utils.inflate
import com.tao.base.domain.entities.Game
import kotlinx.android.synthetic.main.list_item_expansion.view.*
import javax.inject.Inject


class ExpansionAdapter
    @Inject constructor(): RecyclerView.Adapter<ExpansionAdapter.ExpansionViewHolder>() {

    private var expansions = mutableListOf<Game>()

    var clickListener: (Game) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpansionViewHolder {
        return ExpansionViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ExpansionViewHolder, position: Int) {
        expansions.getOrNull(position)?.let { item -> holder.bind(item) }
    }

    override fun getItemCount(): Int = expansions.size

    fun addExpansion(expansion: Game) {
        expansions.add(expansion)
        notifyDataSetChanged() // TODO: Replace with DiffUtil
    }

    inner class ExpansionViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.list_item_expansion)) {
        fun bind(expansion: Game) = with(itemView) {
            Glide.with(this)
                    .load(expansion.thumbnail)
                    .into(expansion_thumbnail)

            expansion_name.text = expansion.name
                                    .substringAfter(": ")
                                    .substringAfterLast(" - ")
                                    .replace("#", " - ")
                                    .trim()

            setOnClickListener { clickListener(expansion) }
        }
    }

}
