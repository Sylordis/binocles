package com.github.sylordis.binocles.utils.comparators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.sylordis.binocles.contracts.Identifiable;

/**
 * 
 */
class IdentifiableComparatorTest {

	private IdentifiableComparator comparator;
	
	@BeforeEach
	void setUp() {
		comparator = new IdentifiableComparator();
	}
	
	/**
	 * Test method for {@link com.github.sylordis.binocles.utils.comparators.IdentifiableComparator#compare(com.github.sylordis.binocles.contracts.Identifiable, com.github.sylordis.binocles.contracts.Identifiable)}.
	 */
	@Test
	void testCompare_Same() {
		final String id = "abc";
		assertEquals(0, comparator.compare(new Item(id), new Item(id)));
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.utils.comparators.IdentifiableComparator#compare(com.github.sylordis.binocles.contracts.Identifiable, com.github.sylordis.binocles.contracts.Identifiable)}.
	 */
	@Test
	void testCompare_Lower() {
		final String id = "anakin";
		final String id2 = "obiwan";
		int compared = comparator.compare(new Item(id), new Item(id2));
		assertTrue(compared < 0);
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.utils.comparators.IdentifiableComparator#compare(com.github.sylordis.binocles.contracts.Identifiable, com.github.sylordis.binocles.contracts.Identifiable)}.
	 */
	@Test
	void testCompare_Higher() {
		final String id = "iswithme";
		final String id2 = "theforce";
		int compared = comparator.compare(new Item(id2), new Item(id));
		assertTrue(compared > 0);
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.utils.comparators.IdentifiableComparator#compare(com.github.sylordis.binocles.contracts.Identifiable, com.github.sylordis.binocles.contracts.Identifiable)}.
	 */
	@Test
	void testCompare_NoIDHigher() {
		final String id = "high ground";
		assertEquals(-1, comparator.compare(new Item(""), new Item(id)));
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.utils.comparators.IdentifiableComparator#compare(com.github.sylordis.binocles.contracts.Identifiable, com.github.sylordis.binocles.contracts.Identifiable)}.
	 */
	@Test
	void testCompare_NoIDLower() {
		final String id = "life";
		assertEquals(1, comparator.compare(new Item(id), new Item("")));
	}

	/**
	 * Private class for testing.
	 * 
	 * @author sylordis
	 *
	 */
	private record Item(String id) implements Identifiable {

		@Override
		public String getId() {
			return Identifiable.formatId(this.id);
		}
	};
}
