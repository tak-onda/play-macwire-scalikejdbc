package models

import scalikejdbc._

case class SearchResult(user: User, projects: List[Project])
case class UserProject(user: User, project: Project)

trait Syntax {
  val a = Assigns.syntax("a")
  val u = Users.syntax("u")
  val p = Projects.syntax("p")
}

case class Dao(b: SelectSQLBuilder[User], conditions: List[Option[SQLSyntax]] = Nil)(implicit s: DBSession) extends Syntax {

  def like(column: SQLSyntax, key: String): Option[SQLSyntax] = Some(sqls.like(column, s"%${key}%"))

  def byUserName(key: String) = Dao(b, like(u.name, key) :: conditions)

  def byProjectName(key: String) = Dao(b, like(p.name, key) :: conditions)

  def up(u: SyntaxProvider[User], p: SyntaxProvider[Project])(rs: WrappedResultSet) =
    UserProject(Users(u.resultName)(rs), Projects(p.resultName)(rs))

  def apply(): Iterable[SearchResult] = {
    withSQL {
      b.where(sqls.toAndConditionOpt(conditions: _*))
    }.map(up(u, p)).list().apply().groupBy(_.user).map {
      case (u: User, g: List[UserProject]) => SearchResult(u, g.map(_.project))
    }
  }

}

object Dao extends Syntax {

  def base: SelectSQLBuilder[User] =
    select.from[User](Users as u).
      innerJoin(Assigns as a).on(u.id, a.userId).
      innerJoin(Projects as p).on(p.id, a.projectId)

  def apply(implicit s: DBSession): Dao = Dao(base)

}
