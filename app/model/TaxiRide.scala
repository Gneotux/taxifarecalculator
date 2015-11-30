package model

import org.joda.time.DateTime


case class TaxiRide(minutesAboveLimit: Int, milesBelowLimit: Int, dateOfStart: DateTime)
