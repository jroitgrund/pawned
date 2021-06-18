package me.roitgrund.pawned

internal data class CastlingInfo(
    val whiteKingMoved: Boolean = false,
    val whiteKingSideRookMoved: Boolean = false,
    val whiteQueenSideRookMoved: Boolean = false,
    val blackKingMoved: Boolean = false,
    val blackKingSideRookMoved: Boolean = false,
    val blackQueenSideRookMoved: Boolean = false
) {
  fun moveWhiteKing() =
      CastlingInfo(
          true,
          whiteKingSideRookMoved,
          whiteQueenSideRookMoved,
          blackKingMoved,
          blackKingSideRookMoved,
          blackQueenSideRookMoved)
  fun moveWhiteKingSideRook() =
      CastlingInfo(
          whiteKingMoved,
          true,
          whiteQueenSideRookMoved,
          blackKingMoved,
          blackKingSideRookMoved,
          blackQueenSideRookMoved)
  fun moveWhiteQueenSideRook() =
      CastlingInfo(
          whiteKingMoved,
          whiteKingSideRookMoved,
          true,
          blackKingMoved,
          blackKingSideRookMoved,
          blackQueenSideRookMoved)
  fun moveBlackKing() =
      CastlingInfo(
          whiteKingMoved,
          whiteKingSideRookMoved,
          whiteQueenSideRookMoved,
          true,
          blackKingSideRookMoved,
          blackQueenSideRookMoved)
  fun moveBlackKingSideRook() =
      CastlingInfo(
          whiteKingMoved,
          whiteKingSideRookMoved,
          whiteQueenSideRookMoved,
          blackKingMoved,
          true,
          blackQueenSideRookMoved)
  fun moveBlackQueenSideRook() =
      CastlingInfo(
          whiteKingMoved,
          whiteKingSideRookMoved,
          whiteQueenSideRookMoved,
          blackKingMoved,
          blackKingSideRookMoved,
          true)
}
