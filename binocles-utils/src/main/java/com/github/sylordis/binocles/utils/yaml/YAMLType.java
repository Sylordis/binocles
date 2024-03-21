package com.github.sylordis.binocles.utils.yaml;

import java.util.ArrayList;
import java.util.Map;

/**
 * Supposed to represent different yaml types on a Java level. Be reminded that this type checking
 * is a soft checking without parameterised generic classes.
 *
 * @author sylordis
 *
 */
public enum YAMLType {

	/**
	 * A typical YAML node.
	 */
	NODE(Map.class),
	/**
	 * A YAML list.
	 */
	LIST(ArrayList.class),
	/**
	 * A YAML String value.
	 */
	STRING(String.class);

	private final Class<?> javaType;

	/**
	 * Constructor for type.
	 * 
	 * @param javaType
	 */
	YAMLType(Class<?> javaType) {
		this.javaType = javaType;
	}

	/**
	 * Gets the type associated to the item.
	 * 
	 * @return
	 */
	public Class<?> getJavaType() {
		return javaType;
	}
}
