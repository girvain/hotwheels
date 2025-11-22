package hotwheels.service

import cats.effect.Resource
import cats.effect.kernel.MonadCancelThrow
import cats.implicits.toFunctorOps
import hotwheels.domain.vehicle.{Vehicle, VehicleId, VehicleRequest}
import hotwheels.service.VehicleSQL.{insertVehicle, selectAll, selectById}
import hotwheels.sql.codecs._
import skunk.Session

trait Vehicles[F[_]] {
  def createVehicle(vehicleRequest: VehicleRequest): F[VehicleRequest]

  def findAllVehicles(): F[List[Vehicle]]

  def findById(vehicleId: VehicleId): F[Vehicle]
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

      def findAllVehicles(): F[List[Vehicle]] = {
        postgres.use {
          _.execute(selectAll)
        }
      }

      def findById(vehicleId: VehicleId): F[Vehicle] = {
        postgres.use { session =>
          session.prepare(selectById).use {
            _.unique(vehicleId)
          }
        }
      }

    }
  }
}

private object VehicleSQL {

  import skunk._
  import skunk.codec.all._
  import skunk.implicits._ // <-- THIS MUST BE INSIDE THE OBJECT

  val codec: Codec[VehicleRequest] =
    (varchar ~ vehicleTypeId ~ timestamp ~ varchar ~ userId).imap {
      case n ~ vt ~ d ~ c ~ u => VehicleRequest(n, vt, d, c, u)
    } { vr =>
      // build nested tuple by nesting plain tuples, not using ~
      (((vr.name, vr.vehicleType), vr.date), vr.color) -> vr.user
    }

  val vehicleDecoder: Decoder[Vehicle] =
    (vehicleId ~ varchar ~ vehicleTypeId ~ timestamp ~ varchar ~ userId).map {
      case v ~ n ~ vt ~ d ~ c ~ u =>
        Vehicle(v, n, vt, d, c, u)
    }

  val insertVehicle: Command[VehicleRequest] = // need to use the column names here coz the order matters and we generate the UUID on the DB
    sql"""
         INSERT INTO vehicle (name, type_id, date, colour, user_id)
         VALUES ($codec)
         """.command

  val selectAll: Query[Void, Vehicle] =
    sql"""
         SELECT id, name, type_id, date, colour, user_id
         FROM vehicle
       """.query(vehicleDecoder)

  val selectById: Query[VehicleId, Vehicle] =
    sql"""
         SELECT * FROM vehicle WHERE id = $vehicleId
       """.query(vehicleDecoder)
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