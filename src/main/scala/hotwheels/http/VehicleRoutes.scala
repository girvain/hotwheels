package hotwheels.http

import cats.effect.Sync
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

final case class VehicleRoutes[F[_] : Sync]() extends Http4sDsl[F] {

  private val prefixPath = "/vehicles"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root =>
      Ok("hello gavin")
  }

  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)
}
