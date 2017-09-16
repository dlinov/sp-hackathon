package io.github.dlinov.model

import java.util.UUID

import org.mongodb.scala.bson.ObjectId

case class Organization(_id: ObjectId, taskIds: Seq[ObjectId])

case class Project(_id: ObjectId,
                   title: String,
                   description: String,
                   price: Int,
                   bonus: String,
                   isFinished: Boolean,
                   volunteerIds: Seq[ObjectId])

case class Reward(_id: ObjectId, title: String, price: Int, code: Option[String])

case class Sponsor(_id: ObjectId, rewardIds: Seq[ObjectId])

case class User(
                 _id: ObjectId,
                 email: String,
                 password: String,
                 firstName: String,
                 lastName: String,
                 accounts: Seq[UUID]
               )

case class Volunteer(_id: ObjectId, completedTaskIds: ObjectId)
