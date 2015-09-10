//package mm.bitmap.gen
//
//import mm.bitmap.counters.Counter
//
//import scala.collection.immutable.NumericRange
//
///**
// * sifjdfg
// * @author Martynas Maciulevičius.
// * @version 1.0 2015-09-05
// */
//class RealPixelGenerator(counter: Counter) extends PixelGenerator {
//  override def generate(imageWidth: Int, imageHeight: Int, pxToCoord: NumericRange[Double]): Array[Int] = {
//    val pixelCount = imageWidth * imageHeight
//    val pixels = Array.ofDim[Int](pixelCount)
//    for (xPos <- pixels.indices)
//      pixels(xPos) = counter.getMax(pxToCoord.apply(xPos).floor.toInt)
//    pixels
//  }
//}
