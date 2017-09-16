package io.github.dlinov.model

import org.mongodb.scala.bson.ObjectId

case class Volunteer(_id: ObjectId, completedTaskIds: ObjectId)
