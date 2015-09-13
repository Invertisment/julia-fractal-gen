package mm.bitmap.gen

import java.util.concurrent.{TimeUnit, CountDownLatch}

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-13
 */
class CancellableCountDownLatch(count: Int) extends CountDownLatch(count) {
  private var abortionThrowable = Option.empty[Throwable]

  def abort(throwable: Throwable): Unit = {
    if (getCount == 0 || abortionThrowable.isDefined) return

    abortionThrowable = Option(throwable)
    while (getCount > 0)
      countDown()
  }

  def isAborted: Boolean = abortionThrowable.isDefined

  override def await(): Unit = {
    super.await()
    throwIfNeeded()
  }

  override def await(timeout: Long, unit: TimeUnit): Boolean = {
    val status = super.await(timeout, unit)
    throwIfNeeded()
    status
  }

  private def throwIfNeeded(): Unit = {
    if (abortionThrowable.isDefined) throw abortionThrowable.get
  }
}
