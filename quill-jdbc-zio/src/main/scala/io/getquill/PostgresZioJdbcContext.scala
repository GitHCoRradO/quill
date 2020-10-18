package io.getquill

import com.typesafe.config.Config
import io.getquill.context.jdbc.PostgresJdbcContextBase
import io.getquill.context.zio.ZioJdbcContext
import io.getquill.util.LoadConfig
import javax.sql.DataSource

class PostgresZioJdbcContext[N <: NamingStrategy](
  val naming: N
) extends ZioJdbcContext[PostgresDialect, N]
  with PostgresJdbcContextBase[N]

object PostgresZioJdbcContext {
  def withProbeable[N](naming: N, config: Config): PostgresZioJdbcContext[N] =
    new PostgresZioJdbcContext[N](naming) {
      override def probingDataSource: Option[DataSource] = Some(JdbcContextConfig(config).dataSource)
    }
  def withProbeable[N](naming: N, configPrefix: String): PostgresZioJdbcContext[N] =
    PostgresZioJdbcContext.withProbeable[N](naming, LoadConfig(configPrefix))
}

