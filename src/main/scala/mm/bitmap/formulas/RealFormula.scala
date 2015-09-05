package mm.bitmap.formulas

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
class RealFormula(cValue: Int) extends Formula {
  override def countNext(z: Int): Int = z * z + cValue
}
