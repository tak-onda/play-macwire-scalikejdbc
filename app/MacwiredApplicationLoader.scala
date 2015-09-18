import com.softwaremill.macwire._
import controllers.{Assets, Index}
import play.api.ApplicationLoader.Context
import play.api.routing.Router
import play.api.{Application, ApplicationLoader, BuiltInComponents, BuiltInComponentsFromContext}
import router.Routes
import scalikejdbc.config.DBs

class MacwiredApplicationLoader extends ApplicationLoader {
  def load(context: Context): Application = {
    (new BuiltInComponentsFromContext(context) with AppComponents).application
  }
}

trait AppComponents extends BuiltInComponents with AppModule {
  lazy val assets: Assets = wire[Assets]
  lazy val router: Router = wire[Routes] withPrefix "/"
}

trait AppModule {
  DBs.setupAll()
  lazy val index = wire[Index]
}
