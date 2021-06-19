package me.roitgrund.pawned

internal class Bishop : Piece {
  override fun tryMove(gameInfo: GameInfo, from: Coord, to: Coord, color: Color): GameInfo? {
    val squaresTo = from.squaresToMovingLikeBishop(to) ?: return null

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

  override val pieceType = PieceType.BISHOP
}
