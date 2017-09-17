package io.github.dlinov.db.mongo

import io.github.dlinov.db.mongo.MongoDao.FieldNames.fId
import io.github.dlinov.model.Volunteer
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.result.UpdateResult

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

class VolunteersMongoDao(db: MongoDatabase)(override implicit val ec: ExecutionContext) extends MongoDao[Volunteer](db) {

  override implicit def classTag: ClassTag[Volunteer] = ClassTag(classOf[Volunteer])

  override def collectionName: String = "volunteers"

  def addPointsTo(volunteerIds: Seq[ObjectId], points: Int): Future[Option[UpdateResult]] = {
    collection.updateMany(in(fId, volunteerIds: _*), inc("balance", points))
      .toFuture.map(Option(_))
  }

  def createVolunteer(volunteer: Volunteer): Future[Volunteer] = {
    // volunteer is expected to have unique (user) id
    insert(volunteer).map(_ ⇒ volunteer)
  }
}
