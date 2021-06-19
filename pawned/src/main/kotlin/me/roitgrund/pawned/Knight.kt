package me.roitgrund.pawned

import kotlin.math.abs

internal class Knight : Piece {
  override fun tryMove(gameInfo: GameInfo, from: Coord, to: Coord, color: Color): GameInfo? {
    val rankDiff = abs(from.rank - to.rank)
    val fileDiff = abs(from.file.code - to.file.code)

    if (!(rankDiff == 2 && fileDiff == 1 || rankDiff == 1 && fileDiff == 2)) {
      return null
    }

    if (color == Color.WHITE && gameInfo.whitePieces.containsKey(to) ||
        color == Color.BLACK && gameInfo.blackPieces.containsKey(to)) {
      return null
    }

    return movePiece(this, gameInfo, from, to, color)
  }

  override val pieceType = PieceType.KNIGHT
}
