package io.github.dlinov.db.mongo

import io.github.dlinov.db.mongo.MongoDao.FieldNames.fId
import io.github.dlinov.model.{Organization, Reward}
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Updates.set
import org.mongodb.scala.result.UpdateResult

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

class RewardsMongoDao(db: MongoDatabase)
                     (override implicit val ec: ExecutionContext) extends MongoDao[Reward](db) {

  override def collectionName: String = "rewards"

  override implicit val classTag: ClassTag[Reward] = ClassTag(classOf[Reward])

  def assignVolunteerForReward(volunteerId: String, rewardId: String): Future[Option[Reward]] = {
    val volunteerObjId = new ObjectId(volunteerId)
    val rewardObjId = new ObjectId(rewardId)
    collection.updateOne(equal(fId, rewardObjId), set("volunteerId", volunteerObjId))
      .toFuture.flatMap(_ ⇒ findById(rewardId))
  }
}
