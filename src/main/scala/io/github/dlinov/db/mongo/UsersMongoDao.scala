package io.github.dlinov.db.mongo

import io.github.dlinov.db.UsersDao
import io.github.dlinov.model.User
import org.mongodb.scala.MongoClient
import org.mongodb.scala.model.Filters._

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

class UsersMongoDao(mongoClient: MongoClient)
                   (implicit ec: ExecutionContext) extends MongoDao[User](mongoClient) with UsersDao {
  import UsersMongoDao.FieldNames._

  override implicit def classTag: ClassTag[User] = ClassTag[User](classOf[User])

  override val collectionName = "users"

  override def findUser(id: String): Future[User] = findById(id)

  override def findUserByEmail(email: String): Future[User] = {
    findOne(equal(fEmail, email))
  }

  override def createUser(user: User): Future[Unit] = insert(user).map(_ ⇒ ())

  override def checkCredentials(email: String, password: String): Future[Boolean] = {
    for {
      user ← findUserByEmail(email)
    } yield {
      user.password == password
    }
  }
}

object UsersMongoDao {
  object FieldNames {
    val fEmail = "email"
    val fPassword = "password"
    val fFirstName = "firstName"
    val fLastName = "lastName"
  }
}
