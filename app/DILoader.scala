import com.softwaremill.macwire._
import com.zaxxer.hikari.HikariDataSource
import controllers.{Assets, Index}
import play.api.ApplicationLoader.Context
import play.api.routing.Router
import play.api._
import router.Routes
import scalikejdbc.{DataSourceConnectionPool, ConnectionPool}
import scalikejdbc.config.DBs

class DILoader extends ApplicationLoader {
  def load(context: Context): Application = {
    (new BuiltInComponentsFromContext(context) with AppComponents {
      Logger.configure(environment)
    }).application
  }
}

trait AppComponents extends BuiltInComponents with AppModule {
  lazy val assets: Assets = wire[Assets]
  lazy val router: Router = wire[Routes] withPrefix "/"
}

trait AppModule {

  DBs.setupAll()
  val ds = new HikariDataSource()
  ds.setDriverClassName("com.mysql.jdbc.Driver")
  ds.setJdbcUrl("jdbc:mysql://localhost/play_development")
  ds.setUsername("webapp")
  ds.setPassword("ppabew")
  ds.setAutoCommit(false)
  ds.setConnectionTestQuery("select 1 as one")
  ds.setMaximumPoolSize(5)
  ds.setInitializationFailFast(true)
  ConnectionPool.singleton(new DataSourceConnectionPool(ds))

  lazy val index = wire[Index]
}
