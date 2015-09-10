package mm.bitmap.formulas

import org.apache.commons.math3.complex.Complex

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
trait Formula {
  def countNext(z: Complex): Complex
}
