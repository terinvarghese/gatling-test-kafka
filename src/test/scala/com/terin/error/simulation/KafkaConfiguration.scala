package com.terin.error.simulation;

import com.typesafe.config.ConfigFactory

import java.util.concurrent.TimeUnit.MINUTES
import scala.concurrent.duration.FiniteDuration

object KafkaConfiguration {

  private val config = ConfigFactory.load()
  private val env = config.getString("env")
  private val tstBootstrapServers = config.getString("kafka.bootstrap.test")
  private val accBootstrapServers = config.getString("kafka.bootstrap.accp")
  private val tstSchemaRegUrl = config.getString("schemaregistry.url.test")
  private val accSchemaRegUrl = config.getString("schemaregistry.url.accp")

  val bootstrapServers: String = if (config.getString("env").equals("accp")) accBootstrapServers else tstBootstrapServers
  val kafkaTopic: String = config.getString("kafka.topic")
  val duration: FiniteDuration = FiniteDuration.apply(config.getString("loadtest.duration").toLong, MINUTES)
  val rampUp: FiniteDuration = FiniteDuration.apply(config.getString("loadtest.rampUp").toLong, MINUTES)
  val tps: Double = config.getDouble("loadtest.tps")

  val schemaRegistryUrl: String = if (config.getString("env").equals("accp")) accSchemaRegUrl else tstSchemaRegUrl
  val keystoreLocation: String = s"src/test/resources/$env/identity.jks"
  val trustStoreLocation: String = s"src/test/resources/$env/http-trust.jks"

  val keystorePass: String = config.getString("schemaregistry.keystorePass")
  val keyPass: String = config.getString("schemaregistry.keyPass")
  val trustStorePass: String = config.getString("schemaregistry.truststorePass")

  val kafkaSharedSecret: String = config.getString("kafka.shared")

  val testUUID: String = config.getString("test.data.uuid")
  val knownUUIDOdds: Int = config.getInt("test.data.odds")

}
