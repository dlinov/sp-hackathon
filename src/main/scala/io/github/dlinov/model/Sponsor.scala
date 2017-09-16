package io.github.dlinov.model

import org.mongodb.scala.bson.ObjectId

case class Sponsor(_id: ObjectId, rewardIds: Seq[ObjectId])
