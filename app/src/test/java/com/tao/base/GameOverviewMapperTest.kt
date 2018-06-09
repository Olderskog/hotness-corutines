package com.tao.base

import com.tao.base.hotness.data.toDomain
import com.tao.base.hotness.domain.entities.GameOverview
import com.tao.datasource.remote.entities.TGameOverview
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class GameOverviewMapperTest : Spek( {

    val nullTGameOverViewLit: List<TGameOverview>? = null

    given("a list of TGameOverview which is null") {
        it("should be that the mapping returns an empty list") {
            val mappedEmptyList = nullTGameOverViewLit.toDomain()
            assertEquals(emptyList<GameOverview>(), mappedEmptyList)
        }
    }

    val emptyTGameOverviewList = emptyList<TGameOverview>()

    given("a list of TGameOverview with ${emptyTGameOverviewList.size} elements") {

        it("should be that the mapped list also has ${emptyTGameOverviewList.size} elements") {
            val mappedEmptyList = emptyTGameOverviewList.toDomain()
            assertEquals(emptyTGameOverviewList.size, mappedEmptyList.size)
        }
    }


    val tGameWithAllValuesSet = TGameOverview(1L, 21, "MockGame", "", "1994")
    val tGameWithNullThumbnail = TGameOverview(1L, 21, "MockGame", null, "1994")
    val listOfTGameOverview = listOf(tGameWithAllValuesSet, tGameWithNullThumbnail)

    given("a list of TGameOverview with ${listOfTGameOverview.size} elements") {
        val domainGames = listOfTGameOverview.toDomain()

        it("should be that the mapped list also has ${listOfTGameOverview.size} elements") {
            assertEquals(listOfTGameOverview.size, domainGames.size)
        }

        context("-> first element has all values set") {
            val game = domainGames.first()

            on("mapping to domain") {
                it("should be that id was mapped correctly") {
                    assertEquals(1L, game.gameId)
                }

                it("should be that rank was mapped correctly") {
                    assertEquals(21, game.rank)
                }

                it("should be that name was mapped correctly") {
                    assertEquals("MockGame", game.name)
                }

                it("should be that thumbnail was mapped correctly") {
                    assertEquals("", game.thumbnail)
                }

                it("should be that year was mapped correctly") {
                    assertEquals("1994", game.yearPublished)
                }

            }
        }

        context("-> second element has thumbnail set to null") {
            val game = domainGames[1]

            on("mapping to domain") {

                it("should be that thumbnail was mapped to empty string") {
                    assertEquals("", game.thumbnail)
                }

            }
        }

    }

} )
