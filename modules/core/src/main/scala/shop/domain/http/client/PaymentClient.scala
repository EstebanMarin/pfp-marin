package shop.domain.http

import shop.domain.orders._
import shop.domain.payment._

trait PaymentClient[F[_]] {
  def process(payment: Payment): F[PaymentID]
}
