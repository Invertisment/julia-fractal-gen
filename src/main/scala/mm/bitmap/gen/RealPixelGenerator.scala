package mm.bitmap.gen

import mm.bitmap.counters.Counter
import mm.bitmap.numbers.ComplexAsIfIntegral.complexIntegral
import org.apache.commons.math3.complex.Complex

import scala.collection.immutable.NumericRange

/**
 * sifjdfg
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
class RealPixelGenerator(counter: Counter) extends PixelGenerator {
  override def generate(count: Int, pxToCoord: Iterator[Complex]): Array[Int] =
    Array.ofDim[Int](count)
      .map((i: Int) => counter.getMax(pxToCoord.next()))

  override def generate(width: Int, height: Int, coordBounds: (Point, Point)): Array[Int] = {
    val minFirst = getMin(coordBounds)
    val maxFirst = getMax(coordBounds)
    val stepFirst = new Complex(maxFirst.getReal - minFirst.getReal, minFirst.getImaginary).divide(width)
    val firsts = NumericRange(minFirst, maxFirst, stepFirst).toIterator
    val masterArray = Array.ofDim[Int](width * height)
    0 until width foreach ((horizontalPosition: Int) => {
      val min = firsts.next()
      val max = new Complex(min.getReal, maxFirst.getImaginary)
      val step = max.subtract(min).divide(height)
      val range = NumericRange(min, max, step).toIterator
      val arr = generate(height, range)
      arr.copyToArray(masterArray, height * horizontalPosition)
    })
    masterArray
  }

  private def getMin(points: (Point, Point)): Complex = points._1.minCoord(points._2).toComplexPlane

  private def getMax(points: (Point, Point)): Complex = points._1.maxCoord(points._2).toComplexPlane

}
