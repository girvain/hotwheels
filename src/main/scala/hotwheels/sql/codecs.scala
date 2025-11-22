package hotwheels.sql

import hotwheels.domain.user.UserId
import hotwheels.domain.vehicle.{VehicleId, VehicleTypeId}
import skunk.codec.all._

object codecs {

  val vehicleId = uuid.imap[VehicleId](VehicleId(_))(_.value)
  val vehicleTypeId = uuid.imap[VehicleTypeId](VehicleTypeId(_))(_.value)
  val userId = uuid.imap[UserId](UserId(_))(_.value)

  // any varchar with a length, i.e varchar(255)
  val varcharN = varchar.imap(identity[String])(identity[String])

}
