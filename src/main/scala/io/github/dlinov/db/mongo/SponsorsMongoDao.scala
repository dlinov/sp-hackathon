package io.github.dlinov.db.mongo

import io.github.dlinov.db.mongo.MongoDao.FieldNames.fId
import io.github.dlinov.model.{Organization, Sponsor}
import org.bson.types.ObjectId
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Updates.addEachToSet

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

class SponsorsMongoDao(db: MongoDatabase)(override implicit val ec: ExecutionContext) extends MongoDao[Sponsor](db) {
  override implicit def classTag: ClassTag[Sponsor] = ClassTag(classOf[Sponsor])

  override def collectionName: String = "sponsors"


  def setRewardsToSponsor(sponsorId: String, rewardIds: Seq[ObjectId]): Future[Option[Sponsor]] = {
    collection.findOneAndUpdate(equal(fId, new ObjectId(sponsorId)), addEachToSet("rewardIds", rewardIds))
      .toFuture.map(Option(_))
  }
}
