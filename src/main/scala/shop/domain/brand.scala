package shop.domain

import java.util.UUID
import io.estatico.newtype.macros.newtype
import eu.timepit.refined.types.string._

object brand {
  @newtype case class BrandId(value: UUID)
  @newtype case class BrandName(value: NonEmptyString)
  case class Brand(uuid: BrandId, name: BrandName)
}
