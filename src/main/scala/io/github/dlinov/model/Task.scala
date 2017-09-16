package io.github.dlinov.model

import org.mongodb.scala.bson.ObjectId

case class Task(_id: ObjectId,
                title: String,
                description: String,
                price: Int,
                isFinished: Boolean,
                volunteerIds: Seq[ObjectId])
