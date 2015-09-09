package mm.bitmap.gen

import org.scalatest.{Matchers, FlatSpec}

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-09
 */
class PointSpec extends FlatSpec with Matchers {

  "Point" should "not meet point with same coordinate" in {
    val pointA = Point(1, 0)
    val pointB = Point(0, 1)
    pointA.meets(pointB) shouldEqual false
  }

  it should "meet point with same coordinate" in {
    val pointA = Point(0, 0)
    val pointB = Point(0, 1)
    pointA.meets(pointB) shouldEqual true
  }

  it should "meet point with same abscess" in {
    val pointA = Point(0, 0)
    val pointB = Point(1, 0)
    pointA.meets(pointB) shouldEqual true
  }

  it should "meet point with both equal coordinates" in {
    val pointA = Point(0, 1)
    val pointB = Point(0, 1)
    pointA.meets(pointB) shouldEqual true
  }

  "Point.maxCoord" should "give max coordinates" in {
    val pointA = Point(9, 1)
    val pointB = Point(0, 2)
    pointA.maxCoord(pointB) shouldEqual Point(9, 2)
  }

  "Point.minCoord" should "give max coordinates" in {
    val pointA = Point(9, 1)
    val pointB = Point(0, 2)
    pointA.minCoord(pointB) shouldEqual Point(0, 1)
  }

}
