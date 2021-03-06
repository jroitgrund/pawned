package me.roitgrund.pawned

import me.roitgrund.pawned.api.Color
import me.roitgrund.pawned.api.PieceType

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
        else -> throw IllegalStateException()
      }

  return GameInfo(whitePieces, blackPieces, gameInfo.castlingInfo)
}
