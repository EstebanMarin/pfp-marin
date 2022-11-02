package shop
package domain

import io.estatico.newtype.macros.newtype
import java.util.UUID
import eu.timepit.refined.types.string

object Brand {
  @newtype case class BrandId(value: UUID)
  @newtype case class BrandName(value: string.NonEmptyString)
  case class Brand(uuid: UUID, name: BrandName)
}
