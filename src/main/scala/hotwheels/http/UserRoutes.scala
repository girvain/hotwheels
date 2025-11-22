package hotwheels.http

import cats.effect.Concurrent
import hotwheels.service.{Users}
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder

final case class UserRoutes[F[_] : Concurrent](users: Users[F]) extends Http4sDsl[F] {

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root / "all" =>
      Ok(users.findAllUsers())

    //    case GET -> Root /
  }

  val routes: HttpRoutes[F] = Router("/users" -> httpRoutes)
}
