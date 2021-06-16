package me.roitgrund.pawned

class Game {
  var nextTurnState = NextTurnState.WHITE_TO_PLAY
    private set

  private var gameInfo = GameInfo()

  fun attemptMove(from: Coord, to: Coord): Boolean {
    val (piece, color) =
        when (nextTurnState) {
          NextTurnState.WHITE_TO_PLAY -> gameInfo.whitePieces[from] to Color.WHITE
          NextTurnState.BLACK_TO_PLAY -> gameInfo.blackPieces[from] to Color.BLACK
          else -> null
        }
            ?: return false

    if (piece == null) {
      return false
    }

    val newGameInfo = piece.tryMove(gameInfo, from, to, color) ?: return false

    // TODO(return false if king in danger)

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

data class Coord(val file: Char, val rank: Int) {
  init {
    require(file in 'a'..'h' && rank in 1..8)
  }

  constructor(coord: String) : this(coord[0], coord[1].digitToInt()) {
    require(coord.length == 2)
  }
}

enum class NextTurnState {
  WHITE_TO_PLAY,
  BLACK_TO_PLAY,
  WHITE_WON,
  BLACK_WON,
  STALEMATE
}

internal enum class Color {
  BLACK,
  WHITE
}

internal interface Piece {
  fun tryMove(gameInfo: GameInfo, from: Coord, to: Coord, color: Color): GameInfo?
}

internal class Pawn : Piece {
  override fun tryMove(gameInfo: GameInfo, from: Coord, to: Coord, color: Color): GameInfo? {
    val (whitePieces, blackPieces) =
        when (color) {
          Color.WHITE ->
              Pair(gameInfo.whitePieces - from + Pair(to, this), gameInfo.blackPieces - to)
          Color.BLACK ->
              Pair(gameInfo.blackPieces - from + Pair(to, this), gameInfo.whitePieces - to)
        }

    return GameInfo(whitePieces, blackPieces)
  }
}

internal class Rook : Piece {
  override fun tryMove(gameInfo: GameInfo, from: Coord, to: Coord, color: Color): GameInfo? {
    TODO("Not yet implemented")
  }
}

internal class Knight : Piece {
  override fun tryMove(gameInfo: GameInfo, from: Coord, to: Coord, color: Color): GameInfo? {
    TODO("Not yet implemented")
  }
}

internal class Bishop : Piece {
  override fun tryMove(gameInfo: GameInfo, from: Coord, to: Coord, color: Color): GameInfo? {
    TODO("Not yet implemented")
  }
}

internal class King : Piece {
  override fun tryMove(gameInfo: GameInfo, from: Coord, to: Coord, color: Color): GameInfo? {
    TODO("Not yet implemented")
  }
}

internal class Queen : Piece {
  override fun tryMove(gameInfo: GameInfo, from: Coord, to: Coord, color: Color): GameInfo? {
    TODO("Not yet implemented")
  }
}

internal data class GameInfo(
    val whitePieces: Map<Coord, Piece>,
    val blackPieces: Map<Coord, Piece>
) {
  constructor() :
      this(
  mapOf(
      Coord("a1") to Rook(),
      Coord("b1") to Bishop(),
      Coord("c1") to Knight(),
      Coord("d1") to King(),
      Coord("e1") to Queen(),
      Coord("f1") to Bishop(),
      Coord("g1") to Knight(),
      Coord("h1") to Rook(),
      Coord("a2") to Pawn(),
      Coord("b2") to Pawn(),
      Coord("c2") to Pawn(),
      Coord("d2") to Pawn(),
      Coord("e2") to Pawn(),
      Coord("f2") to Pawn(),
      Coord("g2") to Pawn(),
      Coord("h2") to Pawn()),
  mapOf(
      Coord("a8") to Rook(),
      Coord("b8") to Bishop(),
      Coord("c8") to Knight(),
      Coord("d8") to King(),
      Coord("e8") to Queen(),
      Coord("f8") to Bishop(),
      Coord("g8") to Knight(),
      Coord("h8") to Rook(),
      Coord("a7") to Pawn(),
      Coord("b7") to Pawn(),
      Coord("c7") to Pawn(),
      Coord("d7") to Pawn(),
      Coord("e7") to Pawn(),
      Coord("f7") to Pawn(),
      Coord("g7") to Pawn(),
      Coord("h7") to Pawn()))
}
