package shop.domain.programs

import cats.Monad
import shop.domain.http.PaymentClient
import shop.services.ShoppingCart
import shop.services.Orders
import shop.domain.authentication._
import shop.domain.checkout._
import shop.domain.orders._

final case class Checkout[F[_]: Monad](
    payments: PaymentClient[F],
    cart: ShoppingCart[F],
    orders: Orders[F]
) {
  def process(usedId: UserId, card: Card): F[OrderID] =
    // for {
    // //   c <- cart.get(usedId)
    //   //   pid <- payments.process(c.item)
    // } yield ???
    ???
}
