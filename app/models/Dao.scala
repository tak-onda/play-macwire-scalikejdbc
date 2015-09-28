package models

import scalikejdbc._

trait Dao[T,R] {

  val b: SelectSQLBuilder[T]
  val conditions: List[Option[SQLSyntax]]

  def eq(column: SQLSyntax, key: String): Option[SQLSyntax] = Some(sqls.eq(column, key))
  def like(column: SQLSyntax, key: String): Option[SQLSyntax] = Some(sqls.like(column, s"%${key}%"))

  def or(conditions: Option[SQLSyntax]*): Option[SQLSyntax] = sqls.toOrConditionOpt(conditions: _*)

  def create(rs: WrappedResultSet): R

  def list()(implicit s: DBSession): List[R] = {
    withSQL {
      b.where(sqls.toAndConditionOpt(conditions: _*))
    }.map(create).list().apply()
  }

  def single()(implicit s: DBSession): Option[R] = {
    withSQL {
      b.where(sqls.toAndConditionOpt(conditions: _*))
    }.map(create).single().apply()
  }

}
