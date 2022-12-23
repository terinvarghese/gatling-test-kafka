package com.terin.error.simulation

import com.github.mnogu.gatling.kafka.Predef._
import com.terin.error.helpers.{IdType, KafkaErrorEvent}
import com.terin.error.simulation.KafkaConfiguration.{knownUUIDOdds, testUUID}
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import scala.util.Random

object KafkaMessageBuilder {

  private val idTypes = List(
    IdType.HOMEID,
    IdType.EMPLOYEEID,
    IdType.PERSONALID
  )

/*  private val customFeeder = Iterator.continually(Map(
    "randomMobileErrorEvent" -> com.terin.error.helpers.MobileErrorEvent.newBuilder
      .setId(randomUUID())
      .setIdType(idTypes.random)
      .setApplication(Random.alphanumeric.take(10).mkString)
      .setErrorCode(s"ERR00${Random.nextInt(100)}")
      .setDescription(Random.alphanumeric.take(100).mkString)
      .setZonedDateTime(ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
      .build()
  ))*/

  private val customFeeder = Iterator.continually(Map(
    "randomKafkaEvent" -> KafkaErrorEvent.builder()
      .id(randomUUID())
      .idType(idTypes.random)
      .application(Random.alphanumeric.take(10).toString())
      .errorCode(s"ERR00${Random.nextInt(100)}")
      .description(Random.alphanumeric.take(100).mkString)
      .zonedDateTime(ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
      .build()
  ))

  private def randomUUID() = {
    if (Random.nextInt(knownUUIDOdds) == 0) testUUID
    else UUID.randomUUID().toString
  }

  def messages: ChainBuilder = exec(
    exec(feed(customFeeder)
      .exec(kafka("IDErrorMessage")
        .send[KafkaErrorEvent]("${randomKafkaEvent}")))
  )

  private implicit class ListOps[A](list: List[A]) {
    def random: A = list(Random.nextInt(list.size))
  }

}
