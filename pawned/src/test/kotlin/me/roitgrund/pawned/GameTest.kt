package me.roitgrund.pawned

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import me.roitgrund.pawned.api.NextTurnState

internal class GameTest {
  @Test
  fun white_plays_first() {
    assertEquals(NextTurnState.WHITE_TO_PLAY, Game().nextTurnState)
  }

  @Test
  fun play_e4() {
    val game = Game()
    assertTrue(game.playMove(Coord("e2"), Coord("e4")))
    assertEquals(NextTurnState.BLACK_TO_PLAY, game.nextTurnState)
  }

  @Test
  fun fail_to_play_e5() {
    val game = Game()
    assertFalse(game.playMove(Coord("e2"), Coord("e5")))
    assertEquals(NextTurnState.WHITE_TO_PLAY, game.nextTurnState)
  }

  @Test
  fun must_handle_check() {
    val game = Game()
    assertTrue(game.playMove(Coord("f2"), Coord("f3")))
    assertTrue(game.playMove(Coord("e7"), Coord("e6")))
    assertTrue(game.playMove(Coord("b1"), Coord("c3")))
    assertTrue(game.playMove(Coord("d8"), Coord("h4")))

    assertFalse(game.playMove(Coord("a2"), Coord("a3")))
    assertTrue(game.playMove(Coord("g2"), Coord("g3")))
  }

  @Test
  fun check_mate() {
    val game = Game()

    assertTrue(game.playMove(Coord("e2"), Coord("e4")))
    assertTrue(game.playMove(Coord("c7"), Coord("c5")))
    assertTrue(game.playMove(Coord("f1"), Coord("c4")))
    assertTrue(game.playMove(Coord("b8"), Coord("c6")))
    assertTrue(game.playMove(Coord("d1"), Coord("h5")))
    assertTrue(game.playMove(Coord("g8"), Coord("f6")))
    assertTrue(game.playMove(Coord("h5"), Coord("f7")))

    assertEquals(NextTurnState.WHITE_WON, game.nextTurnState)
  }
}
