package mm.bitmap

import java.awt.image.{Raster, DataBuffer, BufferedImage}
import java.io.File
import java.util.concurrent.Executors
import javax.imageio.ImageIO

import mm.bitmap.counters.OccurrenceCounter
import mm.bitmap.formulas.RealFormula
import mm.bitmap.gen.{Point, RealPixelGenerator}
import org.apache.commons.math3.complex.Complex
import scopt.Read

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
object Main {

  def main(args: Array[String]) = {
    parseArgs(args)
    //    wtf("image.bmp", generateImage())
  }

  /** Write to file */
  private def wtf(config: Config, image: BufferedImage): Unit = {
    val file = config.outputFile
    val bool = ImageIO.write(image, "bmp", file)
    println(if (bool) "successfully written an image" else "failed to write to file")
  }

  private def generateImage(config: Config): BufferedImage = {

    val points = (Point(config.realCoordinateFrom, config.imaginaryCoordinateFrom),
      Point(config.realCoordinateTo, config.imaginaryCoordinateTo))
    val cValue = new Complex(config.cReal, config.cImaginary)

    val image = new BufferedImage(
      config.imageWidth, config.imageHeight, BufferedImage.TYPE_INT_RGB)
    val executor = Executors.newFixedThreadPool(config.threadPoolSize)
    try {
      val raster = Raster.createPackedRaster(DataBuffer.TYPE_INT, config.imageWidth, config.imageHeight, 3, 8, null)
      raster.setDataElements(0, 0, config.imageWidth, config.imageHeight,
        new RealPixelGenerator(
          new OccurrenceCounter(
            new RealFormula(
              cValue, config.formulaPower)))(ExecutionContext.fromExecutor(executor)).generate(
            config.imageWidth, config.imageHeight, points).map((i: Int) => i * config.color))
      image.setData(raster)
      println("done counting")
    } finally {
      // shut down the thread pool
      executor.shutdown()
    }
    image
  }

  private case class Config(
                             cReal: Double = 0,
                             cImaginary: Double = 1,
                             imageWidth: Int = 1280,
                             imageHeight: Int = 960,
                             realCoordinateFrom: Double = -0.001,
                             realCoordinateTo: Double = 0.001,
                             imaginaryCoordinateFrom: Double = -0.001,
                             imaginaryCoordinateTo: Double = 0.001,
                             threadPoolSize: Int = Runtime.getRuntime.availableProcessors(),
                             outputFile: File = new File("image.bmp"),
                             formulaPower: Double = 2,
                             verbose: Boolean = false, // not used
                             color: Int = 0xaff587
                             )

  private def parseArgs(args: Array[String]): Unit = {
    val config: Config = Config()

    val parser = new scopt.OptionParser[Config]("jar") {
      head("Fractal generator", "0.1", "\nWritten by Martynas Maciulevičius, September of 2015\n")

      help("help") text "Show this text and exit"

      opt[Double]("c-re") action { case (re, c) =>
        c.copy(cReal = re)
      } valueName "<number>" text s"Real part of complex number. Default: ${config.cReal}"

      opt[Double]("c-im") action { case (im, c) =>
        c.copy(cImaginary = im)
      } valueName "<number>" text s"Imaginary part of complex number. Default: ${config.cImaginary}"

      opt[Double]('p', "pow") action { case (im, c) =>
        c.copy(formulaPower = im)
      } valueName "<number>" text s"Formula's z[1]^<number> + c. Default: ${config.formulaPower}"

      opt[Int]('w', "width") action { case (im, c) =>
        c.copy(imageWidth = im)
      } valueName "<number>" text s"Width of the generated image. Default: ${config.imageWidth}"

      opt[Int]('h', "height") action { case (im, c) =>
        c.copy(imageHeight = im)
      } valueName "<number>" text s"Height of the generated image. Default: ${config.imageHeight}"

      opt[Double]("re-from") action { case (im, c) =>
        c.copy(realCoordinateFrom = im)
      } valueName "<number>" text s"Real plotting axis starting point. Default: ${config.realCoordinateFrom}"

      opt[Double]("re-to") action { case (im, c) =>
        c.copy(realCoordinateTo = im)
      } valueName "<number>" text s"Real plotting axis ending point. Default: ${config.realCoordinateTo}"

      opt[Double]("im-from") action { case (im, c) =>
        c.copy(imaginaryCoordinateFrom = im)
      } valueName "<number>" text s"Imaginary plotting axis starting point. Default: ${config.imaginaryCoordinateFrom}"

      opt[Double]("im-to") action { case (im, c) =>
        c.copy(imaginaryCoordinateTo = im)
      } valueName "<number>" text s"Imaginary plotting axis ending point. Default: ${config.imaginaryCoordinateTo}"

      opt[Int]("color") optional() action { (x, c) =>
        c.copy(color = x)
      } text s"Adds some colors to the computed image. Works by multiplying gray color value with given number (Int). Gray image can be produced using value of ${0x10101}. Default: ${config.color}"

      opt[Int]('t', "threads") action { case (im, c) =>
        c.copy(threadPoolSize = im)
      } valueName "<number>" text s"Thread pool size for computations. Default: ${config.threadPoolSize} (Determined by your current processor)."

      arg[File]("filename") optional() action { (x, c) =>
        c.copy(outputFile = x)
      } text s"Filename for saving generated image. Default: ${config.outputFile}"

    }

    parser.parse(args, config) match {
      case Some(a) =>
        wtf(a, generateImage(a))
      case None =>
    }
  }
}
