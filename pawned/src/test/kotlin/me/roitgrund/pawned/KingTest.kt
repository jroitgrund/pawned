package me.roitgrund.pawned

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class KingTest {

  @Test
  fun king_move() {
    val whiteKing = King()
    val whitePawn = Pawn()
    val blackPawn = Pawn()
    val simpleGameInfo =
        GameInfo(
            mapOf(Coord("e4") to whiteKing, Coord("d5") to whitePawn),
            mapOf(Coord("e5") to blackPawn))

    assertNull(whiteKing.tryMove(simpleGameInfo, Coord("e4"), Coord("e2"), Color.WHITE))
    assertNull(whiteKing.tryMove(simpleGameInfo, Coord("e4"), Coord("d5"), Color.WHITE))
    assertEquals(
        GameInfo(
            mapOf(Coord("e5") to whiteKing, Coord("d5") to whitePawn),
            mapOf(),
            CastlingInfo().moveWhiteKing()),
        whiteKing.tryMove(simpleGameInfo, Coord("e4"), Coord("e5"), Color.WHITE))
    assertEquals(
        GameInfo(
            mapOf(Coord("d3") to whiteKing, Coord("d5") to whitePawn),
            mapOf(Coord("e5") to blackPawn),
            CastlingInfo().moveWhiteKing()),
        whiteKing.tryMove(simpleGameInfo, Coord("e4"), Coord("d3"), Color.WHITE))
  }

  @Test
  fun king_castle() {
    val whiteKing = King()
    val whiteRook = Rook()
    val simpleGameInfo =
        GameInfo(
            mapOf(Coord("e1") to whiteKing, Coord("a1") to whiteRook),
            mapOf(),
            CastlingInfo().moveWhiteKingSideRook())
    assertNull(
        whiteKing.tryMove(
            GameInfo(
                simpleGameInfo.whitePieces,
                simpleGameInfo.blackPieces,
                simpleGameInfo.castlingInfo.moveWhiteKing()),
            Coord("e1"),
            Coord("c1"),
            Color.WHITE))
    assertNull(
        whiteKing.tryMove(
            GameInfo(
                simpleGameInfo.whitePieces,
                simpleGameInfo.blackPieces,
                simpleGameInfo.castlingInfo.moveWhiteQueenSideRook()),
            Coord("e1"),
            Coord("c1"),
            Color.WHITE))
    assertEquals(
        GameInfo(
            mapOf(Coord("c1") to whiteKing, Coord("d1") to whiteRook),
            mapOf(),
            simpleGameInfo.castlingInfo.moveWhiteKing()),
        whiteKing.tryMove(simpleGameInfo, Coord("e1"), Coord("c1"), Color.WHITE))
  }

  @Test
  fun no_castle_through_piece_or_check() {
    val whiteKing = King()
    val whiteRook = Rook()
    val whiteKnight = Pawn()
    val blackBishop = Bishop()

    assertNull(
        whiteKing.tryMove(
            GameInfo(
                mapOf(
                    Coord("a1") to whiteRook, Coord("e1") to whiteKing, Coord("b1") to whiteKnight),
                mapOf()),
            Coord("e1"),
            Coord("c1"),
            Color.WHITE))
    assertNull(
        whiteKing.tryMove(
            GameInfo(
                mapOf(Coord("a1") to whiteRook, Coord("e1") to whiteKing),
                mapOf(Coord("c1") to blackBishop)),
            Coord("e1"),
            Coord("c1"),
            Color.WHITE))
    assertNull(
        whiteKing.tryMove(
            GameInfo(
                mapOf(Coord("a1") to whiteRook, Coord("e1") to whiteKing),
                mapOf(Coord("b4") to blackBishop)),
            Coord("e1"),
            Coord("c1"),
            Color.WHITE))
    assertNull(
        whiteKing.tryMove(
            GameInfo(
                mapOf(Coord("a1") to whiteRook, Coord("e1") to whiteKing),
                mapOf(Coord("f3") to blackBishop)),
            Coord("e1"),
            Coord("c1"),
            Color.WHITE))
  }
}
