package shop.domain

import io.estatico.newtype.macros.newtype
import java.util.UUID

object authentication {
  @newtype case class UserId(id: UUID)
  @newtype case class JwtToken(id: UUID)

  case class User(id: UserId, name: users.UserName)
}
