package com.regnosys.rosetta.generator.kotlin.object;

import com.regnosys.rosetta.generator.kotlin.object.KotlinMetaFieldGenerator;
import com.regnosys.rosetta.generator.kotlin.object.KotlinModelObjectBoilerPlate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("all")
public class KotlinModelObjectGenerator {
  /* @Inject
   */private /* RosettaExtensions */Object _rosettaExtensions;
  
  /* @Inject
   */private KotlinModelObjectBoilerPlate _kotlinModelObjectBoilerPlate;
  
  /* @Inject
   */private KotlinMetaFieldGenerator _kotlinMetaFieldGenerator;
  
  private static final String CLASSES_FILENAME = "Types.kt";
  
  private static final String META_FILENAME = "Metatypes.kt";
  
  public Map<String, ? extends CharSequence> generate(final /* Iterable<Data> */Object rosettaClasses, final /* Iterable<RosettaMetaType> */Object metaTypes, final String version) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method map(Object) is undefined for the type Iterable<Data>"
      + "\nThe method or field superType is undefined"
      + "\nThe method or field allSuperTypes is undefined"
      + "\nThe method sortBy(Object) is undefined for the type Iterable<Data>"
      + "\nThe method or field name is undefined"
      + "\nThe method sortBy(Object) is undefined for the type Iterable<Data>"
      + "\nThe method or field name is undefined"
      + "\nType mismatch: cannot convert from HashMap<String, Object> to Map<String, ? extends CharSequence>"
      + "\nmap cannot be resolved"
      + "\nflatten cannot be resolved"
      + "\ntoSet cannot be resolved"
      + "\ngenerateClasses cannot be resolved"
      + "\nreplaceTabsWithSpaces cannot be resolved"
      + "\ngenerateMetaFields cannot be resolved"
      + "\nreplaceTabsWithSpaces cannot be resolved");
  }
  
  /**
   * Generate the classes
   */
  private CharSequence generateClasses(final /* List<Data> */Object rosettaClasses, final /* Set<Data> */Object superTypes, final String version) {
    throw new Error("Unresolved compilation problems:"
      + "\n! cannot be resolved."
      + "\nThe method classComment(String, Iterable<Object>) from the type KotlinModelGeneratorUtil refers to the missing type Object"
      + "\nThe method generateAttributes(Data) from the type KotlinModelObjectGenerator refers to the missing type Data"
      + "\ndefinition cannot be resolved"
      + "\nallExpandedAttributes cannot be resolved"
      + "\nname cannot be resolved"
      + "\nsuperType cannot be resolved"
      + "\n=== cannot be resolved"
      + "\n&& cannot be resolved"
      + "\nsuperType cannot be resolved"
      + "\n!== cannot be resolved"
      + "\n&& cannot be resolved"
      + "\nsuperType cannot be resolved"
      + "\nname cannot be resolved"
      + "\nsuperType cannot be resolved"
      + "\n!== cannot be resolved"
      + "\nsuperType cannot be resolved"
      + "\nname cannot be resolved");
  }
  
  private CharSequence generateAttributes(final /* Data */Object c) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method generateExpandedAttribute(Data, ExpandedAttribute) from the type KotlinModelObjectGenerator refers to the missing type Data"
      + "\nallExpandedAttributes cannot be resolved");
  }
  
  private CharSequence generateExpandedAttribute(final /* Data */Object c, final /* ExpandedAttribute */Object attribute) {
    throw new Error("Unresolved compilation problems:"
      + "\nenclosingType cannot be resolved"
      + "\n== cannot be resolved"
      + "\nname cannot be resolved"
      + "\nenum cannot be resolved"
      + "\n&& cannot be resolved"
      + "\nhasMetas cannot be resolved"
      + "\n! cannot be resolved"
      + "\nsingleOptional cannot be resolved"
      + "\ntoAttributeName cannot be resolved"
      + "\ntoType cannot be resolved"
      + "\ntoAttributeName cannot be resolved"
      + "\ntoType cannot be resolved"
      + "\nsingleOptional cannot be resolved"
      + "\ntoAttributeName cannot be resolved"
      + "\ntoType cannot be resolved"
      + "\ntoAttributeName cannot be resolved"
      + "\ntoType cannot be resolved");
  }
  
  private CharSequence generateConditionLogic(final /* Data */Object c, final /* Condition */Object condition) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method generateOneOfLogic(Data) from the type KotlinModelObjectGenerator refers to the missing type Data"
      + "\nconstraint cannot be resolved"
      + "\n!== cannot be resolved"
      + "\n&& cannot be resolved"
      + "\nconstraint cannot be resolved"
      + "\noneOf cannot be resolved");
  }
  
  private Object generateOneOfLogic(final /* Data */Object c) {
    return null;
  }
  
  protected /* Iterable<ExpandedAttribute> */Object _allExpandedAttributes(final /* RosettaClass */Object type) {
    throw new Error("Unresolved compilation problems:"
      + "\nallSuperTypes cannot be resolved"
      + "\nexpandedAttributes cannot be resolved");
  }
  
  protected /* Iterable<ExpandedAttribute> */Object _allExpandedAttributes(final /* Data */Object type) {
    throw new Error("Unresolved compilation problems:"
      + "\nallSuperTypes cannot be resolved"
      + "\nmap cannot be resolved"
      + "\nexpandedAttributes cannot be resolved"
      + "\nflatten cannot be resolved");
  }
  
  protected String _definition(final /* RosettaClass */Object element) {
    throw new Error("Unresolved compilation problems:"
      + "\ndefinition cannot be resolved");
  }
  
  protected String _definition(final /* Data */Object element) {
    throw new Error("Unresolved compilation problems:"
      + "\ndefinition cannot be resolved");
  }
  
  public /* Iterable<ExpandedAttribute> */Object allExpandedAttributes(final RosettaClass type) {
    if (type != null) {
      return _allExpandedAttributes(type); else {
        throw new IllegalArgumentException("Unhandled parameter types: " +
          Arrays.<Object>asList(type).toString());
      }
    }
    
    public String definition(final RosettaClass element) {
      if (element != null) {
        return _definition(element); else {
          throw new IllegalArgumentException("Unhandled parameter types: " +
            Arrays.<Object>asList(element).toString());
        }
      }
    }
    