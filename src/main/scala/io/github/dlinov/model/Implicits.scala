package io.github.dlinov.model

import io.github.dlinov.model.ui.UiUser

object Implicits {

  implicit class UserConverter(u: User) {
    def asUI: UiUser = UiUser(
      id = Option(u._id).map(_.toString),
      email = u.email,
      firstName = u.firstName,
      lastName = u.lastName
    )
  }
}
