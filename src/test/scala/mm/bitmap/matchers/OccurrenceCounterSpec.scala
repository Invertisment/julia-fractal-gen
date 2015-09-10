package mm.bitmap.matchers

import mm.bitmap.counters.OccurrenceCounter
import mm.bitmap.formulas.Formula
import org.apache.commons.math3.complex.Complex
import org.scalatest._

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
class OccurrenceCounterSpec extends FlatSpec with Matchers {

  "OccurrenceCounter" should "return zero if formula stays constant" in {
    val smallVal = new Complex(152)
    val counter = new OccurrenceCounter(new Formula {
      override def countNext(z: Complex): Complex = smallVal
    })
    val output = counter.getMax(new Complex(100))
    output shouldBe 0
  }

  it should "return max value from big Formula value" in {
    val counter = new OccurrenceCounter(new Formula {
      val range = 1 until 5 iterator

      override def countNext(z: Complex): Complex = {
        val item = if (range.hasNext) range.next() else 0
        new Complex(item)
      }
    })
    val bigVal = 0
    val output = counter.getMax(new Complex(bigVal))
    output shouldBe 4
  }

  it should "iterate exact amount of times" in {
    val maxValueExclusive = 5
    val range = 1 to maxValueExclusive iterator
    val counter = new OccurrenceCounter(new Formula {

      override def countNext(z: Complex): Complex = {
        new Complex(if (range.hasNext) range.next() else fail())
      }
    }, maxValueExclusive)
    val bigVal = 0
    val output = counter.getMax(new Complex(bigVal))
    output shouldBe maxValueExclusive
    range.hasNext shouldBe false
  }

}
