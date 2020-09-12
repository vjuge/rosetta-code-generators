package com.regnosys.rosetta.generator.kotlin.object;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.regnosys.rosetta.RosettaExtensions;
import com.regnosys.rosetta.generator.kotlin.object.KotlinMetaFieldGenerator;
import com.regnosys.rosetta.generator.kotlin.object.KotlinModelObjectBoilerPlate;
import com.regnosys.rosetta.generator.kotlin.util.KotlinModelGeneratorUtil;
import com.regnosys.rosetta.generator.object.ExpandedAttribute;
import com.regnosys.rosetta.generator.util.RosettaAttributeExtensions;
import com.regnosys.rosetta.rosetta.RosettaClass;
import com.regnosys.rosetta.rosetta.RosettaMetaType;
import com.regnosys.rosetta.rosetta.simple.Condition;
import com.regnosys.rosetta.rosetta.simple.Data;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class KotlinModelObjectGenerator {
  @Inject
  @Extension
  private RosettaExtensions _rosettaExtensions;
  
  @Inject
  @Extension
  private KotlinModelObjectBoilerPlate _kotlinModelObjectBoilerPlate;
  
  @Inject
  @Extension
  private KotlinMetaFieldGenerator _kotlinMetaFieldGenerator;
  
  private static final String CLASSES_FILENAME = "Types.kt";
  
  private static final String META_FILENAME = "Metatypes.kt";
  
  public Map<String, ? extends CharSequence> generate(final Iterable<Data> rosettaClasses, final Iterable<RosettaMetaType> metaTypes, final String version) {
    HashMap<String, String> _xblockexpression = null;
    {
      final HashMap<String, String> result = new HashMap<String, String>();
      final Function1<Data, Data> _function = new Function1<Data, Data>() {
        @Override
        public Data apply(final Data it) {
          return it.getSuperType();
        }
      };
      final Function1<Data, Set<Data>> _function_1 = new Function1<Data, Set<Data>>() {
        @Override
        public Set<Data> apply(final Data it) {
          return KotlinModelObjectGenerator.this._rosettaExtensions.getAllSuperTypes(it);
        }
      };
      final Set<Data> superTypes = IterableExtensions.<Data>toSet(Iterables.<Data>concat(IterableExtensions.<Data, Set<Data>>map(IterableExtensions.<Data, Data>map(rosettaClasses, _function), _function_1)));
      final Function1<Data, String> _function_2 = new Function1<Data, String>() {
        @Override
        public String apply(final Data it) {
          return it.getName();
        }
      };
      final String classes = this._kotlinModelObjectBoilerPlate.replaceTabsWithSpaces(this.generateClasses(IterableExtensions.<Data, String>sortBy(rosettaClasses, _function_2), superTypes, version));
      result.put(KotlinModelObjectGenerator.CLASSES_FILENAME, classes);
      final Function1<Data, String> _function_3 = new Function1<Data, String>() {
        @Override
        public String apply(final Data it) {
          return it.getName();
        }
      };
      final String metaFields = this._kotlinModelObjectBoilerPlate.replaceTabsWithSpaces(this._kotlinMetaFieldGenerator.generateMetaFields(IterableExtensions.<Data, String>sortBy(rosettaClasses, _function_3), metaTypes, version));
      result.put(KotlinModelObjectGenerator.META_FILENAME, metaFields);
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  /**
   * Generate the classes
   */
  private CharSequence generateClasses(final List<Data> rosettaClasses, final Set<Data> superTypes, final String version) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _fileComment = KotlinModelGeneratorUtil.fileComment(version);
    _builder.append(_fileComment);
    _builder.newLineIfNotEmpty();
    _builder.append("package org.isda.cdm");
    _builder.newLine();
    _builder.newLine();
    _builder.append("import kotlinx.serialization.*");
    _builder.newLine();
    _builder.append("import kotlinx.serialization.json.*");
    _builder.newLine();
    _builder.newLine();
    _builder.append("import org.isda.cdm.metafields.*");
    _builder.newLine();
    _builder.newLine();
    {
      boolean _hasElements = false;
      for(final Data c : rosettaClasses) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate("\n", "");
        }
        CharSequence _classComment = KotlinModelGeneratorUtil.classComment(c.getDefinition(), this.allExpandedAttributes(c));
        _builder.append(_classComment);
        _builder.newLineIfNotEmpty();
        _builder.append("@Serializable");
        _builder.newLine();
        _builder.append("open class ");
        String _name = c.getName();
        _builder.append(_name);
        {
          if (((c.getSuperType() == null) && (!superTypes.contains(c)))) {
          }
        }
        {
          if (((c.getSuperType() != null) && superTypes.contains(c))) {
            _builder.append(": ");
            String _name_1 = c.getSuperType().getName();
            _builder.append(_name_1);
            _builder.append("()");
          } else {
            Data _superType = c.getSuperType();
            boolean _tripleNotEquals = (_superType != null);
            if (_tripleNotEquals) {
              _builder.append(": ");
              String _name_2 = c.getSuperType().getName();
              _builder.append(_name_2);
              _builder.append("()");
            }
          }
        }
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        CharSequence _generateAttributes = this.generateAttributes(c);
        _builder.append(_generateAttributes);
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  private CharSequence generateAttributes(final Data c) {
    StringConcatenation _builder = new StringConcatenation();
    {
      Iterable<ExpandedAttribute> _allExpandedAttributes = this.allExpandedAttributes(c);
      for(final ExpandedAttribute attribute : _allExpandedAttributes) {
        CharSequence _generateExpandedAttribute = this.generateExpandedAttribute(c, attribute);
        _builder.append(_generateExpandedAttribute);
      }
    }
    return _builder;
  }
  
  private CharSequence generateExpandedAttribute(final Data c, final ExpandedAttribute attribute) {
    CharSequence _xifexpression = null;
    String _enclosingType = attribute.getEnclosingType();
    String _name = c.getName();
    boolean _equals = Objects.equal(_enclosingType, _name);
    if (_equals) {
      CharSequence _xifexpression_1 = null;
      if ((attribute.isEnum() && (!attribute.hasMetas()))) {
        CharSequence _xifexpression_2 = null;
        boolean _isSingleOptional = attribute.isSingleOptional();
        if (_isSingleOptional) {
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("var ");
          CharSequence _attributeName = this._kotlinModelObjectBoilerPlate.toAttributeName(attribute);
          _builder.append(_attributeName);
          _builder.append(": ");
          CharSequence _type = this._kotlinModelObjectBoilerPlate.toType(attribute);
          _builder.append(_type);
          _builder.append(" = null");
          _builder.newLineIfNotEmpty();
          _xifexpression_2 = _builder;
        } else {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("lateinit\tvar ");
          CharSequence _attributeName_1 = this._kotlinModelObjectBoilerPlate.toAttributeName(attribute);
          _builder_1.append(_attributeName_1);
          _builder_1.append(": ");
          CharSequence _type_1 = this._kotlinModelObjectBoilerPlate.toType(attribute);
          _builder_1.append(_type_1);
          _builder_1.newLineIfNotEmpty();
          _xifexpression_2 = _builder_1;
        }
        _xifexpression_1 = _xifexpression_2;
      } else {
        CharSequence _xifexpression_3 = null;
        boolean _isSingleOptional_1 = attribute.isSingleOptional();
        if (_isSingleOptional_1) {
          StringConcatenation _builder_2 = new StringConcatenation();
          _builder_2.append("var ");
          CharSequence _attributeName_2 = this._kotlinModelObjectBoilerPlate.toAttributeName(attribute);
          _builder_2.append(_attributeName_2);
          _builder_2.append(": ");
          CharSequence _type_2 = this._kotlinModelObjectBoilerPlate.toType(attribute);
          _builder_2.append(_type_2);
          _builder_2.append(" = null");
          _builder_2.newLineIfNotEmpty();
          _xifexpression_3 = _builder_2;
        } else {
          StringConcatenation _builder_3 = new StringConcatenation();
          _builder_3.append("lateinit\tvar ");
          CharSequence _attributeName_3 = this._kotlinModelObjectBoilerPlate.toAttributeName(attribute);
          _builder_3.append(_attributeName_3);
          _builder_3.append(": ");
          CharSequence _type_3 = this._kotlinModelObjectBoilerPlate.toType(attribute);
          _builder_3.append(_type_3);
          _builder_3.newLineIfNotEmpty();
          _xifexpression_3 = _builder_3;
        }
        _xifexpression_1 = _xifexpression_3;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  private CharSequence generateConditionLogic(final Data c, final Condition condition) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if (((condition.getConstraint() != null) && condition.getConstraint().isOneOf())) {
        Object _generateOneOfLogic = this.generateOneOfLogic(c);
        _builder.append(_generateOneOfLogic);
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  private Object generateOneOfLogic(final Data c) {
    return null;
  }
  
  protected Iterable<ExpandedAttribute> _allExpandedAttributes(final RosettaClass type) {
    return RosettaAttributeExtensions.getExpandedAttributes(this._rosettaExtensions.getAllSuperTypes(type));
  }
  
  protected Iterable<ExpandedAttribute> _allExpandedAttributes(final Data type) {
    final Function1<Data, List<ExpandedAttribute>> _function = new Function1<Data, List<ExpandedAttribute>>() {
      @Override
      public List<ExpandedAttribute> apply(final Data it) {
        return RosettaAttributeExtensions.getExpandedAttributes(it);
      }
    };
    return Iterables.<ExpandedAttribute>concat(IterableExtensions.<Data, List<ExpandedAttribute>>map(this._rosettaExtensions.getAllSuperTypes(type), _function));
  }
  
  protected String _definition(final RosettaClass element) {
    return element.getDefinition();
  }
  
  protected String _definition(final Data element) {
    return element.getDefinition();
  }
  
  public Iterable<ExpandedAttribute> allExpandedAttributes(final EObject type) {
    if (type instanceof Data) {
      return _allExpandedAttributes((Data)type);
    } else if (type instanceof RosettaClass) {
      return _allExpandedAttributes((RosettaClass)type);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(type).toString());
    }
  }
  
  public String definition(final EObject element) {
    if (element instanceof Data) {
      return _definition((Data)element);
    } else if (element instanceof RosettaClass) {
      return _definition((RosettaClass)element);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(element).toString());
    }
  }
}
