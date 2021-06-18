package me.roitgrund.pawned

import kotlin.math.abs

data class Coord(val file: Char, val rank: Int) {
  init {
    require(file in 'a'..'h' && rank in 1..8)
  }

  constructor(coord: String) : this(coord[0], coord[1].digitToInt()) {
    require(coord.length == 2)
  }

  fun squaresToMovingLikeRook(to: Coord): Sequence<Coord>? {
    val fileDelta =
        when {
          file == to.file -> 0
          file < to.file -> 1
          else -> -1
        }

    val rankDelta =
        when {
          rank == to.rank -> 0
          rank < to.rank -> 1
          else -> -1
        }

    if (rankDelta != 0 && fileDelta != 0 || rankDelta == 0 && fileDelta == 0) {
      return null
    }

    return generateSequence(this) { Coord(it.file + fileDelta, it.rank + rankDelta) }
        .takeWhile { it != to }
        .drop(1)
  }

  fun squaresToMovingLikeBishop(to: Coord): Sequence<Coord>? {
    if (this == to || abs(rank - to.rank) != abs(file.code - to.file.code)) {
      return null
    }

    val rankDelta = if (rank < to.rank) 1 else -1
    val fileDelta = if (file < to.file) 1 else -1

    return generateSequence(this) { Coord(it.file + fileDelta, it.rank + rankDelta) }
        .takeWhile { it != to }
        .drop(1)
  }
}
