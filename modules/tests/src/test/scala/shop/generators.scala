package shop

import domain.brand._
import org.scalacheck.Gen
import java.util.UUID

object Generators {
  val nonEmptyStringGen: Gen[String] =
    Gen.chooseNum(21, 40).flatMap(Gen.buildableOfN[String, Char](_, Gen.alphaChar))

  def nesGen[A](f: String => A): Gen[A] =
    nonEmptyStringGen.map(f)

  def idGen[A](f: UUID => A): Gen[A] =
    Gen.uuid.map(f)

  val brandGen: Gen[Brand] = for {
    n: Int       <- Gen.chooseNum(21, 40)
    name: String <- Gen.buildableOfN[String, Char](n, Gen.alphaChar)
    uuid: UUID   <- Gen.uuid
  } yield Brand(BrandId(uuid), BrandName(name))

}
