package utils

import play.api.Play
import play.api.Play.current


object WebsiteConfig {

  implicit val fareConfig = FareConfig(
    entry = Play.configuration.getDouble("fareConfig.entry").get,
    unit  = Play.configuration.getDouble("fareConfig.unit").get,
    nightSurchargeStart = Play.configuration.getInt("fareConfig.nightSurchargeStart").get,
    nightSurchargeFinish = Play.configuration.getInt("fareConfig.nightSurchargeFinish").get,
    nightSurcharge = Play.configuration.getDouble("fareConfig.nightSurcharge").get,
    peakTimeWeekdayStart = Play.configuration.getInt("fareConfig.peakTimeWeekdayStart").get,
    peakTimeWeekdayFinish = Play.configuration.getInt("fareConfig.peakTimeWeekdayFinish").get,
    peakTimeSurcharge = Play.configuration.getDouble("fareConfig.peakTimeSurcharge").get,
    citySurcharge = Play.configuration.getDouble("fareConfig.citySurcharge").get,
    milesUnit = Play.configuration.getDouble("fareConfig.milesUnit").get,
    secondsUnit = Play.configuration.getDouble("fareConfig.secondsUnit").get
  )

}

case class FareConfig(
  entry: Double,
  unit: Double,
  nightSurchargeStart: Int,
  nightSurchargeFinish: Int,
  nightSurcharge: Double,
  peakTimeWeekdayStart: Int,
  peakTimeWeekdayFinish: Int,
  peakTimeSurcharge: Double,
  citySurcharge: Double,
  milesUnit: Double,
  secondsUnit: Double
)
