package hotwheels.service

import cats.effect.{Resource, Sync}
import hotwheels.domain.user.UserId
import hotwheels.domain.vehicle.{Vehicle, VehicleId, VehicleTypeId}
import skunk.Session

import java.time.LocalDateTime
import java.util.UUID

trait Vehicles[F[_]] {
  def createVehicle(): F[Vehicle]
}

object Vehicles {

  def make[F[_] : Sync](postgres: Resource[F, Session[F]]): Vehicles[F] = {
    new Vehicles[F] {
      def createVehicle: F[Vehicle] = {

      }
    }
  }
}

//Sync[F].delay {
//  Vehicle(
//    id = VehicleId(UUID.randomUUID()),
//    name = "Hot Rod",
//    vehicleType = VehicleTypeId(UUID.randomUUID()),
//    color = "Red",
//    date = LocalDateTime.now(),
//    user = UserId(UUID.randomUUID()),
//  )
//}