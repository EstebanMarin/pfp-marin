package shop.domain

import java.util.UUID
import io.estatico.newtype.macros.newtype

object brand {
  @newtype case class BrandId(value: UUID)
}
