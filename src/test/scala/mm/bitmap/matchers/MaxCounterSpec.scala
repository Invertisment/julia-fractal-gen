package mm.bitmap.matchers

import mm.bitmap.formulas.Formula
import mm.bitmap.counters.MaxCounter
import org.scalatest._

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
class MaxCounterSpec extends FlatSpec with Matchers {

  "MaxCounter" should "return max value from small Formula value" in {
    val smallVal = 152
    val counter = new MaxCounter(new Formula {
      override def countNext(z: Int): Int = smallVal
    })
    val output = counter.getMax(100)
    output shouldBe smallVal
  }

  it should "return max value from big Formula value" in {
    val counter = new MaxCounter(new Formula {
      override def countNext(z: Int): Int = 152
    })
    val bigVal = 1001
    val output = counter.getMax(bigVal)
    output shouldBe bigVal
  }

  it should "count custom value from formula" in {
    val limit = 3
    val counter = new MaxCounter(new Formula {
      override def countNext(z: Int): Int = {
        if (z == limit) limit else z + 1
      }
    })
    val output = counter.getMax(0)
    output shouldBe limit
  }

  it should "stop counting on same value" in {
    val limit = 3
    val counter = new MaxCounter(new Formula {
      var isExecuted = false

      override def countNext(z: Int): Int = {
        if (isExecuted) throw new AssertionError("Same value should not be recalculated.")
        isExecuted = true
        limit
      }
    })
    val output = counter.getMax(limit)
    output shouldBe limit
  }
}
