package shop.domain

import shop.domain.category._
import shop.domain.brand._

import io.estatico.newtype.macros.newtype
import java.util.UUID
import squants.market.Money

object item {
  @newtype case class ItemID(id: UUID)
  @newtype case class ItemModel(name: String)
  @newtype case class ItemDescription(description: String)

  case class Item(
      id: ItemID,
      model: ItemModel,
      description: ItemDescription,
      price: Money,
      brand: Brand,
      category: Category
  )

  case class CreateItem(
      description: ItemDescription,
      price: Money,
      brand: Brand,
      category: Category
  )

  case class UpdateItem(
      id: ItemID,
      price: Option[Money],
      description: Option[ItemDescription],
      name: Option[ItemModel]
  )
}
