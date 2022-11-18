package shop.config

import cats.effect.Async
import cats.syntax.all._
import ciris._
import ciris.refined._
import com.comcast.ip4s._
import eu.timepit.refined.auto._
import eu.timepit.refined.cats._
import eu.timepit.refined.types.string.NonEmptyString
import shop.config.AppEnvironment._
import shop.config.types._

object Config {
  def load[F[_]: Async]: F[AppConfiguration] =
    env("SC_APP_ENV")
      .as[AppEnvironment]
      .flatMap {
        case AppEnvironment.Local      => default[F](RedisURI("redis://localhost"))
        case AppEnvironment.Testing    => default[F](RedisURI("redis://localhost"))
        case AppEnvironment.Production => default[F](RedisURI("redis://localhost"))
      }
      .load[F]

  private def default[F[_]](redisUri: RedisURI): ConfigValue[F, AppConfiguration] =
    (
      env("SC_TEST").as[String].secret,
      env("SC_POSTGRES_PASSWORD").as[NonEmptyString].secret
    ).parMapN { (_, postgresPWD) =>
      AppConfiguration(
        PostgreSQLConfig(
          host = "localhost",
          port = 5432,
          user = "postgres",
          password = postgresPWD,
          database = "store",
          max = 10
        ),
        RedisConfig(redisUri),
        HttpServerConfig(
          host = host"0.0.0.0",
          port = port"9000"
        )
      )
    }

}
