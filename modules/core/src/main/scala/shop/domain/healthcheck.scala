package shop.domain

import io.estatico.newtype.macros.newtype

object healthcheck {
  sealed trait Status {
    object Okay        extends Status
    object Unreachable extends Status
  }

  @newtype case class RedisStatus(v: Status)
  @newtype case class PostgresStatus(v: Status)

  case class AppStatus(redis: RedisStatus, postgress: PostgresStatus)
}
