package controllers

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.mvc._
import models._

class Index extends Controller {

  implicit val userWriter: Writes[User] = (
    (JsPath \ "id").write[Int] and
     (JsPath \ "name").write[String]
  )(unlift(User.unapply))

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def user(id: Int) = Action {
    val u = User.find(id).getOrElse(User(0, "unknown"))
    Ok(Json.toJson(u))
  }

}
