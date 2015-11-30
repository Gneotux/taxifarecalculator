package service

import model.{TaxiRideResult, TaxiRide}
import org.joda.time._
import utils.FareConfig
import utils.WebsiteConfig._


object TaxiFareCalculator {

  def setMinutesAndSecondsToZero(date :DateTime) = Option(date.withMinuteOfHour(0).withSecondOfMinute(0))

  def getFareForPeakTimeWeekday(date: DateTime)(implicit fareConfig: FareConfig): Double = {

    val weekDays: List[Int] = List(
      DateTimeConstants.MONDAY,
      DateTimeConstants.TUESDAY,
      DateTimeConstants.WEDNESDAY,
      DateTimeConstants.THURSDAY,
      DateTimeConstants.FRIDAY
    )

    val isBetweenInterval: Option[Boolean] = for {
      weekDay <- Option(date.getDayOfWeek) if weekDays.contains(weekDay)
      startTime <- setMinutesAndSecondsToZero(date.withHourOfDay(fareConfig.peakTimeWeekdayStart))
      endTime <- setMinutesAndSecondsToZero(date.withHourOfDay(fareConfig.peakTimeWeekdayFinish))
      interval <- Option(new Interval(startTime, endTime))
    } yield interval.contains(date)

    isBetweenInterval.fold(0.0){
      e => if (e) fareConfig.peakTimeSurcharge else 0.0
    }
  }

  def getFareForNightSurcharge(date: DateTime)(implicit fareConfig: FareConfig) = {
    val isBetweenInterval: Option[Boolean] = for {
      startTimeDawn <- setMinutesAndSecondsToZero(date.withHourOfDay(0))
      endTimeDawn <- setMinutesAndSecondsToZero(date.withHourOfDay(fareConfig.nightSurchargeFinish))
      startTimeNight <- setMinutesAndSecondsToZero(date.withHourOfDay(fareConfig.nightSurchargeStart))
      endTimeNight <- Option(date.withHourOfDay(23).withMinuteOfHour(59))
      intervalNight <- Option(new Interval(startTimeNight, endTimeNight))
      intervalDawn <- Option(new Interval(startTimeDawn, endTimeDawn))
    } yield intervalNight.contains(date) || intervalDawn.contains(date)

    isBetweenInterval.fold(0.0){
      e => if (e) fareConfig.nightSurcharge else 0.0
    }
  }

  def getMinutesAboveLimitFare(minutesAboveLimit: Int)(implicit fareConfig: FareConfig): Double =
    ((minutesAboveLimit * 60) / fareConfig.secondsUnit) * fareConfig.unit

  def getMilesBelowLimitFare(milesBelowLimit:Int )(implicit fareConfig: FareConfig): Double =
    (milesBelowLimit / fareConfig.milesUnit) * fareConfig.unit

  def getTaxiRideResult(taxiRide: TaxiRide)(implicit fareConfig: FareConfig): TaxiRideResult = {
    TaxiRideResult(
      Some(fareConfig.entry),
      Some(taxiRide.dateOfStart),
      Some(getMinutesAboveLimitFare(taxiRide.minutesAboveLimit)),
      Some(getMilesBelowLimitFare(taxiRide.milesBelowLimit)),
      Some(fareConfig.citySurcharge),
      Some(getFareForNightSurcharge(taxiRide.dateOfStart)),
      Some(getFareForPeakTimeWeekday(taxiRide.dateOfStart))
    )
  }

}
