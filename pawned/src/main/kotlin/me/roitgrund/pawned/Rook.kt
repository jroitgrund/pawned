package me.roitgrund.pawned

internal class Rook : Piece {
  override fun tryMove(gameInfo: GameInfo, from: Coord, to: Coord, color: Color): GameInfo? {
    val squaresToMovingLikeRook = from.squaresToMovingLikeRook(to) ?: return null

    if (squaresToMovingLikeRook.any {
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
}
