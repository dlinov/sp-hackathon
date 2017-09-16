package io.github.dlinov.model.ui

import java.util.UUID

case class UiUser(
  id: Option[String],
  email: String,
  firstName: String,
  lastName: String
)
