package io.github.dlinov

import com.typesafe.config.ConfigFactory

trait AppSettings {
  private val environment = System.getenv()
  private val config = ConfigFactory.load()

  private def readFromEnvOrSettings[T](envVar: String, configVar: String): String = {
    Option(environment.get(envVar))
      .getOrElse(config.getConfig("app.settings").getString(configVar))
  }

  val port: Int = readFromEnvOrSettings("PORT", "port").toInt

  val mongodbUri: String = Option(System.getenv("MONGODB_URI")).getOrElse("mongodb://localhost/sp-hackathon")
}
