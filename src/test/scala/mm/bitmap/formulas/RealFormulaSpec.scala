package mm.bitmap.formulas

import org.apache.commons.math3.complex.Complex
import org.scalatest._

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
class RealFormulaSpec extends FlatSpec with Matchers {

  val precision = 1E-15

  it should "return z^2 if c=0" in {
    val initValue = new Complex(1, 1) // (1 + i) ^ 2 = 2 * i
    val formula = new RealFormula(new Complex(0))
    val output = formula.countNext(initValue)
    output.getReal.abs should be < precision
    output.getImaginary.abs shouldEqual 2.0
  }

  it should "return z^2 + c" in {
    val initValue = new Complex(1, 1) // (1 + i) ^ 2 = 2 * i
    val formula = new RealFormula(new Complex(17, -1))
    val output = formula.countNext(initValue)
    (output.getReal - 17).abs should be < precision
    output.getImaginary.abs shouldEqual 1.0
  }

  it should "use given power" in {
    (2 to 4).foreach((power: Int) => {
      val input = new Complex(-0.3, -18.2)
      val initValue = new Complex(2, 4)
      val power = -1
      val formula = new RealFormula(initValue, power)
      val output = formula.countNext(input)
      output shouldEqual (input pow power add initValue)
    })

  }
}
