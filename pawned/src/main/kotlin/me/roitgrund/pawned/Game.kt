package me.roitgrund.pawned

class Game {
  var nextTurnState = NextTurnState.WHITE_TO_PLAY
    private set

  private var gameInfo = GameInfo()

  val whitePieces
    get() = gameInfo.whitePieces.map { (coord, piece) -> (coord to piece.pieceType) }
  val blackPieces
    get() = gameInfo.blackPieces.map { (coord, piece) -> (coord to piece.pieceType) }

  fun attemptMove(from: Coord, to: Coord): Boolean {
    val (piece, color) =
        when (nextTurnState) {
          NextTurnState.WHITE_TO_PLAY -> gameInfo.whitePieces[from] to Color.WHITE
          NextTurnState.BLACK_TO_PLAY -> gameInfo.blackPieces[from] to Color.BLACK
          else -> throw IllegalStateException()
        }

    if (piece == null) {
      return false
    }

    val newGameInfo = piece.tryMove(gameInfo, from, to, color) ?: return false

    val (piecesToCheckForKingAggression, piecesToFindKingIn, colorToAggressAs) =
        if (nextTurnState == NextTurnState.WHITE_TO_PLAY) {
          Triple(newGameInfo.blackPieces, newGameInfo.whitePieces, Color.BLACK)
        } else {
          Triple(newGameInfo.whitePieces, newGameInfo.blackPieces, Color.WHITE)
        }
    val kingToCheckForDanger =
        piecesToFindKingIn.asSequence().find { (_, piece) -> piece is King }?.key
            ?: throw IllegalStateException()
    if (piecesToCheckForKingAggression.any { (coord, piece) ->
      piece.tryMove(newGameInfo, coord, kingToCheckForDanger, colorToAggressAs) != null
    }) {
      return false
    }

    gameInfo = newGameInfo
    nextTurnState =
        when (nextTurnState) {
          NextTurnState.WHITE_TO_PLAY -> NextTurnState.BLACK_TO_PLAY
          NextTurnState.BLACK_TO_PLAY -> NextTurnState.WHITE_TO_PLAY
          else -> throw IllegalStateException()
        }
    return true
  }
}
