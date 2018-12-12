package com.tao.base.hotness.android.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.google.android.material.chip.Chip
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.tao.base.R
import com.tao.base.base.utils.appComponent
import com.tao.base.base.utils.tint
import com.tao.base.di.ActivityComponent
import com.tao.base.di.modules.activity.ActivityModule
import com.tao.base.hotness.android.*
import com.tao.base.hotness.domain.entities.Game
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsActivity : BaseActivity() {

    private val component: ActivityComponent by lazy { appComponent()
                                                .newActivityComponent(ActivityModule(this))}

    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory)
                                                        .get(HotnessViewModel::class.java)}

    private val gameId by lazy { getBundleService().data.getLong(EXTRA_GAME_ID) }

    @Inject
    lateinit var viewModelFactory: HotnessViewModelFactory
    @Inject
    lateinit var expansionAdapter: ExpansionAdapter

    private var imageJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        component.inject(this)

        setupGui()
        observeChanges()

        viewModel.action(FetchGameDetails(gameId))
    }

    override fun onDestroy() {
        super.onDestroy()
        imageJob?.cancel()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null)
            return super.onOptionsItemSelected(item)

        return when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }


    }

    override fun onBackPressed() {
        finish()
    }

    private fun setupGui() {
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setBackgroundDrawable(ColorDrawable(getColor(android.R.color.transparent)))
        }

        details_game_expansions.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@DetailsActivity, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)

            adapter = expansionAdapter.apply {
                clickListener = { startActivity(DetailsActivity.launchIntent(this@DetailsActivity, it.gameId)) }
            }
        }
    }

    private fun fetchExpansions(game: Game) {
        game.expansions?.forEach {
            Log.i("DetailsActivity", "Fetching details for expansion: ${it.name}")
            viewModel.action(FetchExpansion(it.gameId))
        }
    }

    private fun observeChanges() {
        viewModel.errorMessage.observe { if (it != null && it.isNotEmpty()) Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        viewModel.game.observe { game -> game?.let {
            fetchExpansions(it)
            displayGame(it)
        } }
        viewModel.expansions.observe { expansion -> if (expansion != null) expansionAdapter.addExpansion(expansion) }
    }

    private fun displayGame(game: Game) {
        displayHeader(game)

        details_game_description.text = game.description
                                                .replace("&#10;&#10;", " ")
                                                .replace("&#10;", "")
                                                .replace("&rsquo;", "'")
                                                .replace("&quot;", "\"")
                                                .replace("&mdash;", "-")
                                                .replace("&ndash;", "-")
                                                .replace("&hellip;", "...")
    }

    private fun displayHeader(game: Game) {
        Glide.with(this)
                .asBitmap()
                .load(game.image)
                .listener(object: RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        if (resource == null)
                            return false

                        imageJob = launch {
                            androidx.palette.graphics.Palette.from(resource)
                                    .generate { palette ->
                                        if (palette == null)
                                            return@generate

                                        val swatchDominant = palette.dominantSwatch ?: return@generate
                                        val swatchMuted = palette.mutedSwatch ?: return@generate

                                        details_header_background.setBackgroundColor(swatchDominant.rgb)
                                        supportActionBar?.setBackgroundDrawable(ColorDrawable(swatchDominant.rgb))
                                        details_game_title.setTextColor(swatchDominant.titleTextColor)

                                        displayRating(game, swatchMuted)
                                        displayTime(game, swatchMuted)
                                        displayPlayers(game, swatchMuted)
                                    }
                        }

                        return false
                    }
                })
                .apply(RequestOptions().circleCrop())
                .into(details_game_image)

        details_game_title.text = if (game.isExpansion)
            game.name.substringAfter(": ")
        else
            game.name
    }

    private fun displayRating(game: Game, swatch: androidx.palette.graphics.Palette.Swatch?) {
        if (swatch == null) {
            details_game_rating_chip.visibility = View.GONE
            return
        }

        details_game_rating_chip?.
            setup("%.1f".format(game.averageRating), swatch)

    }

    private fun displayTime(game: Game, swatch: androidx.palette.graphics.Palette.Swatch?) {
        if (swatch == null) {
            details_game_time_chip.visibility = View.GONE
            return
        }

        if (game.playingTime != 0)
            details_game_time_chip?.
                setup("%d".format(game.playingTime), swatch)
        else
            details_game_time_chip?.
                    setup("NA", swatch)
    }

    private fun displayPlayers(game: Game, swatch: androidx.palette.graphics.Palette.Swatch?) {
        if (swatch == null) {
            details_game_players_chip.visibility = View.GONE
            return
        }

        if (game.maxPlayers > game.minPlayers)
        details_game_players_chip?.
                setup("%d - %d".format(game.minPlayers, game.maxPlayers), swatch)
        else
            details_game_players_chip?.
                    setup("%d".format(game.minPlayers), swatch)
    }

    private fun Chip.setup(text: String, swatch: androidx.palette.graphics.Palette.Swatch) {
        this.tint(swatch.rgb)

        // Set text color
        chipText = text
        setTextColor(swatch.bodyTextColor)

        // Set icon color
        val icon = chipIcon
        icon?.tint(swatch.titleTextColor)
        chipIcon = icon

        visibility = View.VISIBLE
    }

    companion object {
        private const val EXTRA_GAME_ID = "100"

        fun launchIntent(context: Context, gameId: Long): Intent {
            return Intent(context, DetailsActivity::class.java).apply {
                putExtra(EXTRA_GAME_ID, gameId)
            }
        }
    }

    private fun <T> LiveData<T>.observe(observe: (T?) -> Unit) = observe(this@DetailsActivity, Observer { observe(it) } )


}