package mm.bitmap.gen

import mm.bitmap.counters.Counter
import org.apache.commons.math3.complex.Complex
import org.scalatest.{FlatSpec, Matchers}
import mm.bitmap.numbers.ComplexAsIfIntegral.complexIntegral
import scala.collection.immutable.NumericRange

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-06
 */
class RealPixelGeneratorSpec extends FlatSpec with Matchers {

  "RealImage" should "create desired size image" in {
    val desiredPixelColor = 12345
    val gen = new RealPixelGenerator(new Counter {
      override def getMax(int: Complex): Int = desiredPixelColor
    })
    for (x <- 1 to 3) {
      val y = 2 + x
      val img = gen.generate(x, y, NumericRange[Complex](new Complex(0, 0), new Complex(Int.MaxValue, Int.MaxValue), new Complex(1, 1)).iterator)
      img.length shouldBe x * y
    }
  }

  it should "return correct pixel" in {
    val pixel: Int = (Math.random() * 1000).toInt
    val gen = new RealPixelGenerator(new Counter {
      override def getMax(int: Complex): Int = pixel
    })
    val img = gen.generate(1, 1, NumericRange[Complex](new Complex(0, 0), new Complex(Int.MaxValue, Int.MaxValue), new Complex(1, 1)).iterator)
    img.length shouldBe 1
    img(0) shouldBe pixel
  }

  it should "call method with right argument once" in {
    val pixel = new Complex(0, 0)
    var called = false
    val gen = new RealPixelGenerator(new Counter {
      val generator = NumericRange[Complex](new Complex(0, 0), new Complex(Int.MaxValue, Int.MaxValue), new Complex(1, 1)).iterator

      override def getMax(int: Complex): Int = {
        called shouldBe false
        called = true
        int shouldBe pixel
        -1
      }
    })
    val arr = gen.generate(1, 1, NumericRange[Complex](pixel, new Complex(Int.MaxValue, Int.MaxValue), new Complex(1, 1)).iterator)
    called shouldBe true
    arr.length shouldBe 1
    arr(0) shouldBe -1
  }

  it should "call method with right argument twice" in {
    val pixel = 4884921 // Random test value
    var called = 0
    val twice = 2
    val range = NumericRange[Complex](new Complex(0), new Complex(2, 0), new Complex(1, 0)).iterator
    val gen = new RealPixelGenerator(new Counter {
      override def getMax(int: Complex): Int = {
        called += 1
        int.abs() should be < 2.0
        int.abs() should be >= 0.0
        -1
      }
    })
    gen.generate(1, twice, range)
    called shouldBe twice
    range.hasNext shouldBe false
  }

}
