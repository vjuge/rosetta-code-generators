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
import static extension com.regnosys.rosetta.generator.kotlin.util.KotlinTranslator.toKotlinType
import com.regnosys.rosetta.generator.kotlin.util.KotlinTranslator

class KotlinModelObjectGenerator {

    @Inject extension RosettaExtensions
    @Inject extension KotlinModelObjectBoilerPlate
    @Inject extension KotlinMetaFieldGenerator

    static final String CLASSES_FILENAME = 'Types.kt'
    static final String DSL_FILENAME = 'Types-Dsl.kt'
    static final String META_FILENAME = 'Metatypes.kt'
    static final String DSLMETA_FILENAME = 'Metatypes-Dsl.kt'


    def Map<String, ? extends CharSequence> generate(Iterable<Data> rosettaClasses, Iterable<RosettaMetaType> metaTypes, String version) {
        val result = new HashMap

        val superTypes = rosettaClasses
                .map[superType]
                .map[allSuperTypes].flatten
                .toSet

        val classes = rosettaClasses.sortBy[name].generateClasses(superTypes, version).replaceTabsWithSpaces
        result.put(CLASSES_FILENAME, classes)
        
        val dsl = rosettaClasses.sortBy[name].generateDslForJavaLib(superTypes, version).replaceTabsWithSpaces
        result.put(DSL_FILENAME, dsl)

        val metaFields = rosettaClasses.sortBy[name].generateMetaFields(metaTypes, version).replaceTabsWithSpaces
        result.put(META_FILENAME, metaFields)

 		val metaFieldsDSL = rosettaClasses.sortBy[name].generateMetaFieldsDsl(metaTypes, version).replaceTabsWithSpaces
        result.put(DSLMETA_FILENAME, metaFieldsDSL)

        result;
    }

	/**
	 * Generate the classes
	 */
	 // TODO remove Date implementation in beginning
	 // TODO removed one-of condition due to limitations after instantiation of objects
    private def generateClasses(List<Data> rosettaClasses, Set<Data> superTypes, String version) {
		'''
		«fileComment(version)»
		package org.isda.cdm.kotlin
		
		import kotlinx.serialization.*		

		/**
		* Basic Date implementation
		*/
		@Serializable
		class Date (
		    val year: Int,
		    val month: Int,
		    val day: Int
		)
		
		inline fun <reified T : Any> orCreate(prop: T?): T {
		    return if (prop == null) {
		        val actualRuntimeClassName: String = T::class.qualifiedName!!
		        Class.forName(actualRuntimeClassName).newInstance() as T
		    } else prop
		}

		«FOR c : rosettaClasses SEPARATOR "\n"»
		«classComment(c.definition, c.allExpandedAttributes)»
		@Serializable
		open class «c.name»«IF c.superType === null && !superTypes.contains(c)»«ENDIF»
		(
		«generateAttributes(c)»
		)
		«IF c.superType !== null && superTypes.contains(c)»: «c.superType.name»()«ELSEIF c.superType !== null»: «c.superType.name»()«ENDIF»
		{
«««		«IF c.conditions.size !== 0»
«««			«FOR condition : c.conditions»
«««			«generateConditionLogic(c, condition)»
«««			«ENDFOR»
«««		«ENDIF»
«««			«generateDslFunctions(c)»
		}
		
		«ENDFOR»
		'''
    }
    
    /**
     * Generate Kotlin DSL function for use with the Java distribution of CDM
     */
    private def generateDslForJavaLib(List<Data> rosettaClasses, Set<Data> superTypes, String version) {
		'''
		«FOR c : rosettaClasses SEPARATOR "\n"»
«««		«println("class name: " + c.name)»
		«FOR attribute : c.allExpandedAttributes»
«««	    	for attributes of the current class, not those from inherited class
	    	«IF (attribute.enclosingType == c.name)»
«««	    		«print("attribute name: " + attribute.name)» | «print("Kotlin Type: " + attribute.type.toKotlinType)» | «print("meta: " + attribute.hasMetas)» | «print("type: " + attribute.type.isType)» | «print("enum: " + attribute.enum) »
		        ««« generate dsl function only if attribute is of some properties combination
		        «IF (!attribute.enum && attribute.hasMetas) || (!attribute.enum && attribute.type.isType) || (attribute.enum && attribute.hasMetas)»
			        «IF (!attribute.multiple)»
			        ««« if property is a single CDM object
			        	fun «c.name».«c.name»Builder.«attribute.toAttributeName»(f: «attribute.toTypeForDSL».«attribute.toTypeForDSL»Builder.() -> Unit) = orCreate«attribute.name.toFirstUpper».apply(f)
			        «ELSE»
			        ««« if property is a collection of CDM objects
			        	fun «c.name».«c.name»Builder.«attribute.toAttributeName»(f: «attribute.toTypeForDSL».«attribute.toTypeForDSL»Builder.() -> Unit) = add«attribute.name.toFirstUpper»(«attribute.toTypeForDSL».«attribute.toTypeForDSL»Builder().apply(f).build())
			        «ENDIF»
		        «ENDIF»
	    	«ENDIF»
    	«ENDFOR»		
		«ENDFOR»
		'''
    }

    private def generateAttributes(Data c) {
        '''«FOR attribute : c.allExpandedAttributes»«generateExpandedAttribute(c, attribute)»«ENDFOR»'''
    }
    
    private def generateDslFunctions(Data c) {
    	'''
    	«FOR attribute : c.allExpandedAttributes»
    	«IF (attribute.enclosingType == c.name)»
    	fun «attribute.toAttributeName»(f: «attribute.toType».() -> Unit) = orCreate(«attribute.toAttributeName»).apply(f)
    	«ENDIF»
    	«ENDFOR»
    	'''
    }
    

    private def generateExpandedAttribute(Data c, ExpandedAttribute attribute) {
	   if(attribute.enclosingType == c.name){     
	        if (attribute.enum && !attribute.hasMetas) {
	            if (attribute.singleOptional) {
	                '''
	                var «attribute.toAttributeName»: «attribute.toType»? = null,
	                '''
	            } else {
	                '''
	                var «attribute.toAttributeName»: «attribute.toType»? = null,
	                '''
	            }
	        } else {
	        	if (attribute.singleOptional) {
	                '''
	                var «attribute.toAttributeName»: «attribute.toType»? = null,
	                '''
	            } else {
	                '''
	                var «attribute.toAttributeName»: «attribute.toType»? = null,
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
        '''
	        init {
	        	require(listOfNotNull(«FOR attribute : c.allExpandedAttributes SEPARATOR ', '»«attribute.toAttributeName»«ENDFOR»).size == 1)
	        }
        '''
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