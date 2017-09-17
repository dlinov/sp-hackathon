package io.github.dlinov.model

import io.github.dlinov.model.ui.UiProject
import org.bson.types.ObjectId
import org.mongodb.scala.bson.ObjectId

case class UiNewUser(email: String, password: String, firstName: String, lastName: String) {
  def toUser = User(ObjectId.get(), email, password, firstName, lastName)
}

case class UiUser(
                   id: String,
                   email: String,
                   firstName: String,
                   lastName: String
                 )

case class UiVolunteer(
                        id: String,
                        completedProjects: Seq[UiProject],
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

case class UiOrganization(id: String, title: String, balance: Int, projects: Seq[UiProject])

case class UiNewOrganization(title: String, email: String, password: String) {
  def toOrganization(userId: ObjectId): Organization = Organization(
    _id = userId,
    title = title,
    balance = 0,
    taskIds = Seq.empty
  )
}

case class UiVolunteerApplication(userId: String, projectId: String)
