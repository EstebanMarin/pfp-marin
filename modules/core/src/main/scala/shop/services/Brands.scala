package shop.services

import java.util.UUID

import shop.domain.brand._

import cats.Applicative
import cats.syntax.all._

trait Brands[F[_]] {
  def findAll: F[List[Brand]]
  def create(name: BrandName): F[BrandId]
}

object Brands {
  def make[F[_]: Applicative]: Brands[F] = {
    new Brands[F] {
      override def findAll: F[List[Brand]] =
        List(Brand(uuid = BrandId(UUID.randomUUID()), name = BrandName("test"))).pure
      override def create(name: BrandName): F[BrandId] = BrandId(UUID.randomUUID()).pure
    }
  }

}
