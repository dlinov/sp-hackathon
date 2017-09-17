package io.github.dlinov.db.mongo

import io.github.dlinov.db.mongo.MongoDao.FieldNames.fId
import io.github.dlinov.model.Project
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._

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

  def applyVolunteer(projectId: String, userId: String): Future[Option[Project]] = {
    val projectObjId = new ObjectId(projectId)
    val userObjectId = new ObjectId(userId)
    collection.findOneAndUpdate(equal(fId, projectObjId), addEachToSet("volunteerIds", userObjectId))
      .toFuture().map(Option(_).map(x ⇒ x.copy(volunteerIds = x.volunteerIds.+:(userObjectId))))
  }

  def finishProject(projectId: String): Future[Option[Project]] = {
    collection.findOneAndUpdate(equal(fId, projectId), set("isFinished", true))
      .toFuture().map(Option(_))
  }
}
