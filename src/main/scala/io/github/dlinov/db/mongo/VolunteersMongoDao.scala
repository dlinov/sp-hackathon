package io.github.dlinov.db.mongo

import io.github.dlinov.model.Volunteer
import org.mongodb.scala.MongoDatabase

import scala.concurrent.ExecutionContext
import scala.reflect.ClassTag

class VolunteersMongoDao(db: MongoDatabase)(override implicit val ec: ExecutionContext) extends MongoDao[Volunteer](db) {
  override implicit def classTag: ClassTag[Volunteer] = ClassTag(classOf[Volunteer])

  override def collectionName: String = "volunteers"
}
