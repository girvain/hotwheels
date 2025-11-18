package hotwheels.http

import cats.effect.Sync
import hotwheels.service.Vehicles
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

// GET /api/v1/vehicle/{id}
// POST /api/v1/vehicle/create
// GET /api/v1/vehicle?name=...
// GET /api/v1/vehicle/type/{id}
// GET /api/v1/vehicle/date?start=...&end=...
// GET /api/v1/vehicle/user

final case class VehicleRoutes[F[_] : Sync](vehicles: Vehicles[F]) extends Http4sDsl[F] {

  private val prefixPath = "/vehicles"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root => Ok(vehicles.createVehicle())
  }

  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)


}
