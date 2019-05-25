import org.scalatest._

class LibSpec extends FunSpec with Matchers {
  it("should say hello") {
    Lib.text shouldEqual "Hello nwjs!"
  }
}
