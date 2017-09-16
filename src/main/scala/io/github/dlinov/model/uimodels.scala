package io.github.dlinov.model

import java.util.UUID

import org.bson.types.ObjectId
import org.mongodb.scala.bson.ObjectId

case class UiNewUser(email: String, password: String, firstName: String, lastName: String) {
  def toUser = User(ObjectId.get(), email, password, firstName, lastName, Seq())
}

case class UiUser(
                   id: String,
                   email: String,
                   firstName: String,
                   lastName: String
                 )

case class UiVolunteer(
                        id: String,
                        completedTaskIds: ObjectId,
                        email: String,
                        password: String,
                        firstName: String,
                        lastName: String
                      )

case class UiSponsor(
                      id: String,
                      rewardIds: Seq[ObjectId],
                      email: String,
                      password: String,
                      firstName: String,
                      lastName: String
                    )

case class UiNewReward(title: String, price: Int, code: Option[String]) {
  def toReward = Reward(ObjectId.get(), title, price, code)
}
