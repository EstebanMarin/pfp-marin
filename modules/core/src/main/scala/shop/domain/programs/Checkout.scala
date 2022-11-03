package shop.domain.programs

import cats._
import cats.implicits._
import cats.data._

import shop.domain.http.PaymentClient
import shop.services.ShoppingCart
import shop.services.Orders
import shop.domain.authentication._
import shop.domain.checkout._
import shop.domain.orders._
import shop.domain.payment._
import shop.domain.cart._

final case class Checkout[F[_]: MonadThrow](
    payments: PaymentClient[F],
    cart: ShoppingCart[F],
    orders: Orders[F]
) {

  private def ensureNonEmpty[A](xs: List[A]): F[NonEmptyList[A]] =
    MonadThrow[F].fromOption(NonEmptyList.fromList(xs), EmptyCartError)

  def process(userId: UserId, card: Card): F[OrderID] = {
    for {
      c: shop.domain.cart.CartTotal <- cart.get(userId)
      its: NonEmptyList[CartItem]   <- ensureNonEmpty(c.items)
      pid: PaymentID                <- payments.process(Payment(userId, c.total, card))
      oid: OrderID                  <- orders.create(userId, pid, its, c.total)
    } yield oid
  }
}
