package shop

import shop.domain.brand.Brand
import shop.modules.Services

import cats.effect._

object Main extends IOApp.Simple {
  val services: Services[IO] = Services.make[IO]
  val IOBrands: IO[List[Brand]] = for {
    brands: List[Brand] <- services.brands.findAll
  } yield (brands)

  override def run =
    for {
      brands <- IOBrands
      _      <- IO.println(s"[HERE] ${brands.toList}")
    } yield ()

}
