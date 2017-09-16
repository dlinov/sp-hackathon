package io.github.dlinov.model.ui

import io.github.dlinov.model.User
import org.bson.types.ObjectId

case class UiNewUser(email: String, password: String, firstName: String, lastName: String) {
  def toUser = User(ObjectId.get(), email, password, firstName, lastName, Seq())
}
