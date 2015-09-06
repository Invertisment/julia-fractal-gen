package mm.bitmap.gen

import mm.bitmap.counters.Counter
import org.scalatest.{FlatSpec, Matchers}

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-06
 */
class RealImageGeneratorSpec extends FlatSpec with Matchers {

  "RealImage" should "create desired sise image" in {
    val desiredPixelColor = 12345
    val gen = new RealImageGenerator(new Counter {
      override def getMax(int: Int): Int = desiredPixelColor
    })
    for (x <- 1 to 3) {
      val y = 2 + x
      val img = gen.generate(x, y, (i: Int) => i)
      img.length shouldBe x * y
    }
  }

  it should "return correct pixel" in {
    val pixel: Int = (Math.random() * 1000).toInt
    val gen = new RealImageGenerator(new Counter {
      override def getMax(int: Int): Int = pixel
    })
    val img = gen.generate(1, 1, (i: Int) => i)
    img.length shouldBe 1
    img(0) shouldBe pixel
  }

  it should "call method with right argument" in {
    val pixel = 0
    var called = false
    val gen = new RealImageGenerator(new Counter {
      override def getMax(int: Int): Int = {
        called = true
        int shouldBe pixel
        -1
      }
    })
    gen.generate(1, 1, (i: Int) => i)
    called shouldBe true
  }

  it should "call method with right argument twice" in {
    val pixel = 4884921 // Random test value
    var called = 0
    val twice = 2
    val gen = new RealImageGenerator(new Counter {
      override def getMax(int: Int): Int = {
        called += 1
        int shouldBe pixel
        -1
      }
    })
    gen.generate(1, twice, (i: Int) => {
      i shouldBe called
      pixel
    })
    called shouldBe twice
  }
}
