package shop.services

import shop.domain.users._
import shop.domain.authentication._

trait Users[F[_]] {
  def find(usermame: UserName): F[Option[UserWithPassword]]
  def create(username: UserName, password: EncryptedPassword): F[UserId]
}
