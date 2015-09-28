package models

import scalikejdbc._

case class User(id: Int, name: String)
case class Project(id: Int, name: String)
case class Assign(userId: Int, projectId: Int)

object Users extends SQLSyntaxSupport[User] {

  override val tableName = "users"
  override val columns = Seq("id", "name")

  def apply(u: ResultName[User])(rs: WrappedResultSet) =
    new User(id = rs.int(u.id), name = rs.string(u.name))

  val u = Users.syntax("u")

  def find(id: Int)(implicit session: DBSession = autoSession): Option[User] = {
    withSQL {
      select.from(Users as u).where.eq(u.id, id)
    }.map(Users(u.resultName)).single.apply()
  }

}

object Projects extends SQLSyntaxSupport[Project] {

  override val tableName = "projects"
  override val columns = Seq("id", "name")

  def apply(p: ResultName[Project])(rs: WrappedResultSet) =
    new Project(id = rs.int(p.id), name = rs.string(p.name))

}

object Assigns extends SQLSyntaxSupport[Assign] {

  override val tableName = "assigns"
  override val columns = Seq("user_id", "project_id")

  def apply(a: ResultName[Assign])(rs: WrappedResultSet) =
    new Assign(userId = rs.int(a.userId), projectId = rs.int(a.projectId))

}
