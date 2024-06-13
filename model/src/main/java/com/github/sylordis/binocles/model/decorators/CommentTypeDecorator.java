package com.github.sylordis.binocles.model.decorators;

import com.github.sylordis.binocles.model.review.CommentType;
import com.github.sylordis.binocles.utils.CustomDecorator;

public class CommentTypeDecorator extends CustomDecorator<CommentType> {

	/**
	 * Adds the name of the comment type.
	 * 
	 * @return itself
	 */
	public CommentTypeDecorator thenName() {
		this.then(c -> c.getName());
		return this;
	}

}
