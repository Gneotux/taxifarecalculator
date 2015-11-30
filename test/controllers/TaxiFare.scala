import controllers.TaxiFare
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.libs.json.{Json, JsValue}

import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class TaxiFare extends Specification {

  "TaxiFareController" should {
    "POST /taxifare return the correct TaxiRideResult" in new WithApplication {

      val json: JsValue = Json.parse("""{"minutesAboveLimit": 5, "milesBelowLimit": 2, "dateOfStart": 1286555400000 }""")

      val request = route(FakeRequest(Helpers.POST, "/taxifare").withJsonBody(json).withHeaders(CONTENT_TYPE -> "application/json")).get

      val expected = """{"entry":3.0,"date":1286555400000,"minutesAboveLimitFare":1.75,"milesBelowLimitFare":3.5,"cityFare":0.5,"nightSurcharge":0.0,"peakTimeSurcharge":1.0}"""

      status(request) mustEqual OK

      contentAsString(request) mustEqual expected
    }

    "POST /taxifare without minutesAboveLimit return error" in new WithApplication {

      //2010-10-08
      val json: JsValue = Json.parse( """{"milesBelowLimit": 2, "dateOfStart": 1286555400000 }""")

      val request = route(FakeRequest(Helpers.POST, "/taxifare").withJsonBody(json).withHeaders(CONTENT_TYPE -> "application/json")).get

      status(request) mustEqual BAD_REQUEST
    }

    "POST /taxifare without milesBelowLimit return error" in new WithApplication {

      //2010-10-08
      val json: JsValue = Json.parse( """{"minutesAboveLimit": 5, "dateOfStart": 1286555400000 }""")

      val request = route(FakeRequest(Helpers.POST, "/taxifare").withJsonBody(json).withHeaders(CONTENT_TYPE -> "application/json")).get

      status(request) mustEqual BAD_REQUEST
    }

    "POST /taxifare without date return error" in new WithApplication {

      //2010-10-08
      val json: JsValue = Json.parse("""{"minutesAboveLimit": 5, "milesBelowLimit": 2}""")

      val request = route(FakeRequest(Helpers.POST, "/taxifare").withJsonBody(json).withHeaders(CONTENT_TYPE -> "application/json")).get

      status(request) mustEqual BAD_REQUEST
    }
  }

}