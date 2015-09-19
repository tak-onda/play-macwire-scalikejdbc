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
    findUser(id).map(u => Ok(Json.toJson(u)))
//    val fu = findUser(id)
//    for {
//      u <- fu
//    } yield Ok(Json.toJson(u))
  }

  def findUser(id: Int): Future[User] = Future {
    DB.readOnly { implicit session =>
      Users.find(id).getOrElse(User(0, "unknown"))
    }
  }

}
