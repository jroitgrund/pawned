package me.roitgrund.pawned

internal interface Piece {
  fun tryMove(gameInfo: GameInfo, from: Coord, to: Coord, color: Color): GameInfo?
}

internal fun movePiece(
    piece: Piece,
    gameInfo: GameInfo,
    from: Coord,
    to: Coord,
    color: Color
): GameInfo {
  val (whitePieces, blackPieces) =
      when (color) {
        Color.WHITE ->
            Pair(gameInfo.whitePieces - from + Pair(to, piece), gameInfo.blackPieces - to)
        Color.BLACK ->
            Pair(gameInfo.blackPieces - from + Pair(to, piece), gameInfo.whitePieces - to)
      }

  return GameInfo(whitePieces, blackPieces)
}
