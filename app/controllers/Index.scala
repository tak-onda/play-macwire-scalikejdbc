package controllers

import play.api.libs.json._
import play.api.mvc._
import models._
import scalikejdbc.DB

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class Index extends Controller {

  implicit val userWriter = Json.writes[User]
  implicit val projectWriter = Json.writes[Project]
  implicit val upWriter = Json.writes[UserProject]
  implicit val resultWriter = Json.writes[UserProjects]

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def user(id: Int) = Action.async {
    val f = findProjects(id)
    for {
      up <- f
    } yield {
      val result = up.groupBy(_.user).map {
        case (u: User, g: List[UserProject]) => UserProjects(u, g.map(_.project))
      }
      Ok(Json.toJson(result))
    }
  }

  def findProjects(id: Int): Future[List[UserProject]] = Future {
    DB.readOnly { implicit s =>
      AssignDao().byUserName("a").byProjectName("r").list()
    }
  }

}
