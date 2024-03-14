package com.github.sylordis.binocles.model.review;

/**
 * Default nomenclature for the software with just a field for text. The default nomenclature is
 * assigned by default if none other is specified and should not be edited.
 */
public class DefaultNomenclature extends Nomenclature {

	/**
	 * Constant for the name of the default nomenclature.
	 */
	public static final String NAME = "Default";

	/**
	 * Creates a default nomenclature.
	 */
	public DefaultNomenclature() {
		super(NAME);
		getTypes().add(new DefaultCommentType());
	}

	@Override
	public boolean isDefaultNomenclature() {
		return true;
	}
	
	

}
