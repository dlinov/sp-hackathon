package io.github.dlinov.model

import io.github.dlinov.model.ui.UiUser

object Implicits {

  implicit class UserConverter(u: User) {
    def asUI: UiUser = UiUser(
      u._id.toString,
      u.email,
      u.firstName,
      u.lastName
    )
  }
}
