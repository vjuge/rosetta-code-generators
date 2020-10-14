package com.regnosys.rosetta.generator.kotlin

import com.google.inject.Inject
import com.google.inject.Provider
import com.regnosys.rosetta.rosetta.RosettaModel
import com.regnosys.rosetta.tests.RosettaInjectorProvider
import com.regnosys.rosetta.tests.util.ModelHelper
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.junit.jupiter.api.Assertions.*

@ExtendWith(InjectionExtension)
@InjectWith(RosettaInjectorProvider)
class KotlinModelObjectGeneratorTest {

    @Inject extension ModelHelper
    @Inject KotlinCodeGenerator generator;

    @Inject extension ParseHelper<RosettaModel>
    @Inject Provider<XtextResourceSet> resourceSetProvider;

    @Test
    //@Disabled("Test to generate the kotlin for CDM")
    def void generateCdm() {
        val dirs = newArrayList(
                //('/Users/hugohills/code/src/github.com/REGnosys/rosetta-cdm/src/main/rosetta'),
                //('/Users/hugohills/code/src/github.com/REGnosys/rosetta-dsl/com.regnosys.rosetta.lib/src/main/java/model')
                //('rosetta-cdm/src/main/rosetta'),
                //('rosetta-dsl/com.regnosys.rosetta.lib/src/main/java/model')
                ('/Users/vincentjuge/devel/vjuge/rosetta-code-generators/kotlin/src/test/resources/rosetta-samples')
        );

        val resourceSet = resourceSetProvider.get

        dirs.map[new File(it)].map[listFiles[it.name.endsWith('.rosetta')]].flatMap[
                map[Files.readAllBytes(toPath)].map[new String(it)]
                ].forEach[parse(resourceSet)]

        val rosettaModels = resourceSet.resources.map[contents.filter(RosettaModel)].flatten.toList

        val generatedFiles = generator.afterGenerate(rosettaModels)

        val cdmDir = Files.createDirectories(Paths.get("cdm"))
        generatedFiles.forEach [ fileName, contents |
                Files.write(cdmDir.resolve(fileName), contents.toString.bytes)
		]
    }

    @Test
    //@Disabled
    def void shouldGenerateEnums() {
        val kotlin = '''
        enum TestEnum: <"Test enum description.">
                TestEnumValue1 <"Test enum value 1">
                TestEnumValue2 <"Test enum value 2">
                '''.generateKotlin

        val enums = kotlin.get('Enums.kt').toString
        println(enums)
        assertTrue(enums.contains('''
		/**
		 * This file is auto-generated from the ISDA Common Domain Model, do not edit.
		 * Version: test
		 */
		package org.isda.cdm
		import kotlinx.serialization.*
		import kotlinx.serialization.json.*
		
		// Test enum description.
		@Serializable
		enum class TestEnum {
		  // Test enum value 1
		  TEST_ENUM_VALUE_1,
		  // Test enum value 2
		  TEST_ENUM_VALUE_2
		}
        '''))
    }

    @Test
    //@Disabled
    def void shouldGenerateTypes() {
        val kotlin = '''
        type TestType: <"Test type description.">
                testTypeValue1 string (1..1) <"Test string">
                testTypeValue2 string (0..1) <"Test optional string">
                testTypeValue3 string (0..*) <"Test string list">
                testTypeValue4 TestType2 (1..1) <"Test TestType2">
                testEnum TestEnum (0..1) <"Optional test enum">

        type TestType2:
        		testType2Value1 number(1..*) <"Test number list">
                testType2Value2 date(0..1) <"Test date">
                testEnum TestEnum (1..1) <"Optional test enum">

        enum TestEnum: <"Test enum description.">
                TestEnumValue1 <"Test enum value 1">
                TestEnumValue2 <"Test enum value 2">

                '''.generateKotlin

        val types = kotlin.get('Types.kt').toString
        println(types)
        assertTrue(types.contains(
        '''
		/**
		 * This file is auto-generated from the ISDA Common Domain Model, do not edit.
		 * Version: test
		 */
		package org.isda.cdm
		
		import kotlinx.serialization.*    
		import org.isda.cdm.metafields.*
		
		/**
		 * Test type description.
		 *
		 * @param testEnum Optional test enum
		 * @param testTypeValue1 Test string
		 * @param testTypeValue2 Test optional string
		 * @param testTypeValue3 Test string list
		 * @param testTypeValue4 Test TestType2
		 */
		@Serializable
		open class TestType
		{
		var testEnum: TestEnum? = null
		lateinit var testTypeValue1: String
		var testTypeValue2: String? = null
		lateinit var testTypeValue3: MutableList<String>
		lateinit var testTypeValue4: TestType2
		}
		
		@Serializable
		open class TestType2
		{
		lateinit var testEnum: TestEnum
		lateinit var testType2Value1: MutableList<BigDecimal>
		var testType2Value2: LocalDate? = null
		}
        '''))

    }

    @Test
    //@Disabled
    def void shouldGenerateTypesExtends() {
        val kotlin = 
        '''
        type TestType extends TestType2:
		    TestTypeValue1 string (1..1) <"Test string">
		    TestTypeValue2 int (0..1) <"Test int">

        type TestType2 extends TestType3:
	        TestType2Value1 number (0..1) <"Test number">
	        TestType2Value2 date (0..*) <"Test date">

        type TestType3:
	        TestType3Value1 string (0..1) <"Test string">
	        TestType4Value2 int (1..*) <"Test int">
        '''.generateKotlin


        val types = kotlin.get('Types.kt').toString
        println(types)
        assertTrue(types.contains(
        '''
		/**
		 * This file is auto-generated from the ISDA Common Domain Model, do not edit.
		 * Version: test
		 */
		package org.isda.cdm
		
		import kotlinx.serialization.*    
		import org.isda.cdm.metafields.*
		
		@Serializable
		open class TestType: TestType2()
		{
		lateinit var testTypeValue1: String
		var testTypeValue2: Int? = null
		}
		
		@Serializable
		open class TestType2: TestType3()
		{
		var testType2Value1: BigDecimal? = null
		lateinit var testType2Value2: MutableList<LocalDate>
		}
		
		@Serializable
		open class TestType3
		{
		var testType3Value1: String? = null
		lateinit var testType4Value2: MutableList<Int>
		}
        '''))
    }

    @Test
    //@Disabled
    def void shouldGenerateMetaTypes() {
        val kotlin = '''
		metaType reference string
		metaType scheme string
		metaType id string
		
		type TestType:
			[metadata key]
			testTypeValue1 TestType2(1..1)
				[metadata reference]
		
		enum TestEnum: 
		        TestEnumValue1 
		        TestEnumValue2 
		
		type TestType2:
			testType2Value1 number (1..1)
					[metadata reference]
			testType2Value2 string (1..1)
					[metadata id]
					[metadata scheme]
			testType2Value3 TestEnum (1..1)
					[metadata scheme]
        '''.generateKotlin

        val types = kotlin.values.join('\n').toString
        println(types)
        assertTrue(types.contains(
        '''
        @Serializable
        enum class TestEnum {
          TEST_ENUM_VALUE_1,
          TEST_ENUM_VALUE_2
        }
        '''))
        assertTrue(types.contains(
        '''
        '''))

    }

    @Test
    //@Disabled("TODO fix oneOf code generation for attributes that are Lists")
    def void shouldGenerateOneOfCondition() {
        val kotlin = '''
        type TestType: <"Test type with one-of condition.">
                field1 string (0..1) <"Test string field 1">
                field2 string (0..1) <"Test string field 2">
                field3 number (0..1) <"Test number field 3">
                field4 number (0..*) <"Test number field 4">
                condition: one-of
        '''.generateKotlin

        val types = kotlin.get('Types.kt').toString
        println(types)
        assertTrue(types.contains('''
        '''))
    }

    def generateKotlin(CharSequence model) {
        val eResource = model.parseRosettaWithNoErrors.eResource

        generator.afterGenerate(eResource.contents.filter(RosettaModel).toList)
    }
}
