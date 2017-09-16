package io.github.dlinov.model

import org.mongodb.scala.bson.ObjectId

case class Reward(_id: ObjectId, title: String, price: Int, code: Option[String])
