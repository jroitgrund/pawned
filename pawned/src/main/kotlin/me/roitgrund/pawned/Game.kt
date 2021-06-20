package me.roitgrund.pawned

internal data class CheckInfo(
    val piecesToCheckForKingAggression: Map<Coord, Piece>,
    val piecesToFindKingIn: Map<Coord, Piece>,
    val colorToAggressAs: Color
)

internal enum class RemainingPieces {
  KING,
  KING_AND_KNIGHT,
  KING_AND_LIGHT_BISHOP,
  KING_AND_DARK_BISHOP,
  NOTHING_SPECIAL
}

private val STALEMATE_CONDITIONS =
    setOf(
        setOf(RemainingPieces.KING, RemainingPieces.KING),
        setOf(RemainingPieces.KING, RemainingPieces.KING_AND_KNIGHT),
        setOf(RemainingPieces.KING, RemainingPieces.KING_AND_DARK_BISHOP),
        setOf(RemainingPieces.KING, RemainingPieces.KING_AND_LIGHT_BISHOP),
        setOf(RemainingPieces.KING_AND_DARK_BISHOP, RemainingPieces.KING_AND_DARK_BISHOP),
        setOf(RemainingPieces.KING_AND_LIGHT_BISHOP, RemainingPieces.KING_AND_LIGHT_BISHOP))

private fun remainingPieces(pieces: Map<Coord, Piece>): RemainingPieces {
  if (pieces.size == 1) {
    return RemainingPieces.KING
  }

  if (pieces.size == 2 && pieces.values.any { it.pieceType == PieceType.KNIGHT }) {
    return RemainingPieces.KING_AND_KNIGHT
  }

  if (pieces.size == 2) {
    val (coord, _) =
        pieces.asSequence().find { (_, piece) -> piece.pieceType == PieceType.BISHOP }
            ?: return RemainingPieces.NOTHING_SPECIAL
    return when (((coord.rank - 1) + (coord.file.code - 'a'.code)) % 2) {
      0 -> RemainingPieces.KING_AND_DARK_BISHOP
      1 -> RemainingPieces.KING_AND_LIGHT_BISHOP
      else -> throw IllegalStateException()
    }
  }

  return RemainingPieces.NOTHING_SPECIAL
}

class Game {
  var nextTurnState = NextTurnState.WHITE_TO_PLAY
    private set

  private var gameInfo = GameInfo()

  val whitePieces
    get() = gameInfo.whitePieces.map { (coord, piece) -> (coord to piece.pieceType) }
  val blackPieces
    get() = gameInfo.blackPieces.map { (coord, piece) -> (coord to piece.pieceType) }

  fun playMove(from: Coord, to: Coord): Boolean {
    val (nextGameInfo, turnState) = attemptMove(from, to) ?: return false

    gameInfo = nextGameInfo
    nextTurnState = turnState

    if (ALL_COORDS.all { from -> ALL_COORDS.all { to -> attemptMove(from, to) == null } }) {
      val checkInfoForCurrentPlayer = checkInfoForCurrentPlayer(gameInfo)
      if (isInCheck(checkInfoForCurrentPlayer, gameInfo)) {
        nextTurnState =
            when (nextTurnState) {
              NextTurnState.WHITE_TO_PLAY -> NextTurnState.BLACK_WON
              NextTurnState.BLACK_TO_PLAY -> NextTurnState.WHITE_WON
              else -> throw IllegalStateException()
            }
      } else {
        nextTurnState = NextTurnState.STALEMATE
      }
    }

    if (STALEMATE_CONDITIONS.contains(
        setOf(remainingPieces(gameInfo.whitePieces), remainingPieces(gameInfo.blackPieces)))) {
      nextTurnState = NextTurnState.STALEMATE
    }

    return true
  }

  private fun attemptMove(from: Coord, to: Coord): Pair<GameInfo, NextTurnState>? {
    val (piece, color) =
        when (nextTurnState) {
          NextTurnState.WHITE_TO_PLAY -> gameInfo.whitePieces[from] to Color.WHITE
          NextTurnState.BLACK_TO_PLAY -> gameInfo.blackPieces[from] to Color.BLACK
          else -> throw IllegalStateException()
        }

    if (piece == null) {
      return null
    }

    val nextGameInfo = piece.tryMove(gameInfo, from, to, color) ?: return null
    val checkInfoForCurrentPlayer = checkInfoForCurrentPlayer(nextGameInfo)

    if (isInCheck(checkInfoForCurrentPlayer, nextGameInfo)) return null

    val turnState =
        when (nextTurnState) {
          NextTurnState.WHITE_TO_PLAY -> NextTurnState.BLACK_TO_PLAY
          NextTurnState.BLACK_TO_PLAY -> NextTurnState.WHITE_TO_PLAY
          else -> throw IllegalStateException()
        }

    return (nextGameInfo to turnState)
  }

  private fun checkInfoForCurrentPlayer(gameInfo: GameInfo) =
      if (nextTurnState == NextTurnState.WHITE_TO_PLAY) {
        CheckInfo(gameInfo.blackPieces, gameInfo.whitePieces, Color.BLACK)
      } else {
        CheckInfo(gameInfo.whitePieces, gameInfo.blackPieces, Color.WHITE)
      }

  private fun isInCheck(checkInfo: CheckInfo, gameInfo: GameInfo): Boolean {
    val kingToCheckForDanger =
        checkInfo.piecesToFindKingIn.asSequence().find { (_, piece) -> piece is King }?.key
            ?: throw IllegalStateException()
    if (checkInfo.piecesToCheckForKingAggression.any { (coord, piece) ->
      piece.tryMove(gameInfo, coord, kingToCheckForDanger, checkInfo.colorToAggressAs) != null
    }) {
      return true
    }
    return false
  }
}
