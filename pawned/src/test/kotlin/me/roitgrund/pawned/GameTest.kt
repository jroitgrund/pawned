package me.roitgrund.pawned

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class GameTest {
  @Test
  fun white_plays_first() {
    assertEquals(NextTurnState.WHITE_TO_PLAY, Game().nextTurnState)
  }

  @Test
  fun play_e4() {
    val game = Game()
    assertTrue(game.attemptMove(Coord("e2"), Coord("e4")))
    assertEquals(NextTurnState.BLACK_TO_PLAY, game.nextTurnState)
  }

  @Test
  fun fail_to_play_e5() {
    val game = Game()
    assertFalse(game.attemptMove(Coord("e2"), Coord("e5")))
    assertEquals(NextTurnState.WHITE_TO_PLAY, game.nextTurnState)
  }

  @Test
  fun must_handle_check() {
    val game = Game()
    assertTrue(game.attemptMove(Coord("f2"), Coord("f3")))
    assertTrue(game.attemptMove(Coord("e7"), Coord("e6")))
    assertTrue(game.attemptMove(Coord("b1"), Coord("c3")))
    assertTrue(game.attemptMove(Coord("d8"), Coord("h4")))

    assertFalse(game.attemptMove(Coord("a2"), Coord("a3")))
    assertTrue(game.attemptMove(Coord("g2"), Coord("g3")))
  }
}
