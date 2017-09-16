package io.github.dlinov.model

import org.mongodb.scala.bson.ObjectId

case class Organization(_id: ObjectId, taskIds: Seq[ObjectId])
