package shop

import weaver._
import cats.syntax.all._
import cats.effect._
import weaver.scalacheck.Checkers

object ShopSpec extends SimpleIOSuite with Checkers {
  val randomUUID = IO(java.util.UUID.randomUUID())

  // A test for side-effecting functions
  test("hello side-effects") {
    for {
      x <- randomUUID
      y <- randomUUID
    } yield expect(x =!= y)
  }

  pureTest("pure expectations") {
    expect(1 === 1)
  }

}
