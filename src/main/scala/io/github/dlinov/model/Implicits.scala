package io.github.dlinov.model

import io.github.dlinov.model.ui.UiProject

object Implicits {

  implicit class UserConverter(u: User) {
    def asUI: UiUser = UiUser(
      u._id.toString,
      u.email,
      u.firstName,
      u.lastName
    )
  }

  implicit class ProjectConverter(p: Project) {
    def asUI: UiProject = UiProject(
      id = p._id.toString,
      title = p.title,
      description = p.description,
      price = p.price,
      bonus = p.bonus,
      isFinished = p.isFinished,
      volunteerIds = p.volunteerIds.map(_.toString)
    )
  }

  implicit class OrganizationConverter(o: Organization) {
    def asUI(projects: Seq[UiProject]): UiOrganization = UiOrganization(
      id = o._id.toString,
      title = o.title,
      balance = o.balance,
      projects = projects
    )
  }

  implicit class VolunteerConverter(v: Volunteer) {
    def asUI(user: User, projects: Seq[UiProject]) = UiVolunteer(
      id = v._id.toString,
      completedProjects = projects,
      email = user.email,
      password = user.password,
      firstName = user.firstName,
      lastName = user.lastName,
      balance = v.balance
    )
  }


  implicit class SponsorConverter(s: Sponsor) {
    def asUI(user: User, rewards: Seq[UiReward]): UiSponsor = UiSponsor(
      id = s._id.toString,
      rewards = rewards,
      email = user.email,
      firstName = user.firstName,
      lastName = user.lastName
    )
  }

  implicit class RewardConverter(r: Reward) {
    def asUI: UiReward = UiReward(
      r._id.toString,
      r.title, r.price, r.code
    )
  }
}
