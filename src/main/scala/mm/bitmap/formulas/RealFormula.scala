package mm.bitmap.formulas

import org.apache.commons.math3.complex.Complex

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
class RealFormula(cValue: Complex) extends Formula {
  override def countNext(z: Complex): Complex = z pow 2 add cValue
}
