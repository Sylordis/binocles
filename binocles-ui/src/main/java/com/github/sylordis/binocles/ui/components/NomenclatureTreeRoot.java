package com.github.sylordis.binocles.ui.components;

import com.github.sylordis.binocles.model.review.NomenclatureItem;

/**
 * Class used for the tree root of the reviews.
 * 
 * @author sylordis
 *
 */
public final class NomenclatureTreeRoot implements NomenclatureItem{

	@Override
	public String toString() {
		return "Nomenclatures root";
	}

	public String getId() {
		return "THENOMENCLATUREROOT";
	}
}
