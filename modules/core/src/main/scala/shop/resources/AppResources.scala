package shop.resources

import cats.effect.std.Console
import cats.effect._
import cats.syntax.all._
import dev.profunktor.redis4cats.{ Redis, RedisCommands }
import eu.timepit.refined.auto._
import fs2.io.net.Network
import natchez.Trace.Implicits.noop
import org.typelevel.log4cats.Logger
import skunk._
import skunk.codec.text._
import skunk.implicits._
import dev.profunktor.redis4cats.effect.MkRedis

sealed abstract class AppResources[F[_]](val postgres: Resource[F, Session[F]])

object AppResources {
  def make[F[_]: Concurrent: Network: Console: Logger: MkRedis]: Resource[F, AppResources[F]] = {

    def checkPostgres(postgres: Resource[F, Session[F]]) =
      postgres
        .use(session => session.unique(sql"select version()".query(text)))
        .flatMap(v => Logger[F].info(s"[POSTGRES OK] ${v}"))

    def mkPostgreSqlResource: Resource[F, Resource[F, Session[F]]] =
      Session
        .pooled[F](
          host = "localhost",
          port = 5432,
          user = "postgres",
          password = Some("my-password"),
          database = "store",
          max = 10
        )
        .evalTap(checkPostgres)

    def checkRedisConnection(
        redis: RedisCommands[F, String, String]
    ): F[Unit] =
      redis.info.flatMap {
        _.get("redis_version").traverse_ { v =>
          Logger[F].info(s"Connected to Redis $v")
        }
      }

    def mkRedisResource: Resource[F, RedisCommands[F, String, String]] =
      Redis[F].utf8("redis://localhost").evalTap(redis => checkRedisConnection(redis))

    for {
      postgres: Resource[F, Session[F]]       <- mkPostgreSqlResource
      redis: RedisCommands[F, String, String] <- mkRedisResource
    } yield new AppResources[F](postgres) {}
  }
}
