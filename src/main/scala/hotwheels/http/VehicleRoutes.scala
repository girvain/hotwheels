package hotwheels.http

import cats.effect.Concurrent
import cats.implicits.toFlatMapOps
import cats.syntax.all._
import hotwheels.domain.vehicle._
import hotwheels.service.Vehicles
import org.http4s._
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

final case class VehicleRoutes[F[_] : Concurrent](vehicles: Vehicles[F]) extends Http4sDsl[F] {

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {

    case req@POST -> Root / "create" =>
      for {
        vehicleReq <- req.asJsonDecode[VehicleRequest]
        created <- vehicles.createVehicle(vehicleReq)
        res <- Created(created)
      } yield res

    case GET -> Root / "all" =>
      Ok(vehicles.findAllVehicles())

//    case GET -> Root /
  }

  val routes: HttpRoutes[F] = Router("/vehicles" -> httpRoutes)
}
