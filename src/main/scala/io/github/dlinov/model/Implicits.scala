package io.github.dlinov.model

import io.github.dlinov.model.ui.UiProject

object Implicits {

  implicit class UserConverter(u: User) {
    def asUI: UiUser = UiUser(
      u._id.toString,
      u.email,
      u.firstName,
      u.lastName
    )
  }

  implicit class ProjectConverter(p: Project) {
    def asUI: UiProject = UiProject(
      id = p._id.toString,
      title = p.title,
      description = p.description,
      price = p.price,
      bonus = p.bonus,
      isFinished = p.isFinished,
      volunteerIds = p.volunteerIds.map(_.toString)
    )
  }
}
