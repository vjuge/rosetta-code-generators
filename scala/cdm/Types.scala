/**
  * This file is auto-generated from the ISDA Common Domain Model, do not edit.
  * Version: test
  */
package org.isda.cdm

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.module.scala.JsonScalaEnumeration
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

import org.isda.cdm.metafields._

/**
  * A sample data
  *
  * @param intAttribute 
  * @param multipleAttribute 
  * @param stringAttribute 
  */
case class DataFoo(intAttribute: ,
    multipleAttribute: List[Foo],
    stringAttribute: ) {
}

/**
  * A sample class
  *
  * @param dataAttribute 
  * @param intAttribute 
  * @param multipleAttribute 
  * @param stringAttribute 
  */
case class Foo(dataAttribute: DataFoo,
    intAttribute: ,
    multipleAttribute: List[],
    stringAttribute: ) {
}

