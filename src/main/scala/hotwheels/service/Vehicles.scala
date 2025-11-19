package hotwheels.service

import cats.effect.Resource
import cats.effect.kernel.MonadCancelThrow
import cats.implicits.toFunctorOps
import hotwheels.domain.vehicle.VehicleRequest
import hotwheels.service.VehicleSQL.insertVehicle
import hotwheels.sql.codecs._
import skunk.Session

trait Vehicles[F[_]] {
  def createVehicle(vehicleRequest: VehicleRequest): F[VehicleRequest]
}

object Vehicles {

  def make[F[_] : MonadCancelThrow](postgres: Resource[F, Session[F]]): Vehicles[F] = {
    new Vehicles[F] {
      def createVehicle(vehicleRequest: VehicleRequest): F[VehicleRequest] = {
        postgres.use { session =>
          session.prepare(insertVehicle).use { cmd =>
            cmd.execute(vehicleRequest).as(vehicleRequest)
          }
        }
      }
    }
  }
}

private object VehicleSQL {

  import skunk._
  import skunk.codec.all._
  import skunk.implicits._     // <-- THIS MUST BE INSIDE THE OBJECT

  val codec: Codec[VehicleRequest] =
    (varchar ~ vehicleTypeId ~ timestamp ~ varchar ~ userId).imap {
      case n ~ vt ~ d ~ c ~ u => VehicleRequest(n, vt, d, c, u)
    } { vr =>
      // build nested tuple by nesting plain tuples, not using ~
      (((vr.name, vr.vehicleType), vr.date), vr.color) -> vr.user
    }

  val insertVehicle: Command[VehicleRequest] =
    sql"""
         INSERT INTO vehicle (name, type_id, date, colour, user_id)
         VALUES ($codec)
         """.command
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