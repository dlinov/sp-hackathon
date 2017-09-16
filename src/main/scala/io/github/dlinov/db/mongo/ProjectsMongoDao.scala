package io.github.dlinov.db.mongo

import io.github.dlinov.model.Project
import org.mongodb.scala.MongoDatabase

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

class ProjectsMongoDao(db: MongoDatabase)
                      (override implicit val ec: ExecutionContext) extends MongoDao[Project](db) {
  override implicit def classTag: ClassTag[Project] = ClassTag[Project](classOf[Project])

  override def collectionName: String = "tasks"

  def createProject(project: Project): Future[Project] = {
    // it's expected project to have unique id
    insert(project).map(_ ⇒ project)
  }
}
