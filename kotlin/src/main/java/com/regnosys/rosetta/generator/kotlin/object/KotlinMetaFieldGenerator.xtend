package com.regnosys.rosetta.generator.kotlin.object

import com.regnosys.rosetta.generator.object.ExpandedType
import com.regnosys.rosetta.rosetta.RosettaMetaType
import com.regnosys.rosetta.rosetta.simple.Data
import java.util.List

import static com.regnosys.rosetta.generator.kotlin.util.KotlinModelGeneratorUtil.*

import static extension com.regnosys.rosetta.generator.kotlin.object.KotlinModelObjectBoilerPlate.*
import static extension com.regnosys.rosetta.generator.kotlin.util.KotlinTranslator.*
import static extension com.regnosys.rosetta.generator.util.RosettaAttributeExtensions.*
import static extension com.regnosys.rosetta.generator.util.Util.*

class KotlinMetaFieldGenerator {

    def generateMetaFields(List<Data> rosettaClasses, Iterable<RosettaMetaType> metaTypes, String version) {
        val metaFieldsImports = generateMetaFieldsImports.toString

        val refs = rosettaClasses
                .flatMap[expandedAttributes]
                .filter[hasMetas && metas.exists[name=="reference"]]
                .map[type]
                .toSet

        var referenceWithMeta = '';

        for (ref:refs) {
            if (ref.isType)
                referenceWithMeta += generateReferenceWithMeta(ref).toString
            else
                referenceWithMeta += generateBasicReferenceWithMeta(ref).toString
        }

        val metas =  rosettaClasses
                .flatMap[expandedAttributes]
                .filter[hasMetas && !metas.exists[name=="reference"]]
                .map[type]
                .toSet

        for (meta:metas) {
            referenceWithMeta += generateFieldWithMeta(meta).toString
        }

        val metaFields = genMetaFields(metaTypes.filter[t|t.name!="id" && t.name!="reference"], version)

        return fileComment(version) + metaFieldsImports + referenceWithMeta + metaFields
    }

    private def generateMetaFieldsImports() '''
            package org.isda.cdm.metafields

		import kotlinx.serialization.*
        import kotlinx.serialization.json.*

        import org.isda.cdm.*
        
            '''

    private def generateFieldWithMeta(ExpandedType type) '''
    @Serializable
    open class FieldWithMeta«type.toMetaTypeName»{
    	«generateAttribute(type)»,
    meta: MetaFields?
    }

	'''

    private def generateAttribute(ExpandedType type) {
        if (type.enumeration) {
            '''value: «type.toKotlinType»?'''
        } else {
            '''value: «type.toKotlinType»?'''
        }
    }

    private def generateReferenceWithMeta(ExpandedType type) '''
    @Serializable
    open class ReferenceWithMeta«type.toMetaTypeName»(value: «type.toKotlinType»?,
    globalReference: String?,
    externalReference: String?) {}

	'''
    private def generateBasicReferenceWithMeta(ExpandedType type) '''
    @Serializable
    open class BasicReferenceWithMeta«type.toMetaTypeName»(value: «type.toKotlinType»?,
    globalReference: String?,
    externalReference: String?) {}

	'''

    private def genMetaFields(Iterable<RosettaMetaType> types, String version) '''
    @Serializable
    open class MetaFields(«FOR type : types.distinctBy(t|t.name.toFirstLower) SEPARATOR '\n		'»«type.name.toFirstLower»: «type.type.name.toKotlinBasicType»?,«ENDFOR»
    globalKey: String?,
    externalKey: String?) {}

	'''
}