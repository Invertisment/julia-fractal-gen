package mm.bitmap.gen

import scala.collection.immutable.NumericRange

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
class RealCoordinatePixelGenerator(pixelGenerator: PixelGenerator) extends CoordinatePixelGenerator {
  override def generate(imageWidth: Int, imageHeight: Int, boundaryPoints: (Point, Point)): Either[Array[Int], String] = {
    if (boundaryPoints._1.meets(boundaryPoints._2))
      Right("Points meet on same coordinate value.")
    else {
      //      val minPoint = boundaryPoints._1.minCoord(boundaryPoints._2)
      val values: NumericRange[Double] = 1.0 until 10 by 1
      //      Left(pixelGenerator.generate(imageWidth, imageHeight, values))
      Left(Array())
    }
  }
}
