package com.regnosys.rosetta.generator.kotlin;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.regnosys.rosetta.generator.kotlin.KotlinCodeGenerator;
import com.regnosys.rosetta.rosetta.RosettaModel;
import com.regnosys.rosetta.tests.RosettaInjectorProvider;
import com.regnosys.rosetta.tests.util.ModelHelper;
import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(InjectionExtension.class)
@InjectWith(RosettaInjectorProvider.class)
@SuppressWarnings("all")
public class KotlinModelObjectGeneratorTest {
  @Inject
  @Extension
  private ModelHelper _modelHelper;
  
  @Inject
  private KotlinCodeGenerator generator;
  
  @Inject
  @Extension
  private ParseHelper<RosettaModel> _parseHelper;
  
  @Inject
  private Provider<XtextResourceSet> resourceSetProvider;
  
  @Test
  @Disabled("Test to generate the kotlin for CDM")
  public void generateCdm() {
    try {
      final ArrayList<String> dirs = CollectionLiterals.<String>newArrayList("rosetta-cdm/src/main/rosetta", "rosetta-dsl/com.regnosys.rosetta.lib/src/main/java/model");
      final XtextResourceSet resourceSet = this.resourceSetProvider.get();
      final Function1<String, File> _function = new Function1<String, File>() {
        @Override
        public File apply(final String it) {
          return new File(it);
        }
      };
      final Function1<File, File[]> _function_1 = new Function1<File, File[]>() {
        @Override
        public File[] apply(final File it) {
          final FileFilter _function = new FileFilter() {
            @Override
            public boolean accept(final File it) {
              return it.getName().endsWith(".rosetta");
            }
          };
          return it.listFiles(_function);
        }
      };
      final Function1<File[], List<String>> _function_2 = new Function1<File[], List<String>>() {
        @Override
        public List<String> apply(final File[] it) {
          final Function1<File, byte[]> _function = new Function1<File, byte[]>() {
            @Override
            public byte[] apply(final File it) {
              try {
                return Files.readAllBytes(it.toPath());
              } catch (Throwable _e) {
                throw Exceptions.sneakyThrow(_e);
              }
            }
          };
          final Function1<byte[], String> _function_1 = new Function1<byte[], String>() {
            @Override
            public String apply(final byte[] it) {
              return new String(it);
            }
          };
          return ListExtensions.<byte[], String>map(ListExtensions.<File, byte[]>map(((List<File>)Conversions.doWrapArray(it)), _function), _function_1);
        }
      };
      final Consumer<String> _function_3 = new Consumer<String>() {
        @Override
        public void accept(final String it) {
          try {
            KotlinModelObjectGeneratorTest.this._parseHelper.parse(it, resourceSet);
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
      IterableExtensions.<File[], String>flatMap(ListExtensions.<File, File[]>map(ListExtensions.<String, File>map(dirs, _function), _function_1), _function_2).forEach(_function_3);
      final Function1<Resource, Iterable<RosettaModel>> _function_4 = new Function1<Resource, Iterable<RosettaModel>>() {
        @Override
        public Iterable<RosettaModel> apply(final Resource it) {
          return Iterables.<RosettaModel>filter(it.getContents(), RosettaModel.class);
        }
      };
      final List<RosettaModel> rosettaModels = IterableExtensions.<RosettaModel>toList(Iterables.<RosettaModel>concat(ListExtensions.<Resource, Iterable<RosettaModel>>map(resourceSet.getResources(), _function_4)));
      final Map<String, ? extends CharSequence> generatedFiles = this.generator.afterGenerate(rosettaModels);
      final Path cdmDir = Files.createDirectories(Paths.get("cdm"));
      final BiConsumer<String, CharSequence> _function_5 = new BiConsumer<String, CharSequence>() {
        @Override
        public void accept(final String fileName, final CharSequence contents) {
          try {
            Files.write(cdmDir.resolve(fileName), contents.toString().getBytes());
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
      generatedFiles.forEach(_function_5);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  @Disabled
  public void shouldGenerateEnums() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("enum TestEnum: <\"Test enum description.\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("TestEnumValue1 <\"Test enum value 1\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("TestEnumValue2 <\"Test enum value 2\">");
    _builder.newLine();
    final Map<String, ? extends CharSequence> kotlin = this.generateKotlin(_builder);
    final String enums = kotlin.get("Enums.kt").toString();
    InputOutput.<String>println(enums);
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("/**");
    _builder_1.newLine();
    _builder_1.append(" ");
    _builder_1.append("* This file is auto-generated from the ISDA Common Domain Model, do not edit.");
    _builder_1.newLine();
    _builder_1.append(" ");
    _builder_1.append("* Version: test");
    _builder_1.newLine();
    _builder_1.append(" ");
    _builder_1.append("*/");
    _builder_1.newLine();
    _builder_1.append("package org.isda.cdm");
    _builder_1.newLine();
    _builder_1.append("import kotlinx.serialization.*");
    _builder_1.newLine();
    _builder_1.append("import kotlinx.serialization.json.*");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("// Test enum description.");
    _builder_1.newLine();
    _builder_1.append("@Serializable");
    _builder_1.newLine();
    _builder_1.append("enum class TestEnum {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("// Test enum value 1");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("TEST_ENUM_VALUE_1,");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("// Test enum value 2");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("TEST_ENUM_VALUE_2");
    _builder_1.newLine();
    _builder_1.append("}");
    _builder_1.newLine();
    Assertions.assertTrue(enums.contains(_builder_1));
  }
  
  @Test
  @Disabled
  public void shouldGenerateTypes() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("type TestType: <\"Test type description.\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("testTypeValue1 string (1..1) <\"Test string\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("testTypeValue2 string (0..1) <\"Test optional string\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("testTypeValue3 string (0..*) <\"Test string list\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("testTypeValue4 TestType2 (1..1) <\"Test TestType2\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("testEnum TestEnum (0..1) <\"Optional test enum\">");
    _builder.newLine();
    _builder.newLine();
    _builder.append("type TestType2:");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("testType2Value1 number(1..*) <\"Test number list\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("testType2Value2 date(0..1) <\"Test date\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("testEnum TestEnum (1..1) <\"Optional test enum\">");
    _builder.newLine();
    _builder.newLine();
    _builder.append("enum TestEnum: <\"Test enum description.\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("TestEnumValue1 <\"Test enum value 1\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("TestEnumValue2 <\"Test enum value 2\">");
    _builder.newLine();
    _builder.newLine();
    final Map<String, ? extends CharSequence> kotlin = this.generateKotlin(_builder);
    final String types = kotlin.get("Types.kt").toString();
    InputOutput.<String>println(types);
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("/**");
    _builder_1.newLine();
    _builder_1.append(" ");
    _builder_1.append("* This file is auto-generated from the ISDA Common Domain Model, do not edit.");
    _builder_1.newLine();
    _builder_1.append(" ");
    _builder_1.append("* Version: test");
    _builder_1.newLine();
    _builder_1.append(" ");
    _builder_1.append("*/");
    _builder_1.newLine();
    _builder_1.append("package org.isda.cdm");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("import kotlinx.serialization.*");
    _builder_1.newLine();
    _builder_1.append("import kotlinx.serialization.json.*");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("import org.isda.cdm.metafields.*");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("/**");
    _builder_1.newLine();
    _builder_1.append(" ");
    _builder_1.append("* Test type description.");
    _builder_1.newLine();
    _builder_1.append(" ");
    _builder_1.append("*");
    _builder_1.newLine();
    _builder_1.append(" ");
    _builder_1.append("* @param testEnum Optional test enum");
    _builder_1.newLine();
    _builder_1.append(" ");
    _builder_1.append("* @param testTypeValue1 Test string");
    _builder_1.newLine();
    _builder_1.append(" ");
    _builder_1.append("* @param testTypeValue2 Test optional string");
    _builder_1.newLine();
    _builder_1.append(" ");
    _builder_1.append("* @param testTypeValue3 Test string list");
    _builder_1.newLine();
    _builder_1.append(" ");
    _builder_1.append("* @param testTypeValue4 Test TestType2");
    _builder_1.newLine();
    _builder_1.append(" ");
    _builder_1.append("*/");
    _builder_1.newLine();
    _builder_1.append("@Serializable");
    _builder_1.newLine();
    _builder_1.append("open class TestType");
    _builder_1.newLine();
    _builder_1.append("{");
    _builder_1.newLine();
    _builder_1.append("var testEnum: TestEnum? = null");
    _builder_1.newLine();
    _builder_1.append("lateinit  var testTypeValue1: String");
    _builder_1.newLine();
    _builder_1.append("var testTypeValue2: String? = null");
    _builder_1.newLine();
    _builder_1.append("lateinit  var testTypeValue3: MutableList<String>");
    _builder_1.newLine();
    _builder_1.append("lateinit  var testTypeValue4: TestType2");
    _builder_1.newLine();
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("@Serializable");
    _builder_1.newLine();
    _builder_1.append("open class TestType2");
    _builder_1.newLine();
    _builder_1.append("{");
    _builder_1.newLine();
    _builder_1.append("lateinit  var testEnum: TestEnum");
    _builder_1.newLine();
    _builder_1.append("lateinit  var testType2Value1: MutableList<BigDecimal>");
    _builder_1.newLine();
    _builder_1.append("var testType2Value2: LocalDate? = null");
    _builder_1.newLine();
    _builder_1.append("}");
    _builder_1.newLine();
    Assertions.assertTrue(
      types.contains(_builder_1));
  }
  
  @Test
  public void shouldGenerateTypesExtends() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("        ");
    _builder.append("type TestType extends TestType2:");
    _builder.newLine();
    _builder.append("TestTypeValue1 string (1..1) <\"Test string\">");
    _builder.newLine();
    _builder.append("TestTypeValue2 int (0..1) <\"Test int\">");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("type TestType2 extends TestType3:");
    _builder.newLine();
    _builder.append("\t        ");
    _builder.append("TestType2Value1 number (0..1) <\"Test number\">");
    _builder.newLine();
    _builder.append("\t        ");
    _builder.append("TestType2Value2 date (0..*) <\"Test date\">");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("type TestType3:");
    _builder.newLine();
    _builder.append("\t        ");
    _builder.append("TestType3Value1 string (0..1) <\"Test string\">");
    _builder.newLine();
    _builder.append("\t        ");
    _builder.append("TestType4Value2 int (1..*) <\"Test int\">");
    _builder.newLine();
    final Map<String, ? extends CharSequence> kotlin = this.generateKotlin(_builder);
    final String types = kotlin.get("Types.kt").toString();
    InputOutput.<String>println(types);
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("/**");
    _builder_1.newLine();
    _builder_1.append(" ");
    _builder_1.append("* This file is auto-generated from the ISDA Common Domain Model, do not edit.");
    _builder_1.newLine();
    _builder_1.append(" ");
    _builder_1.append("* Version: test");
    _builder_1.newLine();
    _builder_1.append(" ");
    _builder_1.append("*/");
    _builder_1.newLine();
    _builder_1.append("package org.isda.cdm");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("import kotlinx.serialization.*");
    _builder_1.newLine();
    _builder_1.append("import kotlinx.serialization.json.*");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("import org.isda.cdm.metafields.*");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("@Serializable");
    _builder_1.newLine();
    _builder_1.append("open class TestType: TestType2()");
    _builder_1.newLine();
    _builder_1.append("{");
    _builder_1.newLine();
    _builder_1.append("lateinit  var testTypeValue1: String");
    _builder_1.newLine();
    _builder_1.append("var testTypeValue2: Int? = null");
    _builder_1.newLine();
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("@Serializable");
    _builder_1.newLine();
    _builder_1.append("open class TestType2: TestType3()");
    _builder_1.newLine();
    _builder_1.append("{");
    _builder_1.newLine();
    _builder_1.append("var testType2Value1: BigDecimal? = null");
    _builder_1.newLine();
    _builder_1.append("lateinit  var testType2Value2: MutableList<LocalDate>");
    _builder_1.newLine();
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("@Serializable");
    _builder_1.newLine();
    _builder_1.append("open class TestType3");
    _builder_1.newLine();
    _builder_1.append("{");
    _builder_1.newLine();
    _builder_1.append("var testType3Value1: String? = null");
    _builder_1.newLine();
    _builder_1.append("lateinit  var testType4Value2: MutableList<Int>");
    _builder_1.newLine();
    _builder_1.append("}");
    _builder_1.newLine();
    Assertions.assertTrue(
      types.contains(_builder_1));
  }
  
  @Test
  @Disabled
  public void shouldGenerateMetaTypes() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("metaType reference string");
    _builder.newLine();
    _builder.append("metaType scheme string");
    _builder.newLine();
    _builder.append("metaType id string");
    _builder.newLine();
    _builder.newLine();
    _builder.append("type TestType:");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("[metadata key]");
    _builder.newLine();
    _builder.append("testTypeValue1 TestType2(1..1)");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("[metadata reference]");
    _builder.newLine();
    _builder.newLine();
    _builder.append("enum TestEnum: <\"Test enum description.\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("TestEnumValue1 <\"Test enum value 1\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("TestEnumValue2 <\"Test enum value 2\">");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("type TestType2:");
    _builder.newLine();
    _builder.append("testType2Value1 number (1..1)");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("[metadata reference]");
    _builder.newLine();
    _builder.newLine();
    _builder.append("testType2Value2 string (1..1)");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("[metadata id]");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("[metadata scheme]");
    _builder.newLine();
    _builder.newLine();
    _builder.append("testType2Value3 TestEnum (1..1)");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("[metadata scheme]");
    _builder.newLine();
    final Map<String, ? extends CharSequence> kotlin = this.generateKotlin(_builder);
    final String types = IterableExtensions.join(kotlin.values(), "\n").toString();
    InputOutput.<String>println(types);
    StringConcatenation _builder_1 = new StringConcatenation();
    Assertions.assertTrue(types.contains(_builder_1));
  }
  
  @Test
  @Disabled("TODO fix oneOf code generation for attributes that are Lists")
  public void shouldGenerateOneOfCondition() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("type TestType: <\"Test type with one-of condition.\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("field1 string (0..1) <\"Test string field 1\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("field2 string (0..1) <\"Test string field 2\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("field3 number (0..1) <\"Test number field 3\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("field4 number (0..*) <\"Test number field 4\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("condition: one-of");
    _builder.newLine();
    final Map<String, ? extends CharSequence> kotlin = this.generateKotlin(_builder);
    final String types = kotlin.get("Types.kt").toString();
    InputOutput.<String>println(types);
    StringConcatenation _builder_1 = new StringConcatenation();
    Assertions.assertTrue(types.contains(_builder_1));
  }
  
  public Map<String, ? extends CharSequence> generateKotlin(final CharSequence model) {
    Map<String, ? extends CharSequence> _xblockexpression = null;
    {
      final Resource eResource = this._modelHelper.parseRosettaWithNoErrors(model).eResource();
      _xblockexpression = this.generator.afterGenerate(IterableExtensions.<RosettaModel>toList(Iterables.<RosettaModel>filter(eResource.getContents(), RosettaModel.class)));
    }
    return _xblockexpression;
  }
}
