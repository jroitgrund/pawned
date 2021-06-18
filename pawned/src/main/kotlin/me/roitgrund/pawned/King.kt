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

    if (rankDiff > 1 || fileDiff > 1) {
      return null
    }

    return movePiece(this, gameInfo, from, to, color)
  }
}
