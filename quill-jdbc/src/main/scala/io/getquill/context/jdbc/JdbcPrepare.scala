package io.getquill.context.jdbc

import java.sql.{Connection, PreparedStatement}

import io.getquill.NamingStrategy
import io.getquill.context.{Context, ContextEffect}
import io.getquill.context.sql.idiom.SqlIdiom
import io.getquill.util.ContextLogger

class JdbcPrepare[Dialect <: SqlIdiom, Naming <: NamingStrategy]
  extends Context[Dialect, Naming]
  with JdbcPrepareBase[Dialect, Naming]{

  type PrepareResult = Connection => Result[PreparedStatement]
  type PrepareBatchResult = Connection => Result[List[PreparedStatement]]

  def prepareQuery[T](sql: String, prepare: Prepare = identityPrepare, extractor: Extractor[T] = identityExtractor): Connection => Result[PreparedStatement] =
    prepareSingle(sql, prepare)

  def prepareAction(sql: String, prepare: Prepare = identityPrepare): Connection => Result[PreparedStatement] =
    prepareSingle(sql, prepare)

  def prepareSingle(sql: String, prepare: Prepare = identityPrepare): Connection => Result[PreparedStatement] =
    (conn: Connection) => prepareInternal(conn, sql, prepare)

  def prepareBatchAction(groups: List[BatchGroup]): Connection => Result[List[PreparedStatement]] =
    (conn: Connection) => prepareBatchInternal(conn, groups, (str:String, prep: Prepare, conn:Connection) => prepareAction(str, prep)(conn))
}
