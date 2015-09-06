package mm.bitmap.gen

import mm.bitmap.counters.Counter

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
class RealImageGenerator(counter: Counter) extends ImageGenerator {
  override def generate(imageWidth: Int, imageHeight: Int, pxToCoord: (Int) => Int): Array[Int] = {
    val pixelCount = imageWidth * imageHeight
    val pixels = Array.ofDim[Int](pixelCount)
    for (xPos <- pixels.indices)
      pixels(xPos) = counter.getMax(pxToCoord.apply(xPos))
    pixels
  }
}
