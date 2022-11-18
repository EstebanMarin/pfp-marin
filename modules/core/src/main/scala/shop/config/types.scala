package shop.config

import enumeratum.{ CirisEnum, EnumEntry }

import ciris._
import com.comcast.ip4s.{ Host, Port }
import enumeratum.Enum
import eu.timepit.refined.types.net.UserPortNumber
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.macros.newtype

sealed trait AppEnvironment extends EnumEntry

object AppEnvironment extends Enum[AppEnvironment] with CirisEnum[AppEnvironment] {
  case object Local      extends AppEnvironment
  case object Testing    extends AppEnvironment
  case object Production extends AppEnvironment

  val values = findValues
}

object types {
  import AppEnvironment.{ Local, Production, Testing }

  final case class PostgreSQLConfig(
      host: NonEmptyString,
      port: UserPortNumber,
      user: NonEmptyString,
      password: Secret[NonEmptyString],
      database: NonEmptyString,
      max: PosInt
  )

  @newtype final case class RedisURI(value: NonEmptyString)

  @newtype final case class RedisConfig(uri: RedisURI)

  case class HttpServerConfig(
      host: Host,
      port: Port
  )

  case class AppConfiguration(
      postgreSQL: PostgreSQLConfig,
      redis: RedisConfig,
      httpServerConfig: HttpServerConfig
  )

}
