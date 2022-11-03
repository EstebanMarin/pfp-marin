package shop.services

import shop.domain.authD._

trait Auth[F[_]] {
  def findUser(token: JwtToken): F[Option[User]]
}
