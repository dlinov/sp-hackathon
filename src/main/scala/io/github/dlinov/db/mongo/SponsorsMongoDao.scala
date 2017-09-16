package io.github.dlinov.db.mongo

import io.github.dlinov.model.Sponsor
import org.mongodb.scala.MongoDatabase

import scala.concurrent.ExecutionContext
import scala.reflect.ClassTag

class SponsorsMongoDao(db: MongoDatabase)(override implicit val ec: ExecutionContext) extends MongoDao[Sponsor](db) {
  override implicit def classTag: ClassTag[Sponsor] = ClassTag(classOf[Sponsor])

  override def collectionName: String = "sponsors"
}
