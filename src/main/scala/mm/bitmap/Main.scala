package mm.bitmap

import java.awt.image.{BufferedImage, DataBuffer, Raster}
import java.io.File
import java.util.concurrent.Executors
import javax.imageio.ImageIO

import mm.bitmap.counters.OccurrenceCounter
import mm.bitmap.formulas.{Formula, RealFormula}
import mm.bitmap.gen.{PixelGenerator, Point, RealPixelGenerator}
import org.apache.commons.math3.complex.Complex

import scala.concurrent.ExecutionContext

/**
  * @author Martynas Maciulevičius.
  * @version 1.0 2015-09-05
  */
object Main {

  def main(args: Array[String]) =
    run(parseArgs(args))

  /** Write to file */
  private def wtf(config: Config, image: BufferedImage): Unit = {
    val file = config.outputFile
    val bool = ImageIO.write(image, "bmp", file)
    println(if (bool) "successfully written an image" else "failed to write to file")
  }

  private def generateImage(config: Config, pixelGeneratorSupplier: PixelGeneratorSupplier): Option[BufferedImage] = {

    val points = (Point(config.realCoordinateFrom, config.imaginaryCoordinateFrom),
      Point(config.realCoordinateTo, config.imaginaryCoordinateTo))

    val image = new BufferedImage(
      config.imageWidth, config.imageHeight, BufferedImage.TYPE_INT_RGB)
    val raster = Raster.createPackedRaster(DataBuffer.TYPE_INT, config.imageWidth, config.imageHeight, 3, 8, null)
    val pool = Executors.newFixedThreadPool(config.threadPoolSize)
    val executorService = ExecutionContext.fromExecutor(pool)
    try {
      val out = pixelGeneratorSupplier.createGenerator(config)(executorService).generate(
        config.imageWidth, config.imageHeight, points)
      if (out.isEmpty)
        return None
      out.foreach((ints: Array[Int]) => {
        raster.setDataElements(
          0, 0,
          config.imageWidth, config.imageHeight,
          ints.map(_ * config.color))
      })
    } finally {
      // shut down the thread pool
      //      pool.awaitTermination(30, TimeUnit.SECONDS)
      pool.shutdown()
      println("pool shut down")
    }
    image.setData(raster)
    println("done counting")
    Some(image)
  }

  private[bitmap] def parseArgs(args: Array[String]): Option[Config] = {
    val config: Config = Config()

    new scopt.OptionParser[Config]("jar") {
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

      opt[Unit]('m', "mpj") action { case (unit, c) =>
        c.copy(runOverMpj = true)
      } valueName "<number>" text s"Run over mpjExpress?. Default: ${config.runOverMpj}."

      opt[Unit]("dry-run") action { case (unit, c) =>
        c.copy(dryRun = true)
      } valueName "<number>" text s"Only display options that were set. Default: ${config.dryRun}."

      arg[File]("filename") optional() action { (x, c) =>
        c.copy(outputFile = x)
      } text s"Filename for saving generated image. Default: ${config.outputFile}"

    }
      .parse(args, config)
  }

  private[bitmap] def run(config: Option[Config], generatorSupplier: PixelGeneratorSupplier = new DefaultPixelGeneratorSupplier): Unit =
    config match {
      case Some(cfg) =>
        if (cfg.dryRun)
          println("Config: " + cfg)
        else
          generateImage(cfg, generatorSupplier).foreach(image =>
            wtf(cfg, image))
      case None =>
    }

  private[bitmap] class DefaultPixelGeneratorSupplier extends PixelGeneratorSupplier {
    override def createGenerator(config: Config)(implicit executionContext: ExecutionContext): PixelGenerator = {
      new RealPixelGenerator(getOccurenceCounter(config))
    }
  }

  private[bitmap] def getOccurenceCounter(config: Config)(implicit executionContext: ExecutionContext): OccurrenceCounter =
    new OccurrenceCounter(
      getDefaultFormula(config))

  private[bitmap] def getDefaultFormula(config: Config): Formula = {
    val cValue = new Complex(config.cReal, config.cImaginary)
    new RealFormula(
      cValue, config.formulaPower)
  }

}
