package hotwheels.http

import cats.effect.Sync
import hotwheels.domain.vehicle._
import hotwheels.service.Vehicles
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec._
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

  val d = implicitly[io.circe.Decoder[VehicleRequest]]
  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root => Ok("meh")


    //    case req @ POST -> Root / "create" =>

    //      for {
    //        vehicleReq <- req.as[VehicleRequest]   // JSON body decoded here
    //        created <- vehicles.createVehicle(vehicleReq)
    //        resp <- Created(vehicleReq)               // encoded to JSON automatically
    //      } yield resp
    //  }
  }

  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)


}
