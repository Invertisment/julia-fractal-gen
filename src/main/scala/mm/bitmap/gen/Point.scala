package mm.bitmap.gen

import org.apache.commons.math3.complex.Complex

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-09
 */
case class Point(x: Double, y: Double) {
  def meets(point: Point): Boolean = point.x == x || point.y == y

  def maxCoord(point: Point): Point = Point(
    Math.max(x, point.x),
    Math.max(y, point.y))

  def minCoord(point: Point): Point = Point(
    Math.min(x, point.x),
    Math.min(y, point.y))

  def accumulate(accX: Double, accY: Double): Point = Point(x + accX, y + accY)

  def toComplexPlane: Complex = new Complex(x, y)
}
