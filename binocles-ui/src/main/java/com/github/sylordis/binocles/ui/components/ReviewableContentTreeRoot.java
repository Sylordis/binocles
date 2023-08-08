package com.github.sylordis.binocles.ui.components;

import com.github.sylordis.binocles.model.review.ReviewableContent;

/**
 * Class used for the tree root of the reviews.
 * 
 * @author sylordis
 *
 */
public final class ReviewableContentTreeRoot extends ReviewableContent {

	@Override
	public String toString() {
		return "Reviews root";
	}

	public String getId() {
		return "THEREVIEWROOT";
	}

	@Override
	public String getTitle() {
		return getId();
	}
}
