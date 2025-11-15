package hotwheels.domain

import java.time.LocalDateTime

object vehicle {

  case class Vehicle(id: Int,
                     name: String,
                     vehicleType: Long,
                     date: LocalDateTime,
                     color: String,
                     user: Option[Long])


}

