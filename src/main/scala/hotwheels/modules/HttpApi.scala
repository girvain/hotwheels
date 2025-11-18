package hotwheels.modules

import cats.effect.Async
import hotwheels.http.VehicleRoutes
import hotwheels.resources.Services
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.middleware._
import org.http4s.{HttpApp, HttpRoutes}

import scala.concurrent.duration._

object HttpApi {
  def make[F[_] : Async](services: Services[F]): HttpApi[F] =
    new HttpApi[F](services) {}
}

sealed abstract class HttpApi[F[_] : Async](services: Services[F]) {

  private val vehicleRoutes = VehicleRoutes[F](services.vehicles).routes

  private val openRoutes: HttpRoutes[F] = vehicleRoutes

  private val routes: HttpRoutes[F] = Router(
    "/api/v1" -> openRoutes
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
