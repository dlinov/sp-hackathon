package io.github.dlinov.model

import java.util.UUID

import org.mongodb.scala.bson.ObjectId

case class User(
  _id: ObjectId,
  email: String,
  password: String,
  firstName: String,
  lastName: String,
  accounts: Seq[UUID]
)
