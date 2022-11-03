package shop.services

import shop.domain.authD._
import shop.domain.orders._
import shop.domain.cart._
import cats.data.NonEmptyList
import squants.market.Money

trait Orders[F[_]] {
  def get(userId: UserId, orderId: OrderID): F[Option[Order]]
  def findByID(userId: UserId): F[List[Order]]
  def create(userId: UserId, paymentID: PaymentID, items: NonEmptyList[CartItem], total: Money): F[OrderID]
}
