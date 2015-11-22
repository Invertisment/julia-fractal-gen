package mm.bitmap.gen

import mm.bitmap.counters.Counter
import mm.bitmap.numbers.ComplexAsIfIntegral.complexIntegral
import org.apache.commons.math3.complex.Complex

import scala.collection.immutable.NumericRange
import scala.concurrent.ExecutionContext
import scala.math.Numeric.DoubleAsIfIntegral

/**
 * sifjdfg
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
class RealPixelGenerator(counter: Counter)(implicit val executionContext: ExecutionContext) extends PixelGenerator {
  override def generate(pxToCoord: Iterator[Complex]): Array[Int] =
    pxToCoord.map((complex: Complex) => counter.getMax(complex)).toArray

  override def generate(width: Int, height: Int, coordBounds: (Point, Point)): Option[Array[Int]] = {
    val minPoint = getMin(coordBounds)
    val maxPoint = getMax(coordBounds)
    val stepFirst = (minPoint.getImaginary - maxPoint.getImaginary) / height
    val firsts = NumericRange(maxPoint.getImaginary, minPoint.getImaginary, stepFirst)(DoubleAsIfIntegral).toIterator
    val stream = (0 until height).map((verticalPosition: Int) => {
      val min = new Complex(minPoint.getReal, firsts.next())
      val max = new Complex(maxPoint.getReal, min.getImaginary)
      val step = max.subtract(min).divide(width)
      (verticalPosition, NumericRange(min, max, step).take(width))
    })

    val masterArray = Array.ofDim[Int](width * height)
    generate(stream)
      .foreach((item: (Int, Array[Int])) => {
        val verticalIndex = item._1 * width
        for (zipped <- item._2.zipWithIndex)
          masterArray(zipped._2 + verticalIndex) = zipped._1
      })
    Some(masterArray)
  }

  override def generate(data: Iterable[(Int, NumericRange[Complex])]): Iterator[(Int, Array[Int])] = {
    data.map(item => {
      (item._1, generate(item._2.toIterator))
    }).iterator
  }

  private def getMin(points: (Point, Point)): Complex = points._1.minCoord(points._2).toComplexPlane

  private def getMax(points: (Point, Point)): Complex = points._1.maxCoord(points._2).toComplexPlane

}
