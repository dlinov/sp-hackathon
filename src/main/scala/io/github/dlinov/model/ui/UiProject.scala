package io.github.dlinov.model.ui

import org.mongodb.scala.bson.ObjectId

case class UiProject(id: String,
                     title: String,
                     description: String,
                     price: Int,
                     bonus: String,
                     isFinished: Boolean,
                     volunteerIds: Seq[String])
