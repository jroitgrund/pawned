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

    val nextGameInfo = movePiece(this, gameInfo, from, to, color)
    return when (from) {
      Coord("a1") ->
          GameInfo(
              nextGameInfo.whitePieces,
              nextGameInfo.blackPieces,
              nextGameInfo.castlingInfo.moveWhiteQueenSideRook())
      Coord("h1") ->
          GameInfo(
              nextGameInfo.whitePieces,
              nextGameInfo.blackPieces,
              nextGameInfo.castlingInfo.moveWhiteKingSideRook())
      Coord("a8") ->
          GameInfo(
              nextGameInfo.whitePieces,
              nextGameInfo.blackPieces,
              nextGameInfo.castlingInfo.moveBlackQueenSideRook())
      Coord("h8") ->
          GameInfo(
              nextGameInfo.whitePieces,
              nextGameInfo.blackPieces,
              nextGameInfo.castlingInfo.moveBlackKingSideRook())
      else -> nextGameInfo
    }
  }

  override val pieceType = PieceType.ROOK
}
