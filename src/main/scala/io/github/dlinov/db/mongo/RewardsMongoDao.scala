package io.github.dlinov.db.mongo

import io.github.dlinov.model.{Organization, Reward}
import org.mongodb.scala.MongoDatabase

import scala.concurrent.ExecutionContext
import scala.reflect.ClassTag

class RewardsMongoDao(db: MongoDatabase)
                     (override implicit val ec: ExecutionContext) extends MongoDao[Reward](db) {
  override def collectionName: String = "rewards"

  override implicit val classTag: ClassTag[Reward] = ClassTag(classOf[Reward])
}
