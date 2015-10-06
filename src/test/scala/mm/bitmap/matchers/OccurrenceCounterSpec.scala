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

  "OccurrenceCounter" should "iterate exact amount of times" in {
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

  it should "return max value if formula return value is constant" in {
    val constantVal = new Complex(152)
    val maxIterCount = 5
    val counter = new OccurrenceCounter(new Formula {
      override def countNext(z: Complex): Complex = constantVal
    }, maxIterCount)
    val output = counter.getMax(new Complex(100))
    output shouldBe maxIterCount
  }

  it should "remove gaps from increasing values if there is any decreasing" in {
    val max = 5
    val range = 1 until max iterator
    val counter = new OccurrenceCounter(new Formula {

      override def countNext(z: Complex): Complex = {
        val item = if (range.hasNext) range.next() else 0
        new Complex(item)
      }
    }, range.length + 2)
    val bigVal = 0
    val output = counter.getMax(new Complex(bigVal))
    output shouldBe (max + 1)
  }

  "getMax" should "stop the calculation at given positive infinity" in {
    val myInfinity = 25
    val overflowAfter = 35
    val input = new Complex(0)
    // Single step iterator (We don't want to call it twice)
    val seq = Iterator(new Complex(myInfinity))
    val counter = new OccurrenceCounter(new Formula {

      override def countNext(z: Complex): Complex = {
        z shouldBe input
        seq.next()
      }

    }, overflowAfter, myInfinity)
    val out = counter.getMax(input) // Random input (should not be used)
    seq.hasNext shouldBe false
    out shouldEqual overflowAfter
  }

  "getMax" should "stop the calculation at given negative infinity" in {
    val myInfinity = 25
    val overflowAfter = 35
    val input = new Complex(0)
    // Single step iterator (We don't want to call it twice)
    val seq = Iterator(new Complex(myInfinity + 1))
    val counter = new OccurrenceCounter(new Formula {

      override def countNext(z: Complex): Complex = {
        z shouldBe input
        seq.next()
      }

    }, overflowAfter, myInfinity)
    val out = counter.getMax(input) // Random input (should not be used)
    seq.hasNext shouldBe false
    out shouldEqual overflowAfter
  }

}
