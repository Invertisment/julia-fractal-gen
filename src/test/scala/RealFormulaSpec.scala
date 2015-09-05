import mm.bitmap.formulas.RealFormula
import org.scalatest._

/**
 * @author Martynas Maciulevičius.
 * @version 1.0 2015-09-05
 */
class RealFormulaSpec extends FlatSpec with Matchers {
  "Formula" should "return c value if n is zero" in {
    val initValue = 152
    val formula = new RealFormula(initValue)
    val output = formula.countNext(0)
    output shouldBe initValue
  }

  it should "count right value" in {
    val initValue = 1
    val formula = new RealFormula(initValue)
    val output = formula.countNext(2)
    output shouldBe 5
  }

}
