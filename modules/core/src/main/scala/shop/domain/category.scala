package shop.domain

import io.estatico.newtype.macros.newtype
import java.util.UUID

object category {
  @newtype case class CategoryID(id: UUID)
  @newtype case class CategoryName(name: String)
  case class Category(id: CategoryID, name: CategoryName)
}
