package mm.bitmap.formulas

import org.apache.commons.math3.complex.Complex

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
class RealFormula(cValue: Complex, power: Double = 2) extends Formula {
  override def countNext(z: Complex): Complex = z pow power add cValue
}
