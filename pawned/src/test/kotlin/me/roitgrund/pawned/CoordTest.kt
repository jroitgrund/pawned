package me.roitgrund.pawned

import kotlin.test.*

internal class CoordTest {
  @Test
  fun coord_instantiation() {
    assertEquals(Coord('a', 1), Coord("a1"))
  }

  @Test
  fun coord_moving_like_bishop() {
    assertNull(Coord("a1").squaresToMovingLikeBishop(Coord("a1")))
    assertNull(Coord("a1").squaresToMovingLikeBishop(Coord("a2")))
    assertEquals(
        listOf(Coord("c3"), Coord("d4")),
        Coord("b2").squaresToMovingLikeBishop(Coord("e5"))?.toList())
    assertEquals(
        listOf(Coord("c4"), Coord("d3")),
        Coord("b5").squaresToMovingLikeBishop(Coord("e2"))?.toList())
    assertEquals(
        listOf(Coord("d3"), Coord("c4")),
        Coord("e2").squaresToMovingLikeBishop(Coord("b5"))?.toList())
    assertEquals(
        listOf(Coord("d4"), Coord("c3")),
        Coord("e5").squaresToMovingLikeBishop(Coord("b2"))?.toList())
  }

  @Test
  fun coord_moving_like_rook() {
    assertNull(Coord("a1").squaresToMovingLikeRook(Coord("a1")))
    assertNull(Coord("a1").squaresToMovingLikeRook(Coord("b2")))
    assertEquals(
        listOf(Coord("b3"), Coord("b4")),
        Coord("b2").squaresToMovingLikeRook(Coord("b5"))?.toList())
    assertEquals(
        listOf(Coord("c2"), Coord("d2")),
        Coord("b2").squaresToMovingLikeRook(Coord("e2"))?.toList())
    assertEquals(
        listOf(Coord("b4"), Coord("b3")),
        Coord("b5").squaresToMovingLikeRook(Coord("b2"))?.toList())
    assertEquals(
        listOf(Coord("d2"), Coord("c2")),
        Coord("e2").squaresToMovingLikeRook(Coord("b2"))?.toList())
  }
}
