package models

import scalikejdbc._

case class User(id: Int, name: String)

object User extends SQLSyntaxSupport[User] {

  override val tableName = "users"
  override val columns = Seq("id", "name")

  def apply(u: ResultName[User])(rs: WrappedResultSet) =
    new User(id = rs.int(u.id), name = rs.string(u.name))

  val u = User.syntax("u")

  def find(id: Int)(implicit session: DBSession = autoSession): Option[User] = {
    withSQL {
      select.from(User as u).where.eq(u.id, id)
    }.map(User(u.resultName)).single.apply()
  }

}
