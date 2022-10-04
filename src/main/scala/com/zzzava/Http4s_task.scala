import cats._
import cats.effect._
import cats.implicits._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._
import org.http4s.dsl.impl._
import org.http4s.headers._
import org.http4s.implicits._
import org.http4s.server._
import org.http4s.server.blaze.BlazeServerBuilder
import org.typelevel.ci.CIString


import java.util.UUID
import scala.collection.mutable
import scala.util.Try

object Http4s_task extends IOApp {

  type Employee = String

  case class Worklist(id: String, company: String, employees: List[String])

  case class Company(name: String) {
    override def toString: String = s"$name"
  }

  val snjl: Worklist = Worklist(
    "6bcbca1e-efd3-411d-9f7c-14b872444fce",
    "SomeCompany",
    List("Ivan Ivanov", "Petr Petrov", "Sidr Sidorov"),

  )

  val worklist: Map[String, Worklist] = Map(snjl.id -> snjl)

  object CompanyQueryParamMatcher extends QueryParamDecoderMatcher[String]("company")





  def worklistRoutes[F[_] : Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "worklist" :? CompanyQueryParamMatcher(company) =>
        val WorklistByCompany = findWorklistByCompany(company)
        Ok(WorklistByCompany.asJson)

      case GET -> Root / "worklist" / UUIDVar(worklistId) / "employees" =>
        findWorklistById(worklistId).map(_.employees) match {
          case Some(employees) => Ok(employees.asJson)
          case _ => NotFound(s"No worklist with id $worklistId found")
        }
    }
  }

  private def findWorklistById(worklistId: UUID) =
    worklist.get(worklistId.toString)

  private def findWorklistByCompany(company: String): List[Worklist] =
    worklist.values.filter(_.company == company).toList

  object CompanyVar {
    def unapply(str: String): Option[Company] = {
      if (str.nonEmpty && str.matches(".*")) {
        Try {

          Company(str)
        }.toOption
      } else None
    }

  }

  val companies: mutable.Map[Employee, Company] =
    mutable.Map("SomeCompany" -> Company("SomeCompany"))

  def companyRoutes[F[_] : Concurrent]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._
    implicit val companyDecoder: EntityDecoder[F, Company] = jsonOf[F, Company]
    HttpRoutes.of[F] {
      case GET -> Root / "company" / CompanyVar(company) =>
        companies.get(company.toString) match {
          case Some(com) => Ok(com.asJson, Header.Raw(CIString("My-Custom-Header"), "value"))
          case _ => NotFound(s"No company called $company found")
        }
      case req@POST -> Root / "company" =>
        for {
          company <- req.as[Company]
          _ = companies.put(company.toString, company)
          res <- Ok.headers(`Content-Encoding`(ContentCoding.gzip))
            .map(_.addCookie(ResponseCookie("My-Cookie", "value")))
        } yield res
    }
  }

  def allRoutes[F[_] : Concurrent]: HttpRoutes[F] = {
    worklistRoutes[F] <+> companyRoutes[F]
  }

  def allRoutesComplete[F[_] : Concurrent]: HttpApp[F] = {
    allRoutes.orNotFound
  }

  import scala.concurrent.ExecutionContext.global

  override def run(args: List[String]): IO[ExitCode] = {

    val apis = Router(
      "/api" -> Http4s_task.worklistRoutes[IO],
      "/api/private" -> Http4s_task.companyRoutes[IO]
    ).orNotFound

    BlazeServerBuilder[IO](global)
      .bindHttp(8080, "localhost")
      .withHttpApp(apis)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}