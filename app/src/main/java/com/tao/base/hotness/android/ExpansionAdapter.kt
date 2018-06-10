package com.tao.base.hotness.android

import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tao.base.R
import com.tao.base.base.utils.inflate
import com.tao.base.hotness.domain.entities.Game
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
            val loadingIndicator = progressDrawable()

            Glide.with(this)
                    .load(expansion.thumbnail)
                    .apply(RequestOptions()
                                   .placeholder(loadingIndicator)
                                   .error(R.drawable.ic_broken_image_24dp))
                    .into(expansion_thumbnail)

            expansion_name.text = expansion.name
                                    .substringAfter(": ")
                                    .substringAfterLast(" - ")
                                    .replace("#", " - ")
                                    .trim()

            setOnClickListener { clickListener(expansion) }
        }

        private fun progressDrawable(strokeWidth: Float = 5f,
                             centerRadius: Float = 20f) : CircularProgressDrawable {
            return CircularProgressDrawable(itemView.context).apply {
                this.strokeWidth = strokeWidth
                this.centerRadius = centerRadius
                this.start()
            }
        }
    }

}
