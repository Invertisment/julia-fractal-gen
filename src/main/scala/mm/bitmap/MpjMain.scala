//package mm.bitmap
//
//import mm.bitmap.Main.Config
//import mpi.MPI
//
///**
// * @author Martynas Maciulevičius.
// * @version 1.0 2015-10-08
// */
//class MpjMain {
//
//  def mpjMain(config: Config): Unit = {
//    try {
//      MPI.Init(Array())
//      mpjWrappedMain(config)
//    } catch {
//      case e: RuntimeException =>
//        e.printStackTrace(Console.err)
//    }
//    finally
//      MPI.Finalize()
//  }
//
//  def mpjWrappedMain(config: Config): Unit = {
//    println("Size: " + MPI.COMM_WORLD.Size())
//    println("Rank: " + MPI.COMM_WORLD.Rank())
//  }
//}
