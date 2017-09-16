package io.github.dlinov.model

import org.mongodb.scala.bson.ObjectId

case class Project(_id: ObjectId,
                   title: String,
                   description: String,
                   price: Int,
                   bonus: String,
                   isFinished: Boolean,
                   volunteerIds: Seq[ObjectId])
