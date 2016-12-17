package mm.bitmap

import java.io.File

/**
  * @author Martynas Maciulevičius.
  * @version 1.0 2015-10-15
  */

case class Config(
                   cReal: Double = 0,
                   cImaginary: Double = 1,
                   imageWidth: Int = 1280,
                   imageHeight: Int = 960,
                   realCoordinateFrom: Double = -0.001,
                   realCoordinateTo: Double = 0.001,
                   imaginaryCoordinateFrom: Double = -0.001,
                   imaginaryCoordinateTo: Double = 0.001,
                   threadPoolSize: Int = Math.max(Runtime.getRuntime.availableProcessors() - 1, 1),
                   outputFile: File = new File("image.bmp"),
                   formulaPower: Double = 2,
                   // verbose: Boolean = false, // not used
                   color: Int = 0xcc11ee,
                   runOverMpj: Boolean = false,
                   dryRun: Boolean = false
                 )

