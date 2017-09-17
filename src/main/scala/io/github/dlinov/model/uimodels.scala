package io.github.dlinov.model

import io.github.dlinov.model.ui.UiProject
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
                      rewards: Seq[UiReward],
                      email: String,
                      firstName: String,
                      lastName: String
                    )

object UiSponsor {
  def apply2(user: User, rewards: Seq[Reward]): UiSponsor = {
    import user._

    UiSponsor(
      _id.toString,
      rewards.map(UiReward(_)),
      email,
      firstName,
      lastName
    )
  }
}

case class UiReward(
                     id: String, title: String, price: Int, code: Option[String]
                   )

object UiReward {
  def apply(reward: Reward): UiReward = {
    import reward._
    new UiReward(_id.toString, title, price, code)
  }
}

case class UiNewReward(title: String, price: Int, code: Option[String]) {
  def toReward = Reward(ObjectId.get(), title, price, code)
}

case class UiOrganization(id: ObjectId, title: String, projects: Seq[UiProject])

case class UiNewOrganization(title: String, email: String, password: String)
