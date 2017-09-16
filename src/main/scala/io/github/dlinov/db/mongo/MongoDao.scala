package io.github.dlinov.db.mongo

import org.bson.types.ObjectId
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.{Completed, MongoCollection, MongoDatabase}

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

abstract class MongoDao[T](db: MongoDatabase) {
  import MongoDao.FieldNames._

  implicit def classTag: ClassTag[T]

  implicit val ec: ExecutionContext

  def collectionName: String

  def collection: MongoCollection[T] = db.getCollection[T](collectionName)

  protected def findOne(filter: Bson): Future[Option[T]] = {
    collection.find(filter).first().toFuture().map(Option(_))
  }

  protected def find(filter: Bson): Future[Seq[T]] = {
    collection.find(filter).toFuture()
  }

  def findAll: Future[Seq[T]] = find(Document.empty)

  def findById(id: String): Future[Option[T]] = {
    findOne(equal(fId, new ObjectId(id)))
  }

  def findByIds(ids: Seq[String]): Future[Seq[T]] = {
    find(in(fId, ids.map(new ObjectId(_))))
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
