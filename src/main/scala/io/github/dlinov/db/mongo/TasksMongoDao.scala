package io.github.dlinov.db.mongo

import io.github.dlinov.model.Task
import org.mongodb.scala.MongoDatabase

import scala.concurrent.ExecutionContext
import scala.reflect.ClassTag

class TasksMongoDao(db: MongoDatabase)(override implicit val ec: ExecutionContext) extends MongoDao[Task](db) {
  override implicit def classTag: ClassTag[Task] = ClassTag[Task](classOf[Task])

  override def collectionName: String = "tasks"
}
