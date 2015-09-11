package mm.bitmap.gen

import mm.bitmap.counters.Counter
import mm.bitmap.numbers.ComplexAsIfIntegral
import org.apache.commons.math3.complex.Complex

import scala.collection.immutable.NumericRange
import scala.math.Numeric.DoubleAsIfIntegral

/**
 * sifjdfg
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
class RealPixelGenerator(counter: Counter) extends PixelGenerator {
  override def generate(imageWidth: Int, imageHeight: Int, pxToCoord: Iterator[Complex]): Array[Int] =
    Array.ofDim[Int](imageWidth * imageHeight)
      .map((i: Int) => counter.getMax(pxToCoord.next()))

}
