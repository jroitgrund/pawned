package me.roitgrund.pawned

import kotlin.math.abs

internal class Pawn : Piece {
  override fun tryMove(gameInfo: GameInfo, from: Coord, to: Coord, color: Color): GameInfo? {
    val (rankDelta, twoRanksStartRank, piecesToCapture) =
        if (color == Color.WHITE) {
          Triple(1, 2, gameInfo.blackPieces)
        } else {
          Triple(-1, 7, gameInfo.whitePieces)
        }
    val movingUpTwoRanks = to.rank == from.rank + rankDelta * 2
    if (from.file == to.file && (to.rank == from.rank + rankDelta || movingUpTwoRanks)) {
      if (gameInfo.blackPieces.containsKey(to) || gameInfo.whitePieces.containsKey(to)) {
        return null
      }

      if (movingUpTwoRanks) {
        val skippedSquare = Coord(to.file, to.rank - rankDelta)
        if (from.rank != twoRanksStartRank ||
            gameInfo.whitePieces.containsKey(skippedSquare) ||
            gameInfo.blackPieces.containsKey(skippedSquare)) {
          return null
        }
      }
    } else if (to.rank == from.rank + rankDelta && abs(from.file.code - to.file.code) == 1) {
      if (gameInfo.enPassant != to && !piecesToCapture.containsKey(to)) {
        return null
      }
    } else {
      return null
    }

    val nextGameInfo = movePiece(this, gameInfo, from, to, color)
    return when {
      gameInfo.enPassant == to -> {
        val enPassantCapture = Coord(to.file, to.rank - rankDelta)
        GameInfo(
            nextGameInfo.whitePieces - enPassantCapture,
            nextGameInfo.blackPieces - enPassantCapture)
      }
      movingUpTwoRanks -> {
        GameInfo(
            nextGameInfo.whitePieces,
            nextGameInfo.blackPieces,
            gameInfo.castlingInfo,
            Coord(to.file, to.rank - rankDelta))
      }
      else -> {
        nextGameInfo
      }
    }
  }
}
