package shop.domain

import java.util.UUID

import derevo.cats._
import derevo.circe.magnolia.{ decoder, encoder }
import derevo.derive
import io.estatico.newtype.macros.newtype
import shop.optics.uuid

object brand {
  @derive(decoder, encoder, eqv, show, uuid)
  @newtype case class BrandId(value: UUID)

  @derive(decoder, encoder, eqv, show)
  @newtype case class BrandName(value: String)

  @derive(encoder, decoder, eqv, show)
  case class Brand(uuid: BrandId, name: BrandName)
}
