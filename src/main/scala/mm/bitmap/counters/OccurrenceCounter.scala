package mm.bitmap.counters

import mm.bitmap.formulas.Formula
import org.apache.commons.math3.complex.Complex

import scala.annotation.tailrec

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 * @param formula Formula to use when counting values
 */
class OccurrenceCounter(formula: Formula, overflowAfter: Int = 0xff) extends Counter {

  def getMax(seed: Complex): Int = getMax(seed, 0, 0)

  @tailrec
  private def getMax(seed: Complex, count: Int, iterCount: Int): Int = {
    val value = formula.countNext(seed)
    val newCount = if (seed.abs <= value.abs) count + 1 else count
    val nextIterCount = iterCount + 1
    if (nextIterCount < overflowAfter) getMax(value, newCount, nextIterCount) else newCount
  }

}
