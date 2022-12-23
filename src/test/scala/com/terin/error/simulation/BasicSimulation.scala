package com.terin.error.simulation

import com.github.mnogu.gatling.kafka.Predef._
import com.terin.error.simulation.KafkaConfiguration._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import org.apache.kafka.clients.producer.ProducerConfig

class BasicSimulation extends Simulation {

  private val kafkaConf = kafka.topic(KafkaConfiguration.kafkaTopic)
    .properties(
      Map(
        ProducerConfig.ACKS_CONFIG -> "1",
        // list of Kafka broker hostname and port pairs
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG -> KafkaConfiguration.bootstrapServers,

        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG ->
          "org.apache.kafka.common.serialization.StringSerializer",

        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG ->
          "com.ing.apisdk.toolkit.connectivity.kafka.avro.serde.EncryptingKafkaPayloadAvroSerializer",
        "value.ing.serde.avro.sharedsecret" -> KafkaConfiguration.kafkaSharedSecret,

        //SSL Configuration for Kafka
        "security.protocol" -> "SSL",
        "ssl.key.password" -> KafkaConfiguration.keyPass,
        "ssl.keystore.password" -> KafkaConfiguration.keystorePass,
        "ssl.keystore.location" -> KafkaConfiguration.keystoreLocation,
        "ssl.truststore.password" -> KafkaConfiguration.trustStorePass,
        "ssl.truststore.location" -> KafkaConfiguration.trustStoreLocation,

        //Schema registry configuration
        "schema.registry.url" -> KafkaConfiguration.schemaRegistryUrl,
        //Uncomment for test and comment for local docker setup
        "schema.registry.ssl.keystore.location" -> KafkaConfiguration.keystoreLocation,
        "schema.registry.ssl.keystore.password" -> KafkaConfiguration.keystorePass,
        "schema.registry.ssl.key.password" -> KafkaConfiguration.keyPass,
        "schema.registry.ssl.truststore.location" -> KafkaConfiguration.trustStoreLocation,
        "schema.registry.ssl.truststore.password" -> KafkaConfiguration.trustStorePass,
        "schema.registry.ssl.protocol" -> "SSL",
        "schema.registry.security.protocol" -> "SSL",
        "auto.register.schemas" -> "false",
      )
    )

  private def scn: ScenarioBuilder = scenario("Kafka Test").exec(KafkaMessageBuilder.messages)

  setUp(scn.inject(
    rampUsersPerSec(0) to tps during rampUp,
    constantUsersPerSec(tps) during duration.min(rampUp)
  )).protocols(kafkaConf)

}
