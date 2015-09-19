import com.softwaremill.macwire._
import com.zaxxer.hikari.HikariDataSource
import controllers.{Assets, Index}
import play.api.ApplicationLoader.Context
import play.api.routing.Router
import play.api._
import router.Routes
import scalikejdbc._
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

object HikariCPFactory extends ConnectionPoolFactory {
  override def apply(url: String, user: String, password: String, settings: ConnectionPoolSettings) = {
    val ds = new HikariDataSource()
    ds.setDriverClassName(settings.driverName)
    ds.setJdbcUrl(url)
    ds.setUsername(user)
    ds.setPassword(password)
    ds.setAutoCommit(false)
    ds.setConnectionTestQuery(settings.validationQuery)
    ds.setMinimumIdle(settings.initialSize)
    ds.setMaximumPoolSize(settings.maxSize)
    ds.setConnectionTimeout(settings.connectionTimeoutMillis)
    ds.setInitializationFailFast(true)
    new DataSourceConnectionPool(ds)
  }
}

trait AppModule {
  ConnectionPoolFactoryRepository.add("hikari-cp", HikariCPFactory)
  DBs.setupAll()
  lazy val index = wire[Index]
}
