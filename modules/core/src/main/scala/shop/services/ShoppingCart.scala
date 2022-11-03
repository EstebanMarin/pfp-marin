package shop.services

import shop.domain.authentication._
import shop.domain.item._
import shop.domain.cart._

trait ShoppingCart[F[_]] {
  def add(userId: UserId, cart: ItemID, quantity: Quantity): F[Option[ItemID]]
  def get(userId: UserId): F[CartTotal]
  def remove(userId: UserId): F[Option[ItemID]]
  def removeItem(userId: UserId, itemID: ItemID): F[Option[ItemID]]
  def update(usedID: UserId, cart: Cart): F[Unit]
}
