package me.roitgrund.pawned

internal data class GameInfo(
    val whitePieces: Map<Coord, Piece>,
    val blackPieces: Map<Coord, Piece>,
    val enPassant: Coord? = null
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
