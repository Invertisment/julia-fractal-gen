package mm.bitmap.counters

import mm.bitmap.formulas.Formula

import scala.annotation.tailrec

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
class MaxCounter(formula: Formula) {

  @tailrec
  final def getMax(int: Int): Int = {
    val value = formula.countNext(int)
    if (int < value) getMax(value) else int // operator "<" is used because of infinite loop possibility because Formula may return the same value
  }

}
