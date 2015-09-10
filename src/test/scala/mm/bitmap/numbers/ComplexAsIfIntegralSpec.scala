package mm.bitmap.numbers

import org.apache.commons.math3.complex.Complex
import org.scalatest.{FlatSpec, Matchers}
import ComplexAsIfIntegral.complexIntegral

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-10
 */
class ComplexAsIfIntegralSpec extends FlatSpec with Matchers {

  abstract class ComplexHelper(real: Double, imaginary: Double = 0) {
    val complex = new Complex(real, imaginary)
  }

  "quot" should "divide" in new ComplexHelper(5, 9) {
    val operand = new Complex(2, 2)
    val expected = complex.divide(operand)
    val real = complexIntegral.quot(complex, operand)
    real shouldBe expected
  }

  "rem" should "calculate reminder" in new ComplexHelper(5, 9) {
    val operand = new Complex(2, 2)
    val expected = new Complex(complex.getReal % operand.getReal, complex.getImaginary % operand.getImaginary)
    val real = complexIntegral.rem(complex, operand)
    real shouldBe expected
  }

  "toDouble" should "get real part" in new ComplexHelper(5, 9) {
    val expected = complex.getReal
    val real = complexIntegral.toDouble(complex)
    real shouldBe expected
  }

  // Other methods are not tested.
}
