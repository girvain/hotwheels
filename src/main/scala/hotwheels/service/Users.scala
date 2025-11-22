package hotwheels.service

import cats.effect.{Resource, Sync}
import cats.implicits.catsSyntaxTuple5Semigroupal
//import cats.implicits._
import hotwheels.domain.user.User
import hotwheels.service.UsersSQL.selectAll
import skunk.Session


trait Users[F[_]] {

  def findAllUsers(): F[List[User]]

}

object Users {

  def make[F[_] : Sync ](postgres: Resource[F, Session[F]]): Users[F] = {
    new Users[F] {

      def findAllUsers(): F[List[User]] = {
        postgres.use {
          _.execute(selectAll)
        }
      }

    }
  }
}

private object UsersSQL {

  import skunk._
  import skunk.codec.all._
  import skunk.implicits._

  val codec: Codec[User] =
    (uuid, varchar, varchar, varchar, varchar).tupled.imap {
      case (i, n, t, d, p) => User(i, n, t, d, p)
    } { u =>
      (u.id, u.name, u.email, u.sub, u.profilePicture)
    }



  //  val vehicleDecoder: Decoder[Vehicle] =
  //    (vehicleId ~ varchar ~ vehicleTypeId ~ timestamp ~ varchar ~ userId).map {
  //      case i ~ n ~ t ~ d ~ c ~ u =>
  //        Vehicle(i, n, t, d, c, u)
  //    }

  //  val vehicleDecoder: Decoder[Vehicle] =
  //    (uuid ~ varchar ~ uuid ~ timestamp ~ varchar ~ uuid).map {
  //      case ((((id ~ name) ~ vehicleType) ~ date) ~ color) ~ user =>
  //        Vehicle(id, name, vehicleType, date, color, user)
  //    }

  //  val vehicleDecoder2: Decoder[Vehicle] =
  //    (uuid ).map {
  //      case i  => VehicleId(i)

  //        Vehicle(i,
  //          n,
  //          vt,
  ////          VehicleTypeId(UUID.randomUUID()),
  //          LocalDateTime.now(), "", UserId(UUID.randomUUID()))
  //    }


  val selectAll: Query[Void, User] =
    sql"""
    SELECT id, name, email, sub, profile_picture
    FROM users
  """.query(codec)

}