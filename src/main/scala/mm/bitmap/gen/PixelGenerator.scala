package mm.bitmap.gen

import org.apache.commons.math3.complex.Complex

import scala.collection.immutable.NumericRange

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
trait PixelGenerator {
  def generate(pxToCoord: Iterator[Complex]): Array[Int]

  def generate(width: Int, height: Int, coordBounds: (Point, Point)): Option[Array[Int]]

  def generate(data: Iterable[(Int, NumericRange[Complex])]): Iterator[(Int, Array[Int])]
}
