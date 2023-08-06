package com.github.sylordis.binocle.ui.components;

import com.github.sylordis.binocle.model.review.Nomenclature;

/**
 * Class used for the tree root of the reviews.
 * 
 * @author sylordis
 *
 */
public final class NomenclatureTreeRoot extends Nomenclature {

	public NomenclatureTreeRoot() {
		super("THENOMENCLATUREROOT");
	}

	@Override
	public String toString() {
		return "Nomenclatures root";
	}

	public String getId() {
		return "THENOMENCLATUREROOT";
	}
}
