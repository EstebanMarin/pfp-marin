package shop.domain

import shop.domain.item._
import shop.domain.cart._

import io.estatico.newtype.macros.newtype
import java.util.UUID
import squants.market.Money
import scala.util.control.NoStackTrace

object orders {
  @newtype case class OrderID(uuid: UUID)
  @newtype case class PaymentID(uuid: UUID)

  case class Order(id: OrderID, pid: PaymentID, items: Map[ItemID, Quantity], total: Money)

  case object EmptyCartError extends NoStackTrace
}
