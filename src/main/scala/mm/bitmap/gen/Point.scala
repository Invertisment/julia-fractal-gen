package mm.bitmap.gen

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-09
 */
case class Point(x: Int, y: Int) {
  def meets(point: Point): Boolean = point.x == x || point.y == y

  def maxCoord(point: Point): Point = Point(
    Math.max(x, point.x),
    Math.max(y, point.y))

  def minCoord(point: Point): Point = Point(
    Math.min(x, point.x),
    Math.min(y, point.y))

  def accumulate(accX: Int, accY: Int): Point = new Point(x + accX, y + accY)

}
