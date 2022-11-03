package shop.domain

import shop.domain.item._

import io.estatico.newtype.macros.newtype

object cart {
  @newtype case class Quantity(value: Int)
  @newtype case class Cart(items: Map[ItemID, Quantity])

  case class CartItem(item: Item, quantity: Quantity)
  case class CartTotal(items: List[CartItem])
}
