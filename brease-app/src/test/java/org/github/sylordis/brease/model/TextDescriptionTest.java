package org.github.sylordis.brease.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class TextDescriptionTest {

	/**
	 * Object under test.
	 */
	private TextDescription txtDesc;

	@Test
	void testTextDescription() {
		txtDesc = new TextDescription();
		assertNotNull(txtDesc);
		assertEquals("", txtDesc.author());
		assertEquals("", txtDesc.source());
	}

}
