package shop.modules

import shop.services.Brands
object Services {
  def make[F[_]]: Services[F] = {
    new Services[F](brands = Brands.make) {}
  }
}

sealed abstract class Services[F[_]] private (
    val brands: Brands[F]
)
