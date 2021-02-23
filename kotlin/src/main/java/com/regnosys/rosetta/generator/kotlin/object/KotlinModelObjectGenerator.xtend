package com.regnosys.rosetta.generator.kotlin.object

import com.google.inject.Inject
import com.regnosys.rosetta.RosettaExtensions
import com.regnosys.rosetta.generator.object.ExpandedAttribute
import com.regnosys.rosetta.rosetta.RosettaMetaType
import com.regnosys.rosetta.rosetta.simple.Condition
import com.regnosys.rosetta.rosetta.simple.Data

import java.util.HashMap
import java.util.List
import java.util.Map
import java.util.Set

import static com.regnosys.rosetta.generator.kotlin.util.KotlinModelGeneratorUtil.*

import static extension com.regnosys.rosetta.generator.util.RosettaAttributeExtensions.*

class KotlinModelObjectGenerator {

    @Inject extension RosettaExtensions
    @Inject extension KotlinModelObjectBoilerPlate
    @Inject extension KotlinMetaFieldGenerator

    static final String CLASSES_FILENAME = 'Types.kt'
    //static final String INTERFACES_FILENAME = 'Interfaces.kt'
    static final String META_FILENAME = 'Metatypes.kt'
    //static final String SERIALIZATION_FILENAME = 'Serialization.kt' //to implement serialization in pure Kotlin, see https://github.com/Kotlin/kotlinx.serialization


    def Map<String, ? extends CharSequence> generate(Iterable<Data> rosettaClasses, Iterable<RosettaMetaType> metaTypes, String version) {
        val result = new HashMap

        val superTypes = rosettaClasses
                .map[superType]
                .map[allSuperTypes].flatten
                .toSet

        val classes = rosettaClasses.sortBy[name].generateClasses(superTypes, version).replaceTabsWithSpaces
        result.put(CLASSES_FILENAME, classes)

        //val interfaces = superTypes.sortBy[name].generateInterfaces(version).replaceTabsWithSpaces
        // result.put(INTERFACES_FILENAME, interfaces)

        val metaFields = rosettaClasses.sortBy[name].generateMetaFields(metaTypes, version).replaceTabsWithSpaces
        result.put(META_FILENAME, metaFields)


        result;
    }

	/**
	 * Generate the classes
	 */
    private def generateClasses(List<Data> rosettaClasses, Set<Data> superTypes, String version) {
		'''
		«fileComment(version)»
		package org.isda.cdm
		
		import kotlinx.serialization.*		
		import org.isda.cdm.metafields.*

		/**
		* Basic Date implementation
		*/
		@Serializable
		class Date (
		    val year: Int,
		    val month: Int,
		    val day: Int)

		«FOR c : rosettaClasses SEPARATOR "\n"»
		«classComment(c.definition, c.allExpandedAttributes)»
		@Serializable
		open class «c.name»«IF c.superType === null && !superTypes.contains(c)»«ENDIF»«IF c.superType !== null && superTypes.contains(c)»: «c.superType.name»()«ELSEIF c.superType !== null»: «c.superType.name»()«ENDIF»
		{
		«generateAttributes(c)»
		}
		«ENDFOR»
		'''
    }

    private def generateAttributes(Data c) {
        '''«FOR attribute : c.allExpandedAttributes»«generateExpandedAttribute(c, attribute)»«ENDFOR»'''
    }

    private def generateExpandedAttribute(Data c, ExpandedAttribute attribute) {
	   //println(c)
	   //println(attribute)
	   if(attribute.enclosingType == c.name){     
	        if (attribute.enum && !attribute.hasMetas) {
	            if (attribute.singleOptional) {
	                '''
	                    var «attribute.toAttributeName»: «attribute.toType»? = null
	                '''
	            } else {
	                '''
	                    var «attribute.toAttributeName»: «attribute.toType»? = null
	                '''
	            }
	        } else {
	        	if (attribute.singleOptional) {
	                '''
	                    var «attribute.toAttributeName»: «attribute.toType»? = null
	                '''
	            } else {
	                '''
	                    var «attribute.toAttributeName»: «attribute.toType»? = null
	                '''
	            }
	        }
		}        
    }


    private def generateConditionLogic(Data c, Condition condition) {
        '''
		«IF condition.constraint !== null && condition.constraint.oneOf»«generateOneOfLogic(c)»«ENDIF»
        '''
    }

    private def generateOneOfLogic(Data c) {
        //'''
        //val numberOfPopulatedFields = listOf(«FOR attribute : c.allExpandedAttributes SEPARATOR ', '»«attribute.toAttributeName»«ENDFOR»).size
        //assert(numberOfPopulatedFields == 1)
        //'''
    }

//    def dispatch Iterable<ExpandedAttribute> allExpandedAttributes(RosettaClass type) {
//        type.allSuperTypes.expandedAttributes
//    }

    def dispatch Iterable<ExpandedAttribute> allExpandedAttributes(Data type){
        type.allSuperTypes.map[it.expandedAttributes].flatten
    }
    
//    def dispatch String definition(RosettaClass element) {
//        element.definition
//    }
    def dispatch String definition(Data element){
        element.definition
    }
}