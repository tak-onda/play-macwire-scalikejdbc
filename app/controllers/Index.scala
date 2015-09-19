package controllers

import play.api.libs.json._
import play.api.mvc._
import models._
import scalikejdbc.DB

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class Index extends Controller {

  implicit val userWriter = Json.writes[User]

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def user(id: Int) = Action.async {
    Future {
      DB.readOnly { implicit session =>
        val u = Users.find(id).getOrElse(User(0, "unknown"))
        Ok(Json.toJson(u))
      }
    }
  }

}
