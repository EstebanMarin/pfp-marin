package shop.resources

import cats.effect.kernel.{ Async, Resource }
import com.comcast.ip4s._
import org.http4s.HttpApp
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import org.http4s.server.defaults.Banner
import org.typelevel.log4cats.Logger

trait MkHttpServer[F[_]] {
  def newEmber(http: HttpApp[F]): Resource[F, Server]
}

object MkHttpServer {
  private def showEmberBanner[F[_]: Logger](s: Server): F[Unit] =
    Logger[F].info(s"\n${Banner.mkString("\n")}\nHTTP Server started at ${s.address}")

  def apply[F[_]: MkHttpServer]: MkHttpServer[F] = implicitly
  implicit def forAsyncLogger[F[_]: Async: Logger]: MkHttpServer[F] =
    new MkHttpServer[F] {
      def newEmber(httpApp: HttpApp[F]): Resource[F, Server] =
        EmberServerBuilder
          .default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(httpApp)
          .build
          .evalTap(showEmberBanner[F])
    }
}
