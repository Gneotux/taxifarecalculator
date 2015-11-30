package service

import model.{TaxiRideResult, TaxiRide}
import org.joda.time.{DateTimeConstants, DateTime}
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.Play
import play.api.test.Helpers._
import play.api.test._
import utils.FareConfig
import utils.WebsiteConfig.fareConfig


@RunWith(classOf[JUnitRunner])
class TaxiFareCalculatorSpec extends Specification {

  "TaxiFareSpec.getFareForPeakTimeWeekday" should {

    "return 0 unit passing Wednesday at 12:00 having the interval between Monday-Friday from 16:00 to 20:00" in {

      implicit val fareConfig = FareConfig(
        entry = 3.00,
        unit  = 0.35,
        nightSurchargeStart = 8,
        nightSurchargeFinish = 6,
        nightSurcharge = 0.5,
        peakTimeWeekdayStart = 16,
        peakTimeWeekdayFinish = 20,
        peakTimeSurcharge = 1.0,
        citySurcharge = 0.5,
        milesUnit = 0.2,
        secondsUnit = 60
      )

      val date = new DateTime().withDayOfWeek(DateTimeConstants.WEDNESDAY).withHourOfDay(12).withMinuteOfHour(0)

      TaxiFareCalculator.getFareForPeakTimeWeekday(date) must equalTo(0.0)
    }

    "return 3.1415 unit passing Monday at 16:01 having the interval between Monday-Friday from 16:00 to 20:00" in {

      implicit val fareConfig = FareConfig(
        entry = 3.00,
        unit  = 0.35,
        nightSurchargeStart = 8,
        nightSurchargeFinish = 6,
        nightSurcharge = 0.5,
        peakTimeWeekdayStart = 16,
        peakTimeWeekdayFinish = 20,
        peakTimeSurcharge = 3.1415,
        citySurcharge = 0.5,
        milesUnit = 0.2,
        secondsUnit = 60
      )

      val date = new DateTime().withDayOfWeek(DateTimeConstants.MONDAY).withHourOfDay(16).withMinuteOfHour(1)

      TaxiFareCalculator.getFareForPeakTimeWeekday(date) must equalTo(3.1415)
    }

    "return 0 unit passing Friday at 15:59 having the interval between Monday-Friday from 16:00 to 20:00" in {

      implicit val fareConfig = FareConfig(
        entry = 3.00,
        unit  = 0.35,
        nightSurchargeStart = 8,
        nightSurchargeFinish = 6,
        nightSurcharge = 0.5,
        peakTimeWeekdayStart = 16,
        peakTimeWeekdayFinish = 20,
        peakTimeSurcharge = 1.0,
        citySurcharge = 0.5,
        milesUnit = 0.2,
        secondsUnit = 60
      )

      val date = new DateTime().withDayOfWeek(DateTimeConstants.MONDAY).withHourOfDay(15).withMinuteOfHour(59)

      TaxiFareCalculator.getFareForPeakTimeWeekday(date) must equalTo(0.0)
    }

  }


  "TaxiFareSpec.getFareForNightSurcharge" should {

    "return 0 unit passing a day at 12:00 having the interval interval between 20:00 to 6:00" in {

      implicit val fareConfig = FareConfig(
        entry = 3.00,
        unit  = 0.35,
        nightSurchargeStart = 20,
        nightSurchargeFinish = 6,
        nightSurcharge = 0.5,
        peakTimeWeekdayStart = 16,
        peakTimeWeekdayFinish = 20,
        peakTimeSurcharge = 1.0,
        citySurcharge = 0.5,
        milesUnit = 0.2,
        secondsUnit = 60
      )

      val date = new DateTime().withHourOfDay(12).withMinuteOfHour(0)

      TaxiFareCalculator.getFareForNightSurcharge(date) must equalTo(0.0)
    }

    "return 3.14 unit passing day at 21:19 having the interval between 20:00 to 6:00" in {

      implicit val fareConfig = FareConfig(
        entry = 3.00,
        unit  = 0.35,
        nightSurchargeStart = 20,
        nightSurchargeFinish = 6,
        nightSurcharge = 3.14,
        peakTimeWeekdayStart = 16,
        peakTimeWeekdayFinish = 20,
        peakTimeSurcharge = 1.0,
        citySurcharge = 0.5,
        milesUnit = 0.2,
        secondsUnit = 60
      )

      val date = new DateTime().withHourOfDay(21).withMinuteOfHour(19)

      TaxiFareCalculator.getFareForNightSurcharge(date) must equalTo(3.14)
    }

    "return 0 unit passing day at 19:59 having the interval between 20:00 to 6:00" in {

      implicit val fareConfig = FareConfig(
        entry = 3.00,
        unit  = 0.35,
        nightSurchargeStart = 20,
        nightSurchargeFinish = 6,
        nightSurcharge = 0.5,
        peakTimeWeekdayStart = 16,
        peakTimeWeekdayFinish = 20,
        peakTimeSurcharge = 1.0,
        citySurcharge = 0.5,
        milesUnit = 0.2,
        secondsUnit = 60
      )

      val date = new DateTime().withHourOfDay(19).withMinuteOfHour(59)

      TaxiFareCalculator.getFareForNightSurcharge(date) must equalTo(0.0)
    }

  }


  "TaxiFareSpec.getPrice" should {

    "return 9.75 passing" in {
      implicit val fareConfig = FareConfig(
        entry = 3.00,
        unit  = 0.35,
        nightSurchargeStart = 20,
        nightSurchargeFinish = 6,
        nightSurcharge = 0.5,
        peakTimeWeekdayStart = 16,
        peakTimeWeekdayFinish = 20,
        peakTimeSurcharge = 1.0,
        citySurcharge = 0.5,
        milesUnit = 0.2,
        secondsUnit = 60
      )


      val date = new DateTime().withDayOfWeek(DateTimeConstants.FRIDAY).withHourOfDay(17).withMinuteOfHour(30)

      val taxiRide = TaxiRide(minutesAboveLimit = 5, milesBelowLimit = 2, date)

      val expected = TaxiRideResult(
        Some(fareConfig.entry),
        Some(date),
        Some(TaxiFareCalculator.getMinutesAboveLimitFare(taxiRide.minutesAboveLimit)),
        Some(TaxiFareCalculator.getMilesBelowLimitFare(taxiRide.milesBelowLimit)),
        Some(fareConfig.citySurcharge),
        Some(TaxiFareCalculator.getFareForNightSurcharge(date)),
        Some(TaxiFareCalculator.getFareForPeakTimeWeekday(date))
      )

      expected.nightSurcharge.get must equalTo(0.0)

      expected.peakTimeSurcharge.get must equalTo(1.0)

      expected.minutesAboveLimitFare.get must equalTo (1.75)

      expected.milesBelowLimitFare.get must equalTo (3.5)

      TaxiFareCalculator.getTaxiRideResult(taxiRide) must equalTo(expected)
    }
  }
}