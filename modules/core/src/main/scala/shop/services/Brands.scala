package shop.services

import shop.domain.brand._

trait Brands[F[_]] {
  def findAll: F[List[Brand]]
  def create(name: BrandName): F[BrandId]
}

object FakeImplementation {}

object Brands {
  def make[F[_]]: Brands[F] =
    new Brands[F] {
      override def findAll: F[List[Brand]]             = ???
      override def create(name: BrandName): F[BrandId] = ???

    }
}
