package hotwheels.service

import cats.effect.Resource
import cats.effect.kernel.MonadCancelThrow
import cats.implicits._
import hotwheels.domain.vehicle.{CreateVehicleRequest, Vehicle, VehicleId}
import hotwheels.service.VehicleSQL.{insertVehicle, selectAll, selectById}
import hotwheels.sql.codecs._
import skunk.Session


trait Vehicles[F[_]] {
  def createVehicle(vehicleRequest: CreateVehicleRequest): F[CreateVehicleRequest]

  def findAllVehicles(): F[List[Vehicle]]

  def findById(vehicleId: VehicleId): F[Option[Vehicle]]

  def updateVehicle(vehicle: Vehicle): F[Vehicle]

  def deleteVehicle(vehicleId: VehicleId): F[Unit]
}

object Vehicles {

  def make[F[_] : MonadCancelThrow](postgres: Resource[F, Session[F]]): Vehicles[F] = {
    new Vehicles[F] {

      def createVehicle(vehicleRequest: CreateVehicleRequest): F[CreateVehicleRequest] =
        postgres.use { session =>
          session.prepare(insertVehicle).use { cmd =>
            cmd.execute(vehicleRequest).as(vehicleRequest)
          }
        }


      def findAllVehicles(): F[List[Vehicle]] =
        postgres.use {
          _.execute(selectAll)
        }


      def findById(vehicleId: VehicleId): F[Option[Vehicle]] =
        postgres.use { session =>
          session.prepare(selectById).use {
            _.option(vehicleId)
          }
        }


      def updateVehicle(vehicle: Vehicle): F[Vehicle] =
        postgres.use { session =>
          session.prepare(VehicleSQL.updateVehicle).use { cmd =>
            cmd.execute(vehicle).as(vehicle)
          }
        }


      def deleteVehicle(vehicleId: VehicleId): F[Unit] =
        postgres.use { session =>
          session.prepare(VehicleSQL.deleteVehicle).use { cmd =>
            cmd.execute(vehicleId).void
          }
        }

    }
  }
}

private object VehicleSQL {

  import skunk._
  import skunk.codec.all._
  import skunk.implicits._ // <-- THIS MUST BE INSIDE THE OBJECT

  val codec: Codec[CreateVehicleRequest] =
    (varchar ~ vehicleTypeId ~ timestamp ~ varchar ~ userId).imap {
      case n ~ vt ~ d ~ c ~ u => CreateVehicleRequest(n, vt, d, c, u)
    } { vr =>
      (((vr.name, vr.vehicleType), vr.date), vr.color) -> vr.user
    }

  val vehicleDecoder: Decoder[Vehicle] =
    (vehicleId ~ varchar ~ vehicleTypeId ~ timestamp ~ varchar ~ userId).map {
      case v ~ n ~ vt ~ d ~ c ~ u =>
        Vehicle(v, n, vt, d, c, u)
    }

  val insertVehicle: Command[CreateVehicleRequest] = // need to use the column names here coz the order matters and we generate the UUID on the DB
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

  val updateVehicle: Command[Vehicle] =
    sql"""
       UPDATE vehicle
       SET name = $varchar,
           type_id = $vehicleTypeId,
           date = $timestamp,
           colour = $varchar,
           user_id = $userId
       WHERE id = $vehicleId
     """.command.contramap { v: Vehicle =>
      ((((v.name, v.vehicleType), v.date), v.color), v.user) -> v.id
    }

  val deleteVehicle: Command[VehicleId] =
    sql"""
          DELETE FROM vehicle WHERE id = $vehicleId
         """.command

}