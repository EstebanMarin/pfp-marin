package shop.modules

import shop.routes.BrandRoutes

import cats.effect.kernel.Async
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.{ HttpApp, HttpRoutes }

object HttpApi {
  def make[F[_]: Async]: HttpApi[F] =
    new HttpApi[F](
      services = Services.make[F]
    ) {}
}

sealed abstract class HttpApi[F[_]: Async] private (
    services: Services[F]
) {
  private val brandRoutes = BrandRoutes[F](services.brands).routes
  private val openRoutes: HttpRoutes[F] =
    brandRoutes

  private val routes: HttpRoutes[F] = Router(
    "/v1" -> openRoutes
  )

  val httpApp: HttpApp[F] = routes.orNotFound
}