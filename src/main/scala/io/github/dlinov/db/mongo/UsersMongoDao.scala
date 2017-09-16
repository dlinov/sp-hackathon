package io.github.dlinov.db.mongo

import io.github.dlinov.model.User
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters._

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

class UsersMongoDao(db: MongoDatabase)
                   (override implicit val ec: ExecutionContext) extends MongoDao[User](db) {
  import UsersMongoDao.FieldNames._

  override implicit val classTag: ClassTag[User] = ClassTag[User](classOf[User])

  override val collectionName = "users"

  def findUser(id: String): Future[Option[User]] = findById(id)

  def findUserByEmail(email: String): Future[Option[User]] = {
    findOne(equal(fEmail, email))
  }

  def createUser(user: User): Future[Unit] = insert(user).map(_ ⇒ ())

  def checkCredentials(email: String, password: String): Future[Boolean] = {
    for {
      user ← findUserByEmail(email)
    } yield {
      user.exists(_.password == password)
    }
  }
}

object UsersMongoDao {
  object FieldNames {
    val fEmail = "email"
  }
}
