package io.github.dlinov.db.mongo

import io.github.dlinov.model.Organization
import org.mongodb.scala.MongoDatabase

import scala.concurrent.ExecutionContext
import scala.reflect.ClassTag

class MongoOrganizationsDao(db: MongoDatabase)
                           (override implicit val ec: ExecutionContext) extends MongoDao[Organization](db) {
  override def collectionName: String = "organizations"

  override implicit val classTag: ClassTag[Organization] = ClassTag(classOf[Organization])
}
