package me.roitgrund.pawned

import kotlin.test.*

private val whiteQueen = Queen()
private val whitePawn = Pawn()
private val blackPawn = Pawn()
private val simpleGameInfo =
    GameInfo(
        mapOf(Coord("d4") to whiteQueen, Coord("g7") to whitePawn), mapOf(Coord("d2") to blackPawn))

internal class QueenTest {

  @Test
  fun queen_move() {
    assertNull(whiteQueen.tryMove(simpleGameInfo, Coord("d4"), Coord("d4"), Color.WHITE))
    assertNull(whiteQueen.tryMove(simpleGameInfo, Coord("d4"), Coord("f3"), Color.WHITE))
    assertNull(whiteQueen.tryMove(simpleGameInfo, Coord("d4"), Coord("g7"), Color.WHITE))
    assertNull(whiteQueen.tryMove(simpleGameInfo, Coord("d4"), Coord("h8"), Color.WHITE))
    assertNull(whiteQueen.tryMove(simpleGameInfo, Coord("d4"), Coord("d1"), Color.WHITE))
    assertEquals(
        GameInfo(
            mapOf(Coord("f6") to whiteQueen, Coord("g7") to whitePawn),
            mapOf(Coord("d2") to blackPawn)),
        whiteQueen.tryMove(simpleGameInfo, Coord("d4"), Coord("f6"), Color.WHITE))
    assertEquals(
        GameInfo(
            mapOf(Coord("d3") to whiteQueen, Coord("g7") to whitePawn),
            mapOf(Coord("d2") to blackPawn)),
        whiteQueen.tryMove(simpleGameInfo, Coord("d4"), Coord("d3"), Color.WHITE))
    assertEquals(
        GameInfo(mapOf(Coord("d2") to whiteQueen, Coord("g7") to whitePawn), mapOf()),
        whiteQueen.tryMove(simpleGameInfo, Coord("d4"), Coord("d2"), Color.WHITE))
  }
}
