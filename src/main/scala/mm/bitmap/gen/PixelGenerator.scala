package mm.bitmap.gen

import org.apache.commons.math3.complex.Complex

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
trait PixelGenerator {
  def generate(count: Int, pxToCoord: Iterator[Complex]): Array[Int]

  def generate(width: Int, height: Int, coordBounds: (Point, Point)): Array[Int]
}
