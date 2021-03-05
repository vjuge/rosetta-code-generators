package com.regnosys.rosetta.generator.typescript.object

import com.regnosys.rosetta.rosetta.RosettaMetaType

import static com.regnosys.rosetta.generator.typescript.util.TypescriptModelGeneratorUtil.*

import static extension com.regnosys.rosetta.generator.util.Util.*
import static extension com.regnosys.rosetta.generator.typescript.util.TypescriptTranslator.toTSType

class TypescriptMetaFieldGenerator {
	
	def generateMetaFields(Iterable<RosettaMetaType> metaTypes, String version) {
		fileComment(version) + metaClasses.toString + metaFields(metaTypes.filter[name !== "reference"], version)
	}
	
		private def metaClasses() '''
		export interface FieldWithMeta<T> {
		  value: T;
		  meta?: MetaFields;
		}

		export interface ReferenceWithMeta<T> {
		  globalReference: String;
		  externalReference: String;
		  value: T;
		}
				
	'''
	
	def metaFields(Iterable<RosettaMetaType> types, String version) '''				
		export interface MetaFields {
			«FOR type : types.distinctBy(t|t.name.toFirstLower)»
				«type.name.toFirstLower»?: «type.type.name.toTSType»;
			«ENDFOR»
			globalKey?: String;
			externalKey?: String;
		}
		
		export interface MetaAndTemplateFields {
			id?: string;
			key?: string;
			reference?: string;
			scheme?: string;
			globalKey?: String;
			externalKey?: String;
		  }
	'''
}
