package io.github.dlinov.db

import io.github.dlinov.model.User

import scala.concurrent.Future

trait UsersDao {
  def findUser(id: String): Future[User]

  def findUserByEmail(email: String): Future[User]

  def createUser(user: User): Future[Unit]

  def checkCredentials(email: String, password: String): Future[Boolean]
}
