package io.github.dlinov.db.mongo

import io.github.dlinov.db.mongo.MongoDao.FieldNames.fId
import io.github.dlinov.model.Organization
import org.bson.types.ObjectId
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

class OrganizationsMongoDao(db: MongoDatabase)
                           (override implicit val ec: ExecutionContext) extends MongoDao[Organization](db) {

  override def collectionName: String = "organizations"

  override implicit val classTag: ClassTag[Organization] = ClassTag(classOf[Organization])

  def registerOrganization(organization: Organization): Future[Option[Organization]] = {
    insert(organization).flatMap(_ ⇒ findById(organization._id.toString))
  }

  def addProjectToOrganization(organizationId: String, projectId: ObjectId): Future[Option[Organization]] = {
    collection.findOneAndUpdate(equal(fId, new ObjectId(organizationId)), addEachToSet("taskIds", projectId))
      .toFuture.map(Option(_))
  }
}
