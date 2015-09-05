package mm.bitmap.counters

import mm.bitmap.formulas.Formula

import scala.annotation.tailrec

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
class MaxIterCounter(formula: Formula) {

  def getMax(int: Int): Int = {
    getMax(int, 1)
  }

  @tailrec
  final def getMax(int: Int, depth: Int): Int = {
    val value = formula.countNext(int)
    if (int < value) getMax(value, depth) else depth // operator "<" is used because of infinite loop possibility because Formula may return the same value
  }

}
