package mm.bitmap.gen

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
trait ImageGenerator {
  def generate(imageWidth: Int, imageHeight: Int, pxToCoord: (Int) => Int): Array[Int]
}
