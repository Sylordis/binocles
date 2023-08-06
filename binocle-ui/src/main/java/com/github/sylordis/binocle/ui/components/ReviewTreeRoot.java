package com.github.sylordis.binocle.ui.components;

import com.github.sylordis.binocle.model.review.ReviewableContent;

/**
 * Class used for the tree root of the reviews.
 * 
 * @author sylordis
 *
 */
public final class ReviewTreeRoot extends ReviewableContent {

	@Override
	public String toString() {
		return "Reviews root";
	}

	public String getId() {
		return "THEREVIEWROOT";
	}
}
