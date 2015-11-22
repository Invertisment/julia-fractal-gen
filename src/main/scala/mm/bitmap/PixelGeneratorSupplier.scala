package mm.bitmap

import mm.bitmap.gen.PixelGenerator

import scala.concurrent.ExecutionContext

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-10-19
 */
trait PixelGeneratorSupplier {
  def createGenerator(config: Config)(implicit executionContext: ExecutionContext): PixelGenerator
}
