package mm.bitmap.gen

import scala.collection.immutable.NumericRange

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
trait PixelGenerator {
  def generate(imageWidth: Int, imageHeight: Int, pxToCoord: NumericRange[Double]): Array[Int]
}
