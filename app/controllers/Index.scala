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
  implicit val resultWriter = Json.writes[SearchResult]

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def user(id: Int) = Action.async {
    val f = findProjects(id)
    for {
      p <- f
    } yield Ok(Json.toJson(p))
//    val projects: List[Project] = for {
//      r <- fr
//      (u: User, p: Project) <- r
//    } yield p
//    OK(Json.toJson(projects))
  }

  def findProjects(id: Int): Future[Iterable[SearchResult]] = Future {
    DB.readOnly { implicit s =>
      Dao(s).byUserName("onda").byProjectName("rx").apply()
    }
  }

}
