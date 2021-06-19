package me.roitgrund.pawned

enum class PieceType {
  ROOK,
  KNIGHT,
  BISHOP,
  QUEEN,
  KING,
  PAWN
}

internal interface Piece {
  fun tryMove(gameInfo: GameInfo, from: Coord, to: Coord, color: Color): GameInfo?
  val pieceType: PieceType
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
            Pair(gameInfo.whitePieces - to, gameInfo.blackPieces - from + Pair(to, piece))
      }

  return GameInfo(whitePieces, blackPieces, gameInfo.castlingInfo)
}
