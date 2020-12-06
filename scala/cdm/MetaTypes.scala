/**
  * This file is auto-generated from the ISDA Common Domain Model, do not edit.
  * Version: test
  */
package org.isda.cdm.metafields

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.module.scala.JsonScalaEnumeration
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

import org.isda.cdm._

case class MetaFields(
    globalKey: Option[String],
    externalKey: Option[String]) {}

case class MetaAndTemplateFields(
    globalKey: Option[String],
    externalKey: Option[String],
    templateGlobalReference: Option[String]) {}
