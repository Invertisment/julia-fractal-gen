package mm.bitmap.numbers

import org.apache.commons.math3.complex.Complex

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-10
 */
class ComplexAsIfIntegral extends Integral[Complex] {
  override def quot(x: Complex, y: Complex): Complex = x.divide(y)

  override def rem(x: Complex, y: Complex): Complex = new Complex(x.getReal % y.getReal, x.getImaginary % y.getImaginary)

  override def toDouble(x: Complex): Double = x.getReal

  override def plus(x: Complex, y: Complex): Complex = x.add(y)

  override def toFloat(x: Complex): Float = x.getReal.toFloat

  override def toInt(x: Complex): Int = x.getReal.toInt

  override def negate(x: Complex): Complex = x.negate()

  override def fromInt(x: Int): Complex = new Complex(x)

  override def toLong(x: Complex): Long = x.getReal.toLong

  override def times(x: Complex, y: Complex): Complex = x.multiply(y)

  override def minus(x: Complex, y: Complex): Complex = x.subtract(y)

  override def compare(x: Complex, y: Complex): Int = x.abs().compareTo(y.abs())
}

object ComplexAsIfIntegral {
  implicit val complexIntegral = new ComplexAsIfIntegral
}
