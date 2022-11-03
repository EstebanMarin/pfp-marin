package shop.domain

import io.estatico.newtype.macros.newtype

import shop.domain.authD._

object users {
  @newtype case class UserName(name: String)
  @newtype case class Password(ps: String)
  @newtype case class EncryptedPassword(salted: String)

  case class UserWithPassword(userId: UserId, name: UserName, password: EncryptedPassword)
}
