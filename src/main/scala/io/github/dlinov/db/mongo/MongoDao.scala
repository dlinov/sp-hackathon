package io.github.dlinov.db.mongo

import io.github.dlinov.model.User
import org.mongodb.scala.{Completed, MongoClient, MongoCollection, MongoDatabase}
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters._

import scala.concurrent.Future
import scala.reflect.ClassTag

abstract class MongoDao[T](mongoClient: MongoClient) {
  import MongoDao.FieldNames._

  def collectionName: String

  implicit def classTag: ClassTag[T]
  val db: MongoDatabase = mongoClient.getDatabase("mydb").withCodecRegistry(ModelMongoCodecs.codecRegistry)
  def collection: MongoCollection[T] = db.getCollection[T](collectionName)

  protected def findOne(filter: Bson): Future[T] = {
    ???
//    collection.find(filter).first().toFuture()
  }

  protected def find(filter: Bson): Future[Seq[T]] = {
    ???
//    collection.find(filter).toFuture()
  }

  def findById(id: String): Future[T] = {
    findOne(equal(fId, id))
  }

  def insert(obj: T): Future[Completed] = {
    collection.insertOne(obj).toFuture()
  }
}

object MongoDao {
  object FieldNames {
    val fId = "_id"
  }
}
