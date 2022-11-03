package shop.services

import shop.domain.item._
import shop.domain.brand._

trait Items[F[_]] {
  def getAll: F[List[Item]]
  def findByBrand(brand: Brand): F[List[Item]]
  def findByID(itemID: ItemID): F[Option[Item]]
  def createItem(item: CreateItem): F[ItemID]
  def update(itemID: ItemID): F[Option[ItemID]]
}
