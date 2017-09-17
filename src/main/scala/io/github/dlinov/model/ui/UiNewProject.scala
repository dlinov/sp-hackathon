package io.github.dlinov.model.ui

import io.github.dlinov.model.Project
import org.bson.types.ObjectId

case class UiNewProject(title: String,
                        description: String,
                        price: Int,
                        bonus: String,
                        organizationId: String) {
  def toProject: Project = Project(
    _id = ObjectId.get(),
    title = title,
    description = description,
    price = price,
    isFinished = false,
    bonus = bonus,
    volunteerIds = Seq.empty
  )
}
