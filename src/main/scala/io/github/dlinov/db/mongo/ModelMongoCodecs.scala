package io.github.dlinov.db.mongo

import io.github.dlinov.model._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._

object ModelMongoCodecs {
  val codecRegistry: CodecRegistry = fromRegistries(
    fromProviders(
      classOf[Organization], classOf[Reward], classOf[Sponsor], classOf[Project], classOf[User], classOf[Volunteer]
    ),
    DEFAULT_CODEC_REGISTRY
  )
}
