package models

import scalikejdbc._

case class User(id: Int, name: String)

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
