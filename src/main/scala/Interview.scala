import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import org.jsoup.Jsoup
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object Interview extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: Materializer = Materializer(system)
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  case class UrlRequest(urls: List[String])
  case class UrlResponse(url: String, title: String)

  def fetchTitle(url: String): Future[UrlResponse] = Future {
    val response = Jsoup.connect(url).get()
    val title = response.title()
    UrlResponse(url, title)
  }

  val route =
    path("fetch-titles") {
      post {
        entity(as[UrlRequest]) { request =>
          val titleFutures: Future[List[UrlResponse]] = Future.sequence(request.urls.map(fetchTitle))

          onComplete(titleFutures) {
            case Success(titles) => complete(titles)
            case Failure(ex) => complete(StatusCodes.InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }

  val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

  bindingFuture.onComplete {
    case Success(binding) =>
      println(s"Server is listening on ${binding.localAddress}")
    case Failure(ex) =>
      println(s"Failed to bind HTTP endpoint, terminating system. Reason: ${ex.getMessage}")
      system.terminate()
  }
}
