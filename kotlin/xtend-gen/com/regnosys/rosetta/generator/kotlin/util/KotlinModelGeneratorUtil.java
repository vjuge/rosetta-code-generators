package com.regnosys.rosetta.generator.kotlin.util;

import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class KotlinModelGeneratorUtil {
  public static CharSequence fileComment(final String version) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* This file is auto-generated from the ISDA Common Domain Model, do not edit.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Version: ");
    _builder.append(version, " ");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence comment(final String definition) {
    throw new Error("Unresolved compilation problems:"
      + "\n!== cannot be resolved."
      + "\n! cannot be resolved."
      + "\n&& cannot be resolved");
  }
  
  public static CharSequence classComment(final String definition, final /* Iterable<ExpandedAttribute> */Object attributes) {
    throw new Error("Unresolved compilation problems:"
      + "\n!== cannot be resolved."
      + "\n! cannot be resolved."
      + "\n&& cannot be resolved"
      + "\nname cannot be resolved"
      + "\ndefinition cannot be resolved");
  }
}
