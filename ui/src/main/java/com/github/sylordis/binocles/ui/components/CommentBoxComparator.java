package com.github.sylordis.binocles.ui.components;

import java.util.Comparator;

import com.github.sylordis.binocles.model.review.Comment;

import javafx.scene.Node;

/**
 * Compares 2 comment boxes
 */
public class CommentBoxComparator implements Comparator<Node> {

	@Override
	public int compare(Node o1, Node o2) {
		int ret = 0;
		if (o1 instanceof CommentBox && o2 instanceof CommentBox) {
			Comment c1 = ((CommentBox) o1).getComment();
			Comment c2 = ((CommentBox) o2).getComment();
			ret = c1.compareTo(c2);
		}
		return ret;
	}

}
