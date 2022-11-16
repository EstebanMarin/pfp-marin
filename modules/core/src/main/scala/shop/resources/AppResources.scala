package shop.resources

import cats.effect.std.Console
import cats.effect.{ Concurrent, Resource }
import fs2.io.net.Network
import natchez.Trace.Implicits.noop
import skunk._

sealed abstract class AppResources[F[_]](val postgres: Resource[F, Session[F]])

object AppResources {
  def make[F[_]: Concurrent: Network: Console]() = {
    def mkPostgreSqlResource: Resource[F, Session[F]] =
      Session.single(
        host = "localhost",
        port = 5432,
        user = "postgres",
        password = Some("password"),
        database = "store"
      )

    new AppResources[F](mkPostgreSqlResource) {}
  }
}
