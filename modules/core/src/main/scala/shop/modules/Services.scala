package shop.modules

import shop.services.Brands
import cats.Applicative
object Services {
  def make[F[_]: Applicative]: Services[F] = {
    new Services[F](brands = Brands.make) {}
  }
}

sealed abstract class Services[F[_]] private (
    val brands: Brands[F]
)
