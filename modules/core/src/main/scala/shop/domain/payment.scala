package shop.domain

import shop.domain.authentication._
import shop.domain.checkout._
import squants.market.Money

object payment {
  case class Payment(
      id: UserId,
      total: Money,
      card: Card
  )
}
