package models

import scalikejdbc._

case class UserProject(user: User, project: Project)
case class UserProjects(user: User, projects: List[Project])

trait Syntax {
  val a = Assigns.syntax("a")
  val u = Users.syntax("u")
  val p = Projects.syntax("p")
}

case class AssignDao(b: SelectSQLBuilder[User], conditions: List[Option[SQLSyntax]] = Nil)
    extends Dao[User, UserProject] with Syntax {

  def byUserName(key: String) = AssignDao(b, like(u.name, key) :: conditions)
  def byProjectName(key: String) = AssignDao(b, like(p.name, key) :: conditions)

  override def create(rs: WrappedResultSet): UserProject =
    UserProject(Users(u.resultName)(rs), Projects(p.resultName)(rs))

}

object AssignDao extends Syntax {

  def base: SelectSQLBuilder[User] =
    select.from[User](Users as u).
      innerJoin(Assigns as a).on(u.id, a.userId).
      innerJoin(Projects as p).on(p.id, a.projectId)

  def apply(): AssignDao =  AssignDao(base)

}