package shop.services

import shop.domain.authentication._
import shop.domain.users._

trait Auth[F[_]] {
  def findUser(token: JwtToken): F[Option[User]]
  def newUser(usermame: UserName, password: Password): F[JwtToken]
}
