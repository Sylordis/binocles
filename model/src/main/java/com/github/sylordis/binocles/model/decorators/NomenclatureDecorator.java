package com.github.sylordis.binocles.model.decorators;

import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.utils.CustomDecorator;

public class NomenclatureDecorator extends CustomDecorator<Nomenclature> {

	/**
	 * Adds the name of the nomenclature.
	 * 
	 * @return itself
	 */
	public NomenclatureDecorator thenName() {
		this.then(n -> n.getName());
		return this;
	}

}
