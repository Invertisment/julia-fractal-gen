package mm.bitmap.gen

import java.util.concurrent.CountDownLatch

import mm.bitmap.counters.Counter
import mm.bitmap.numbers.ComplexAsIfIntegral.complexIntegral
import org.apache.commons.math3.complex.Complex

import scala.collection.immutable.NumericRange
import scala.concurrent.{ExecutionContext, Future}
import scala.math.Numeric.DoubleAsIfIntegral
import scala.util.Try

/**
 * sifjdfg
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
class RealPixelGenerator(counter: Counter)(implicit val executionContext: ExecutionContext) extends PixelGenerator {
  override def generate(count: Int, pxToCoord: Iterator[Complex]): Array[Int] =
    pxToCoord.map((complex: Complex) => counter.getMax(complex)).toArray

  override def generate(width: Int, height: Int, coordBounds: (Point, Point)): Array[Int] = {
    val minFirst = getMin(coordBounds)
    val maxFirst = getMax(coordBounds)
    val stepFirst = (maxFirst.getReal - minFirst.getReal) / width
    val firsts = NumericRange(minFirst.getReal, maxFirst.getReal, stepFirst)(DoubleAsIfIntegral).toIterator
    val masterArray = Array.ofDim[Int](width * height)
    val latch = new CancellableCountDownLatch(width)
    0 until width foreach ((horizontalPosition: Int) => {
      if (latch.isAborted)
        println("line " + horizontalPosition + " start")
      val min = new Complex(firsts.next(), minFirst.getImaginary)
      val max = new Complex(min.getReal, maxFirst.getImaginary)
      val step = max.subtract(min).divide(height)
      val range = NumericRange(min, max, step).toIterator
      Future {
        val arr = generate(height, range)
        for (x <- arr.indices)
          masterArray(x * width + horizontalPosition) = arr(x)
        println("line " + horizontalPosition)
      }(executionContext)
        .onComplete((triedUnit: Try[Unit]) => {
        if (triedUnit.isFailure) {
          val thr = triedUnit.failed.get
          latch.abort(thr)
        }
        latch.countDown()
      })
    })
    latch.await()
    masterArray
  }

  private def getMin(points: (Point, Point)): Complex = points._1.minCoord(points._2).toComplexPlane

  private def getMax(points: (Point, Point)): Complex = points._1.maxCoord(points._2).toComplexPlane

}
