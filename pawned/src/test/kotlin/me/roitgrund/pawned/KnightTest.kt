package me.roitgrund.pawned

import kotlin.test.*

private val whiteKnight = Knight()
private val whitePawn = Pawn()
private val blackPawn = Pawn()
private val simpleGameInfo =
    GameInfo(
        mapOf(Coord("d4") to whiteKnight, Coord("e6") to whitePawn),
        mapOf(Coord("b3") to blackPawn))

internal class KnightTest {

  @Test
  fun knight_move() {
    assertNull(whiteKnight.tryMove(simpleGameInfo, Coord("d4"), Coord("d4"), Color.WHITE))
    assertNull(whiteKnight.tryMove(simpleGameInfo, Coord("d4"), Coord("d3"), Color.WHITE))
    assertNull(whiteKnight.tryMove(simpleGameInfo, Coord("d4"), Coord("e6"), Color.WHITE))
    assertEquals(
        GameInfo(
            mapOf(Coord("e2") to whiteKnight, Coord("e6") to whitePawn),
            mapOf(Coord("b3") to blackPawn)),
        whiteKnight.tryMove(simpleGameInfo, Coord("d4"), Coord("e2"), Color.WHITE))
    assertEquals(
        GameInfo(mapOf(Coord("b3") to whiteKnight, Coord("e6") to whitePawn), mapOf()),
        whiteKnight.tryMove(simpleGameInfo, Coord("d4"), Coord("b3"), Color.WHITE))
  }
}
