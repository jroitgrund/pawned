package me.roitgrund.pawned

import kotlin.math.abs

internal class King : Piece {
  override fun tryMove(gameInfo: GameInfo, from: Coord, to: Coord, color: Color): GameInfo? {
    if (color == Color.WHITE && gameInfo.whitePieces.containsKey(to) ||
        color == Color.BLACK && gameInfo.blackPieces.containsKey(to)) {
      return null
    }

    val rankDiff = abs(from.rank - to.rank)
    val fileDiff = abs(from.file.code - to.file.code)

    if (from == Coord("e1") && to == Coord("c1")) {
      if (gameInfo.castlingInfo.whiteKingMoved ||
          gameInfo.castlingInfo.whiteQueenSideRookMoved ||
          sequenceOf(Coord("b1"), Coord("c1"), Coord("d1")).any {
            gameInfo.allPieceCoordinates.contains(it)
          } ||
          sequenceOf(Coord("c1"), Coord("d1"), Coord("e1")).any { targetCoord ->
            gameInfo.blackPieces.any { (pieceCoord, piece) ->
              piece.tryMove(gameInfo, pieceCoord, targetCoord, Color.BLACK) != null
            }
          }) {
        return null
      } else {
        val whiteKing = gameInfo.whitePieces[Coord("e1")] ?: throw IllegalStateException()
        val whiteRook = gameInfo.whitePieces[Coord("a1")] ?: throw IllegalStateException()
        return GameInfo(
            gameInfo.whitePieces - Coord("e1") - Coord("a1") +
                (Coord("c1") to whiteKing) +
                (Coord("d1") to whiteRook),
            gameInfo.blackPieces,
            gameInfo.castlingInfo.moveWhiteKing())
      }
    }

    if (from == Coord("e1") && to == Coord("g1")) {
      if (gameInfo.castlingInfo.whiteKingMoved ||
          gameInfo.castlingInfo.whiteQueenSideRookMoved ||
          sequenceOf(Coord("g1"), Coord("f1")).any { gameInfo.allPieceCoordinates.contains(it) } ||
          sequenceOf(Coord("e1"), Coord("g1"), Coord("f1")).any { targetCoord ->
            gameInfo.blackPieces.any { (pieceCoord, piece) ->
              piece.tryMove(gameInfo, pieceCoord, targetCoord, Color.BLACK) != null
            }
          }) {
        return null
      } else {
        val whiteKing = gameInfo.whitePieces[Coord("e1")] ?: throw IllegalStateException()
        val whiteRook = gameInfo.whitePieces[Coord("h1")] ?: throw IllegalStateException()
        return GameInfo(
            gameInfo.whitePieces - Coord("e1") - Coord("h1") +
                (Coord("g1") to whiteKing) +
                (Coord("f1") to whiteRook),
            gameInfo.blackPieces,
            gameInfo.castlingInfo.moveWhiteKing())
      }
    }

    if (from == Coord("e8") && to == Coord("c8")) {
      if (gameInfo.castlingInfo.blackKingMoved ||
          gameInfo.castlingInfo.blackKingSideRookMoved ||
          sequenceOf(Coord("b8"), Coord("c8"), Coord("d8")).any {
            gameInfo.allPieceCoordinates.contains(it)
          } ||
          sequenceOf(Coord("c8"), Coord("d8"), Coord("e8")).any { targetCoord ->
            gameInfo.blackPieces.any { (pieceCoord, piece) ->
              piece.tryMove(gameInfo, pieceCoord, targetCoord, Color.BLACK) != null
            }
          }) {
        return null
      } else {
        val blackKing = gameInfo.blackPieces[Coord("e8")] ?: throw IllegalStateException()
        val blackRook = gameInfo.blackPieces[Coord("a8")] ?: throw IllegalStateException()
        return GameInfo(
            gameInfo.whitePieces,
            gameInfo.blackPieces - Coord("e8") - Coord("a8") +
                (Coord("c8") to blackKing) +
                (Coord("d8") to blackRook),
            gameInfo.castlingInfo.moveBlackKing())
      }
    }

    if (from == Coord("e8") && to == Coord("g8")) {
      if (gameInfo.castlingInfo.blackKingMoved ||
          gameInfo.castlingInfo.blackKingSideRookMoved ||
          sequenceOf(Coord("g8"), Coord("f8")).any { gameInfo.allPieceCoordinates.contains(it) } ||
          sequenceOf(Coord("e8"), Coord("g8"), Coord("f8")).any { targetCoord ->
            gameInfo.blackPieces.any { (pieceCoord, piece) ->
              piece.tryMove(gameInfo, pieceCoord, targetCoord, Color.BLACK) != null
            }
          }) {
        return null
      } else {
        val blackKing = gameInfo.blackPieces[Coord("e8")] ?: throw IllegalStateException()
        val blackRook = gameInfo.blackPieces[Coord("h8")] ?: throw IllegalStateException()
        return GameInfo(
            gameInfo.whitePieces,
            gameInfo.blackPieces - Coord("e8") - Coord("h8") +
                (Coord("g8") to blackKing) +
                (Coord("f8") to blackRook),
            gameInfo.castlingInfo.moveBlackKing())
      }
    }

    if (rankDiff > 1 || fileDiff > 1) {
      return null
    }

    val nextGameInfo = movePiece(this, gameInfo, from, to, color)
    return when (color) {
      Color.WHITE ->
          GameInfo(
              nextGameInfo.whitePieces,
              nextGameInfo.blackPieces,
              nextGameInfo.castlingInfo.moveWhiteKing())
      Color.BLACK ->
          GameInfo(
              nextGameInfo.whitePieces,
              nextGameInfo.blackPieces,
              nextGameInfo.castlingInfo.moveBlackKing())
    }
  }
}
