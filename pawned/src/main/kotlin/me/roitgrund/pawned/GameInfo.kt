package me.roitgrund.pawned

internal data class GameInfo(
    val whitePieces: Map<Coord, Piece>,
    val blackPieces: Map<Coord, Piece>,
    val castlingInfo: CastlingInfo = CastlingInfo(),
    val enPassant: Coord? = null,
) {
  constructor() :
      this(
  mapOf(
      Coord("a1") to Rook(),
      Coord("b1") to Knight(),
      Coord("c1") to Bishop(),
      Coord("d1") to Queen(),
      Coord("e1") to King(),
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
      Coord("b8") to Knight(),
      Coord("c8") to Bishop(),
      Coord("d8") to Queen(),
      Coord("e8") to King(),
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

  val allPieceCoordinates: Set<Coord> by lazy { whitePieces.keys.union(blackPieces.keys) }
}
