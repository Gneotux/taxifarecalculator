package model

import org.joda.time.DateTime


case class TaxiRideResult(
  entry: Option[Double],
  date: Option[DateTime],
  minutesAboveLimitFare: Option[Double],
  milesBelowLimitFare: Option[Double],
  cityFare: Option[Double],
  nightSurcharge: Option[Double],
  peakTimeSurcharge: Option[Double]
)