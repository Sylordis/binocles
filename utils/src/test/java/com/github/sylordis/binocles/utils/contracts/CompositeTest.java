package com.github.sylordis.binocles.utils.contracts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Test class for {@link Composite} interface.
 */
class CompositeTest {

	@Test
	void testCanHaveChildren() {
		Composite<?> object = new PossibleParent();
		assertTrue(object.canHaveChildren());
	}

	@Test
	void testCanHaveChildren_not() {
		Composite<?> object = new CannotHaveChildren();
		assertFalse(object.canHaveChildren());
	}

	@Test
	void testHasChildren() {
		PossibleParent object = new PossibleParent();
		TreeNode node = new Leaf();
		object.add(node);
		assertTrue(object.hasChildren());
	}

	@Test
	void testHasChildren_cannotHaveChildren() {
		Composite<?> object = new CannotHaveChildren();
		assertFalse(object.hasChildren());
	}

	@Test
	void testHasChildren_none() {
		Composite<?> object = new PossibleParent();
		assertFalse(object.hasChildren());
	}

	@Test
	void testGetChildren() {
		PossibleParent object = new PossibleParent();
		TreeNode node1 = new Leaf();
		TreeNode node2 = new Leaf();
		object.add(node1);
		object.add(node2);
		List<TreeNode> expected = List.of(node1, node2);
		assertEquals(expected, object.getChildren());
	}

	@Test
	void testGetChildren_cannotHaveChildren() {
		Composite<?> object = new CannotHaveChildren();
		assertNull(object.getChildren());
	}

	@Test
	void testGetChildren_none() {
		PossibleParent object = new PossibleParent();
		assertEquals(List.of(), object.getChildren());
	}

	@Test
	void testIsChildOf() {
		PossibleParent root = new PossibleParent();
		PossibleParent child1 = new PossibleParent();
		PossibleParent child2 = new PossibleParent();
		root.add(child1);
		root.add(child2);
		assertTrue(child1.isChildOf(root));
	}

	@Test
	void testIsChildOf_transitive() {
		PossibleParent root = new PossibleParent();
		PossibleParent node1 = new PossibleParent();
		PossibleParent node2 = new PossibleParent();
		TreeNode node11 = new Leaf();
		TreeNode node12 = new Leaf();
		root.add(node1);
		root.add(node2);
		node1.add(node11);
		node1.add(node12);
		assertTrue(node11.isChildOf(root));
	}

	@Test
	void testIsChildOf_isNotAChild() {
		PossibleParent root = new PossibleParent();
		PossibleParent node1 = new PossibleParent();
		PossibleParent node2 = new PossibleParent();
		TreeNode node11 = new Leaf();
		TreeNode node12 = new Leaf();
		root.add(node1);
		root.add(node2);
		node1.add(node11);
		node1.add(node12);
		TreeNode stranger = new PossibleParent();
		assertFalse(stranger.isChildOf(root));
	}

	@Test
	void testIsChildOf_isNotAChildBecauseParent() {
		PossibleParent root = new PossibleParent();
		PossibleParent child1 = new PossibleParent();
		assertFalse(root.isChildOf(child1));
	}

	@Test
	void testReduce_root() {
		PossibleParent root = new PossibleParent();
		PossibleParent node1 = new PossibleParent();
		PossibleParent node2 = new PossibleParent();
		TreeNode node11 = new Leaf();
		TreeNode node12 = new Leaf();
		TreeNode node21 = new Leaf();
		TreeNode node22 = new Leaf();
		TreeNode node23 = new Leaf();
		root.add(node1);
		root.add(node2);
		node1.add(node11);
		node1.add(node12);
		node2.add(node21);
		node2.add(node22);
		node2.add(node23);
		assertEquals(List.of(root), Composite.reduce(List.of(root, node1, node2, node11, node12, node23)));
	}

	@Test
	void testReduce_childrenOnly() {
		PossibleParent root = new PossibleParent();
		PossibleParent node1 = new PossibleParent();
		PossibleParent node2 = new PossibleParent();
		PossibleParent node3 = new PossibleParent();
		TreeNode node11 = new Leaf();
		TreeNode node12 = new Leaf();
		TreeNode node21 = new Leaf();
		TreeNode node22 = new Leaf();
		TreeNode node23 = new Leaf();
		root.add(node1);
		root.add(node2);
		root.add(node3);
		node1.add(node11);
		node1.add(node12);
		node2.add(node21);
		node2.add(node22);
		node2.add(node23);
		assertEquals(List.of(node11, node12, node3, node21, node22, node23),
		        Composite.reduce(List.of(node11, node12, node3, node21, node22, node23)));
	}

	@Test
	void testReduce_removeChildren() {
		PossibleParent root = new PossibleParent();
		PossibleParent node1 = new PossibleParent();
		PossibleParent node2 = new PossibleParent();
		PossibleParent node3 = new PossibleParent();
		TreeNode node11 = new Leaf();
		TreeNode node12 = new Leaf();
		TreeNode node21 = new Leaf();
		TreeNode node22 = new Leaf();
		TreeNode node23 = new Leaf();
		root.add(node1);
		root.add(node2);
		root.add(node3);
		node1.add(node11);
		node1.add(node12);
		node2.add(node21);
		node2.add(node22);
		node2.add(node23);
		assertEquals(List.of(node1, node21, node22, node3, node23),
		        Composite.reduce(List.of(node11, node1, node21, node22, node3, node23)));
	}

	// Test classes

	class CannotHaveChildren implements Composite<CannotHaveChildren> {
	}

	interface TreeNode extends Composite<TreeNode> {
	}

	class Leaf implements TreeNode {
	}

	class PossibleParent implements TreeNode {

		private List<TreeNode> children = new ArrayList<>();

		public void add(TreeNode node) {
			children.add(node);
		}

		@Override
		public List<? extends TreeNode> getChildren() {
			return children;
		}

	}
}
