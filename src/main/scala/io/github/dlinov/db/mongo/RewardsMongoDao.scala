package io.github.dlinov.db.mongo

import io.github.dlinov.model.{Organization, Reward}
import org.mongodb.scala.MongoDatabase

import scala.concurrent.ExecutionContext
import scala.reflect.ClassTag

class RewardsMongoDao(db: MongoDatabase)
                     (override implicit val ec: ExecutionContext) extends MongoDao[Organization](db) {
  override def collectionName: String = "rewards"

  override implicit val classTag: ClassTag[Organization] = ClassTag(classOf[Reward])
}
