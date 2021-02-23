package com.regnosys.rosetta.generator.kotlin.enums

import com.google.inject.Inject
import com.regnosys.rosetta.generator.java.enums.EnumHelper
import com.regnosys.rosetta.generator.kotlin.object.KotlinModelObjectBoilerPlate
import com.regnosys.rosetta.rosetta.RosettaEnumValue
import com.regnosys.rosetta.rosetta.RosettaEnumeration
import java.util.ArrayList
import java.util.HashMap
import java.util.List
import java.util.Map

import static com.regnosys.rosetta.generator.kotlin.util.KotlinModelGeneratorUtil.*

class KotlinEnumGenerator {

    @Inject extension KotlinModelObjectBoilerPlate

    static final String FILENAME = 'Enums.kt'

    def Map<String, ? extends CharSequence> generate(Iterable<RosettaEnumeration> rosettaEnums, String version) {
        val result = new HashMap
        val enums = rosettaEnums.sortBy[name].generateEnums(version).replaceTabsWithSpaces
        result.put(FILENAME,enums)
        return result;
    }

    def static toJavaEnumName(RosettaEnumeration enumeration, RosettaEnumValue rosettaEnumValue) {
        return enumeration.name + '.' + EnumHelper.convertValues(rosettaEnumValue)
    }

    private def allEnumsValues(RosettaEnumeration enumeration) {
        val enumValues = new ArrayList
        var e = enumeration;

        while (e !== null) {
            e.enumValues.forEach[enumValues.add(it)]
            e = e.superType
        }
        return enumValues.sortBy[name];
    }

    private def generateEnums(List<RosettaEnumeration> enums, String version)
		'''
		«fileComment(version)»
		package org.isda.cdm
		import kotlinx.serialization.*
		import kotlinx.serialization.json.*
		import kotlinx.serialization.KSerializer
		import kotlinx.serialization.SerializationException
		import kotlinx.serialization.descriptors.PrimitiveKind
		import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
		import kotlinx.serialization.descriptors.SerialDescriptor
		import kotlinx.serialization.encoding.Decoder
		import kotlinx.serialization.encoding.Encoder
		
		«FOR e : enums SEPARATOR "\n"»
		«val allEnumValues = allEnumsValues(e)»
		«comment(e.definition)»
		@Serializable(with = «e.name».«e.name»Serializer::class)
		enum class «e.name» (val value: String) {
			«FOR value: allEnumValues SEPARATOR ','»
			«comment(value.definition)»
			«EnumHelper.convertValues(value)»«IF value.display !== null»("«value.display»")«ELSE»("«EnumHelper.convertValues(value)»")«ENDIF»
			«ENDFOR»;

			object «e.name»Serializer : KSerializer<«e.name»> {
				override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("«e.name»", PrimitiveKind.STRING)

				override fun serialize(encoder: Encoder, value: «e.name») {
					val string = value.value
					encoder.encodeString(string)
				}

				override fun deserialize(decoder: Decoder): «e.name» {
					val string = decoder.decodeString()
					val map = «e.name».values().associateBy(«e.name»::value)
					return map[string] ?: throw SerializationException("unable to deserialize provided «e.name» with value ${string}")
				}
			}
		}
		«ENDFOR»
		'''

    def boolean anyValueHasSynonym(RosettaEnumeration enumeration) {
        enumeration.allEnumsValues.map[enumSynonyms].flatten.size > 0
    }

}