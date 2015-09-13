package mm.bitmap.gen

import java.util.concurrent.Executors

import mm.bitmap.counters.Counter
import org.apache.commons.math3.complex.Complex
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import mm.bitmap.numbers.ComplexAsIfIntegral.complexIntegral
import scala.collection.immutable.NumericRange
import scala.concurrent.ExecutionContext

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-06
 */
class RealPixelGeneratorSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

  lazy val executor = Executors.newSingleThreadExecutor()
  implicit lazy val context = ExecutionContext.fromExecutor(executor)

  override protected def afterAll(): Unit = {
    executor.shutdown()
    super.afterAll()
  }

  "RealImage" should "create desired size image" in {
    val desiredPixelColor = 12345
    val gen = new RealPixelGenerator(new Counter {
      override def getMax(int: Complex): Int = desiredPixelColor
    })
    for (x <- 1 to 3) {
      val y = 2 + x
      val img = gen.generate(x * y, NumericRange[Complex](new Complex(0, 0), new Complex(Int.MaxValue, Int.MaxValue), new Complex(1, 1)).iterator)
      img.length shouldBe x * y
    }
  }

  it should "return correct pixel" in {
    val pixel: Int = (Math.random() * 1000).toInt
    val gen = new RealPixelGenerator(new Counter {
      override def getMax(int: Complex): Int = pixel
    })
    val img = gen.generate(1, NumericRange[Complex](new Complex(0, 0), new Complex(Int.MaxValue, Int.MaxValue), new Complex(1, 1)).iterator)
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
    val arr = gen.generate(1, NumericRange[Complex](pixel, new Complex(Int.MaxValue, Int.MaxValue), new Complex(1, 1)).iterator)
    called shouldBe true
    arr.length shouldBe 1
    arr(0) shouldBe -1
  }

  it should "call method with right argument twice" in {
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
    gen.generate(twice, range)
    called shouldBe twice
    range.hasNext shouldBe false
  }

  "generate with points" should "call other generate method" in {
    val generator = new RealPixelGenerator(new Counter {
      override def getMax(int: Complex): Int = -2
    }) {
      override def generate(count: Int, pxToCoord: Iterator[Complex]): Array[Int] = {
        count shouldBe 2
        Array(-1, -1)
      }
    }
    val arrayOut = generator.generate(1, 2, (Point(0, 0), Point(1, 1)))
    arrayOut.length shouldBe 2
    arrayOut.foreach((i: Int) => i shouldBe -1)
  }

  it should "split x axis into separate calls with correct iterators" in {
    val yValue = 2
    2 to 3 foreach ((length: Int) => {
      val numbers = (1 to yValue * length) iterator
      var iterSkip = 0
      val generator = new RealPixelGenerator(new Counter {
        override def getMax(int: Complex): Int = fail()
      }) {
        val min = new Complex(0, 0)
        val max = new Complex(length, 0)
        val step = max.divide(length)
        val firstValues = NumericRange(min, max, step).iterator

        override def generate(count: Int, pxToCoord: Iterator[Complex]): Array[Int] = {
          count shouldBe yValue
          val firstValue = firstValues.next()
          val secondValue = new Complex(firstValue.getReal, length / yValue.toDouble)
          val testValues = Seq(firstValue, secondValue).iterator
          testValues.foreach((complex: Complex) =>
            complex shouldEqual pxToCoord.next)
          pxToCoord.hasNext shouldBe false
          val ret = numbers.slice(iterSkip, iterSkip + count).toArray
          iterSkip += count
          ret
        }
      }
      val out = generator.generate(length, yValue, (Point(0, 0), Point(length, length)))
      iterSkip shouldEqual length * yValue
      out.length shouldBe length * yValue
      //      val numbers2 = (1 to yValue * length).map((index: Int) => (index + yValue) % yValue).iterator
      val numbers2 = (0 to yValue * length).map((i: Int) => i % length * yValue + i / length + 1).iterator
      out.foreach((i: Int) => i shouldEqual numbers2.next)
    })
  }

  it should "produce correct first values with negative input points" in {
    val yValue = 1
    val nullValue = -10
    9 to 9 foreach ((length: Int) => {
      val min = new Complex(nullValue, nullValue)
      val max = new Complex(length, nullValue)
      val step = max.subtract(min).divide(length)
      val firstValues = NumericRange(min, max, step).iterator
      val generator = new RealPixelGenerator(new Counter {
        override def getMax(int: Complex): Int = fail()
      }) {
        override def generate(count: Int, pxToCoord: Iterator[Complex]): Array[Int] = {
          count shouldBe yValue
          val next = firstValues.next()
          val next2 = pxToCoord.next()
          next shouldBe next2
          Array()
        }
      }
      generator.generate(length, yValue, (Point(nullValue, nullValue), Point(length, length)))
      firstValues.hasNext shouldBe false
    })
  }

  "Min value of the sequence" should "be negative if range is negative" in {
    val yValue = 1
    val nullValue = -10
    val length = 1
    val generator = new RealPixelGenerator(new Counter {
      override def getMax(int: Complex): Int = fail()
    }) {
      var isCalled = false

      override def generate(count: Int, pxToCoord: Iterator[Complex]): Array[Int] = {
        isCalled shouldBe false
        isCalled = true
        count shouldBe yValue
        val next2 = pxToCoord.next()
        next2 shouldBe new Complex(nullValue, nullValue)
        Array()
      }
    }
    generator.generate(length, yValue, (Point(nullValue, nullValue), Point(length, length)))
    generator.isCalled shouldBe true
  }

}
