package io.github.dlinov.model.ui

case class UiProject(id: String,
                     title: String,
                     description: String,
                     price: Int,
                     bonus: String,
                     isFinished: Boolean,
                     volunteerIds: Seq[String])
