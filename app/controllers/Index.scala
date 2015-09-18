package controllers

import play.api.libs.json._
import play.api.mvc._
import models._

class Index extends Controller {

//  implicit val userWriter: Writes[User] = Json.writes[User]

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def user(id: Int) = Action {
    Ok(User.find(id).getOrElse(User(0, "unknown")).name)
  }

}
