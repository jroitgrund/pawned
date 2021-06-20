package me.roitgrund.pawned

import me.roitgrund.pawned.api.Color
import me.roitgrund.pawned.api.PieceType

internal class Queen : Piece {
  override fun tryMove(gameInfo: GameInfo, from: Coord, to: Coord, color: Color): GameInfo? {
    val squaresTo =
        from.squaresToMovingLikeRook(to) ?: from.squaresToMovingLikeBishop(to) ?: return null

    if (squaresTo.any {
      gameInfo.blackPieces.containsKey(it) || gameInfo.whitePieces.containsKey(it)
    }) {
      return null
    }

    if (color == Color.WHITE && gameInfo.whitePieces.containsKey(to) ||
        color == Color.BLACK && gameInfo.blackPieces.containsKey(to)) {
      return null
    }

    return movePiece(this, gameInfo, from, to, color)
  }

  override val pieceType = PieceType.QUEEN
}
