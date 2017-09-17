package io.github.dlinov.model

import java.util.UUID

import org.mongodb.scala.bson.ObjectId

trait MongoObject {
  val _id: ObjectId
}

case class Organization(_id: ObjectId, title: String, balance: Int, taskIds: Seq[ObjectId]) extends MongoObject

case class Project(_id: ObjectId,
                   title: String,
                   description: String,
                   price: Int,
                   bonus: String,
                   isFinished: Boolean,
                   volunteerIds: Seq[ObjectId]) extends MongoObject

case class Reward(_id: ObjectId, title: String, price: Int, code: Option[String]) extends MongoObject

case class Sponsor(_id: ObjectId, rewardIds: Seq[ObjectId]) extends MongoObject

case class User(
                 _id: ObjectId,
                 email: String,
                 password: String,
                 firstName: String,
                 lastName: String
               ) extends MongoObject

case class Volunteer(_id: ObjectId, completedTaskIds: ObjectId, balance: Int) extends MongoObject
