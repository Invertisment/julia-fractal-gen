package mm.bitmap.gen

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-09
 */
case class Point(x: Int, y: Int) {
  def meets(point: Point): Boolean = point.x == x || point.y == y

}
