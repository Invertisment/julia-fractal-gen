package mm.bitmap.gen


/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
trait CoordinatePixelGenerator {
  def generate(imageWidth: Int, imageHeight: Int, boundaryPoints: (Point, Point)): Either[Array[Int], String]
}


