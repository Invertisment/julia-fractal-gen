package mm.bitmap.gen

import org.scalatest.{Matchers, FlatSpec}

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-09
 */
class RealCoordinatePixelGeneratorSpec extends FlatSpec with Matchers {

  "Generator" should "give exact size array" in {
    val intCount = 3
    val gen = new RealCoordinatePixelGenerator(new PixelGenerator {
      override def generate(imageWidth: Int, imageHeight: Int, pxToCoord: (Int) => Int): Array[Int] = Array.ofDim(intCount)
    })
    val points = (Point(0, 2), Point(1, 0))
    val width = 3
    val height = 7
    val arrayOrMessage = gen.generate(width, height, points)
    arrayOrMessage.isLeft shouldBe true
    arrayOrMessage.fold((ints: Array[Int]) =>
      ints.length shouldEqual intCount,
      (s: String) => fail())
  }

  it should "fail on bad point values" in {
    val gen = new RealCoordinatePixelGenerator(new PixelGenerator {
      override def generate(imageWidth: Int, imageHeight: Int, pxToCoord: (Int) => Int): Array[Int] = Array.ofDim(0)
    })
    val points = (Point(0, 0), Point(1, 0))
    val width = 3
    val height = 7
    val arrayOrMessage = gen.generate(width, height, points)
    arrayOrMessage.isRight shouldBe true
    arrayOrMessage.fold((ints: Array[Int]) =>
      fail(),
      (s: String) =>
        s shouldEqual "Points meet on same coordinate value."
    )
  }

  it should "apply a correct stepping function" in {
    val arr = Array[Int](111154, 26)
    val func: (Int => Int) = 1 + _
    val gen = new RealCoordinatePixelGenerator(new PixelGenerator {
      override def generate(imageWidth: Int, imageHeight: Int, pxToCoord: (Int) => Int): Array[Int] = {
        for (x <- 1 to 5)
          pxToCoord(x) shouldEqual func(x)
        arr
      }
    })
    val points = (Point(0, 1), Point(1, 0))
    val width = 3
    val height = 7
    val arrayOrMessage = gen.generate(width, height, points)
    arrayOrMessage.isLeft shouldBe true
    arrayOrMessage.fold((ints: Array[Int]) => {
      ints shouldBe arr
      for (x <- arr.indices)
        ints(x) shouldBe arr(x)
    },
      (s: String) => fail())
  }

}
