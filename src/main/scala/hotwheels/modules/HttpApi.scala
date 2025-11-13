package hotwheels.modules

import cats.effect.Async
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.middleware._
import org.http4s.{HttpApp, HttpRoutes}

import scala.concurrent.duration._

object HttpApi {
  def make[F[_] : Async](): HttpApi[F] =
    new HttpApi[F] {}
}

sealed abstract class HttpApi[F[_] : Async] {

  private val openRoutes: HttpRoutes[F] = HttpRoutes.empty[F]

  private val routes: HttpRoutes[F] = Router(
    "v1" -> openRoutes
  )

  private val middleware: HttpRoutes[F] => HttpRoutes[F] = {
    { http: HttpRoutes[F] =>
      AutoSlash(http)
    } andThen { http: HttpRoutes[F] =>
      CORS(http)
    } andThen { http: HttpRoutes[F] =>
      Timeout(60.seconds)(http)
    }
  }

  private val loggers: HttpApp[F] => HttpApp[F] = {
    { http: HttpApp[F] =>
      RequestLogger.httpApp(logHeaders = true, logBody = true)(http)
    } andThen { http: HttpApp[F] =>
      ResponseLogger.httpApp(logHeaders = true, logBody = true)(http)
    }
  }

  val httpApp: HttpApp[F] = loggers(middleware(routes).orNotFound)
}
