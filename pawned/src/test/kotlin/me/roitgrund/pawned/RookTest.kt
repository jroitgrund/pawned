package me.roitgrund.pawned

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import me.roitgrund.pawned.api.Color

private val whiteRook = Rook()
private val whitePawn = Pawn()
private val blackPawn = Pawn()
private val simpleGameInfo =
    GameInfo(
        mapOf(Coord("a1") to whiteRook, Coord("g1") to whitePawn), mapOf(Coord("a6") to blackPawn))

internal class RookTest {

  @Test
  fun rook_move() {
    assertNull(whiteRook.tryMove(simpleGameInfo, Coord("a1"), Coord("a1"), Color.WHITE))
    assertNull(whiteRook.tryMove(simpleGameInfo, Coord("a1"), Coord("b2"), Color.WHITE))
    assertNull(whiteRook.tryMove(simpleGameInfo, Coord("a1"), Coord("g1"), Color.WHITE))
    assertNull(whiteRook.tryMove(simpleGameInfo, Coord("a1"), Coord("h1"), Color.WHITE))
    assertNull(whiteRook.tryMove(simpleGameInfo, Coord("a1"), Coord("a7"), Color.WHITE))
    assertEquals(
        GameInfo(
            mapOf(Coord("d1") to whiteRook, Coord("g1") to whitePawn),
            mapOf(Coord("a6") to blackPawn),
            CastlingInfo().moveWhiteQueenSideRook()),
        whiteRook.tryMove(simpleGameInfo, Coord("a1"), Coord("d1"), Color.WHITE))
    assertEquals(
        GameInfo(
            mapOf(Coord("a3") to whiteRook, Coord("g1") to whitePawn),
            mapOf(Coord("a6") to blackPawn),
            CastlingInfo().moveWhiteQueenSideRook()),
        whiteRook.tryMove(simpleGameInfo, Coord("a1"), Coord("a3"), Color.WHITE))
    assertEquals(
        GameInfo(
            mapOf(Coord("a6") to whiteRook, Coord("g1") to whitePawn),
            mapOf(),
            CastlingInfo().moveWhiteQueenSideRook()),
        whiteRook.tryMove(simpleGameInfo, Coord("a1"), Coord("a6"), Color.WHITE))
  }
}
