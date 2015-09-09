package mm.bitmap.gen

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
class RealCoordinatePixelGenerator(pixelGenerator: PixelGenerator) extends CoordinatePixelGenerator {
  override def generate(imageWidth: Int, imageHeight: Int, boundaryPoints: (Point, Point)): Either[Array[Int], String] = {
    if (boundaryPoints._1.meets(boundaryPoints._2))
      Right("Points meet on same coordinate value.")
    else {
      val array = pixelGenerator.generate(imageWidth, imageHeight, 1 + _)
      Left(array)
    }
  }
}
