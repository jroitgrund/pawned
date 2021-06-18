package me.roitgrund.pawned

import kotlin.test.*

private val whiteBishop = Bishop()
private val whitePawn = Pawn()
private val blackPawn = Pawn()
private val simpleGameInfo =
    GameInfo(
        mapOf(Coord("d4") to whiteBishop, Coord("g7") to whitePawn),
        mapOf(Coord("b2") to blackPawn))

internal class BishopTest {

  @Test
  fun bishop_move() {
    assertNull(whiteBishop.tryMove(simpleGameInfo, Coord("d4"), Coord("d4"), Color.WHITE))
    assertNull(whiteBishop.tryMove(simpleGameInfo, Coord("d4"), Coord("d3"), Color.WHITE))
    assertNull(whiteBishop.tryMove(simpleGameInfo, Coord("d4"), Coord("g7"), Color.WHITE))
    assertNull(whiteBishop.tryMove(simpleGameInfo, Coord("d4"), Coord("h8"), Color.WHITE))
    assertNull(whiteBishop.tryMove(simpleGameInfo, Coord("d4"), Coord("a1"), Color.WHITE))
    assertEquals(
        GameInfo(
            mapOf(Coord("f6") to whiteBishop, Coord("g7") to whitePawn),
            mapOf(Coord("b2") to blackPawn)),
        whiteBishop.tryMove(simpleGameInfo, Coord("d4"), Coord("f6"), Color.WHITE))
    assertEquals(
        GameInfo(
            mapOf(Coord("c3") to whiteBishop, Coord("g7") to whitePawn),
            mapOf(Coord("b2") to blackPawn)),
        whiteBishop.tryMove(simpleGameInfo, Coord("d4"), Coord("c3"), Color.WHITE))
    assertEquals(
        GameInfo(mapOf(Coord("b2") to whiteBishop, Coord("g7") to whitePawn), mapOf()),
        whiteBishop.tryMove(simpleGameInfo, Coord("d4"), Coord("b2"), Color.WHITE))
  }
}
