package me.roitgrund.pawned

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import me.roitgrund.pawned.api.Color

internal class PawnTest {

  @Test
  fun pawn_move() {
    val whitePawn = Pawn()
    val blackPawn = Pawn()
    val gameInfo = GameInfo(mapOf(Coord("b2") to whitePawn), mapOf(Coord("a3") to blackPawn))

    assertNull(whitePawn.tryMove(gameInfo, Coord("b2"), Coord("c2"), Color.WHITE))
    assertNull(whitePawn.tryMove(gameInfo, Coord("b2"), Coord("c3"), Color.WHITE))

    assertEquals(
        GameInfo(mapOf(Coord("b3") to whitePawn), mapOf(Coord("a3") to blackPawn)),
        whitePawn.tryMove(gameInfo, Coord("b2"), Coord("b3"), Color.WHITE))
    assertEquals(
        GameInfo(
            mapOf(Coord("b4") to whitePawn),
            mapOf(Coord("a3") to blackPawn),
            CastlingInfo(),
            Coord("b3")),
        whitePawn.tryMove(gameInfo, Coord("b2"), Coord("b4"), Color.WHITE))
    assertEquals(
        GameInfo(mapOf(Coord("a3") to whitePawn), mapOf()),
        whitePawn.tryMove(gameInfo, Coord("b2"), Coord("a3"), Color.WHITE))
  }

  @Test
  fun pawn_en_passant() {
    val whitePawn = Pawn()
    val blackPawn = Pawn()

    assertEquals(
        GameInfo(mapOf(Coord("b7") to whitePawn), mapOf()),
        whitePawn.tryMove(
            GameInfo(
                mapOf(Coord("a6") to whitePawn),
                mapOf(Coord("b6") to blackPawn),
                CastlingInfo(),
                Coord("b7")),
            Coord("a6"),
            Coord("b7"),
            Color.WHITE))
    assertNull(
        whitePawn.tryMove(
            GameInfo(
                mapOf(Coord("a6") to whitePawn),
                mapOf(Coord("b6") to blackPawn),
            ),
            Coord("a6"),
            Coord("b7"),
            Color.WHITE))
  }
}
