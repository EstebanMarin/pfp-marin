package shop
package services

import shop.domain.Brand._

trait Brands[F[_]] {
  def findAll: F[List[Brand]]
  def create(name: BrandName): F[BrandId]
}
