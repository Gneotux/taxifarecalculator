package controllers

import model.{TaxiRideResult, TaxiRide}
import model.TaxiRide._
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, BodyParsers, Controller}
import service.TaxiFareCalculator
import utils.WebsiteConfig._


object TaxiFare extends Controller {

  implicit val jsonFormatTaxiRideResult = Json.format[TaxiRideResult]
  implicit val jsonFormatTaxiRide = Json.format[TaxiRide]


  def calculateFare() = Action(BodyParsers.parse.json) { request =>
    val taxiRideResult = request.body.validate[TaxiRide]
    taxiRideResult.fold(
      errors => {
        BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toFlatJson(errors)))
      },
      taxiRide => {
        Ok(Json.toJson(TaxiFareCalculator.getTaxiRideResult(taxiRide)))
      }
    )
  }
}
