package shop.modules

import shop.services.Brands
import cats.effect._
import skunk._
import shop.domain.effects.GenUUID

object Services {
  def make[F[_]: MonadCancelThrow: GenUUID](
      postgres: Resource[F, Session[F]]
  ): Services[F] = {
    new Services[F](brands = Brands.make[F](postgres)) {}
  }
}

sealed abstract class Services[F[_]] private (
    val brands: Brands[F]
)
