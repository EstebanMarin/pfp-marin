package shop.resources

import cats.effect.std.Console
import cats.effect.{ Concurrent, Resource }
import cats.syntax.all._
import eu.timepit.refined.auto._
import fs2.io.net.Network
import natchez.Trace.Implicits.noop
import org.typelevel.log4cats.Logger
import skunk._
import skunk.codec.text._
import skunk.implicits._

sealed abstract class AppResources[F[_]](val postgres: Resource[F, Session[F]])

object AppResources {
  def make[F[_]: Concurrent: Network: Console: Logger]: Resource[F, AppResources[F]] = {

    def checkPostgres(postgres: Resource[F, Session[F]]) =
      postgres
        .use(session => session.unique(sql"select version()".query(text)))
        .flatMap(v => Logger[F].info(s"WORKING ${v}"))

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
    for {
      postgres <- mkPostgreSqlResource
    } yield new AppResources[F](postgres) {}
  }
}
