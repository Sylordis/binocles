package com.github.sylordis.binocles.model.review;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentTest {

	private final int START_INDEX = 50;
	private final int END_INDEX = 100;
	@Mock
	private CommentType type;

	/**
	 * Object under test.
	 */
	private Comment comment;

	@BeforeEach
	void setUp() throws Exception {
		this.comment = new Comment(this.type, this.START_INDEX, this.END_INDEX);
	}

	@Test
	void testCommentLegendConfigurationTypeIntInt() {
		assertNotNull(this.comment);
	}

	@Test
	void testCommentLegendConfigurationTypeIntInt_NullType() {
		this.comment = new Comment(null, this.START_INDEX, this.END_INDEX);
		assertNotNull(this.comment);
		assertNull(this.comment.getType());
	}

	@Test
	void testCommentLegendConfigurationTypeIntInt_NegativeStart() {
		assertThrows(IllegalArgumentException.class, () -> new Comment(this.type, -4, 10));
	}

	@Test
	void testCommentLegendConfigurationTypeIntInt_LowerEndThanStart() {
		assertThrows(IllegalArgumentException.class, () -> new Comment(this.type, 15, 8));
	}

	@Test
	void testCommentLegendConfigurationTypeIntInt_CommentSizeZero() {
		this.comment = new Comment(this.type, this.START_INDEX, this.START_INDEX);
		assertNotNull(this.comment);
		assertEquals(this.comment.getStartIndex(), this.comment.getEndIndex());
	}

	@Test
	void testCommentLegendConfigurationTypeIntIntMapOfStringString() {
		Map<String, String> fields = Map.of("field1", "value1", "field2", "value2");
		this.comment = new Comment(this.type, this.START_INDEX, this.END_INDEX, fields);
		assertNotNull(this.comment);
		assertEquals(this.type, this.comment.getType());
		assertEquals(this.START_INDEX, this.comment.getStartIndex());
		assertEquals(this.END_INDEX, this.comment.getEndIndex());
		assertEquals(fields, this.comment.getFields());
		assertNotSame(fields, this.comment.getFields());
	}

	@Test
	void testCommentLegendConfigurationTypeIntIntMapOfStringString_NullType() {
		Map<String, String> fields = Map.of("field1", "value1", "field2", "value2");
		this.comment = new Comment(null, this.START_INDEX, this.END_INDEX, fields);
		assertNotNull(this.comment);
		assertNull(this.comment.getType());
		assertEquals(this.START_INDEX, this.comment.getStartIndex());
		assertEquals(this.END_INDEX, this.comment.getEndIndex());
		assertEquals(fields, this.comment.getFields());
		assertNotSame(fields, this.comment.getFields());
	}

	@Test
	void testCommentLegendConfigurationTypeIntIntMapOfStringString_EmptyFields() {
		final HashMap<String, String> fields = new HashMap<>();
		this.comment = new Comment(this.type, this.START_INDEX, this.END_INDEX, fields);
		assertNotNull(this.comment);
		assertEquals(this.type, this.comment.getType());
		assertEquals(this.START_INDEX, this.comment.getStartIndex());
		assertEquals(this.END_INDEX, this.comment.getEndIndex());
		assertEquals(fields, this.comment.getFields());
		assertNotSame(fields, this.comment.getFields());
	}

	@Test
	void testCommentLegendConfigurationTypeIntIntMapOfStringString_NullFields() {
		this.comment = new Comment(this.type, this.START_INDEX, this.END_INDEX, null);
		assertNotNull(this.comment);
		assertEquals(this.type, this.comment.getType());
		assertEquals(this.START_INDEX, this.comment.getStartIndex());
		assertEquals(this.END_INDEX, this.comment.getEndIndex());
		assertNotNull(this.comment.getFields());
		assertTrue(this.comment.getFields().isEmpty());
	}

	@Test
	void testCommentLegendConfigurationTypeIntIntMapOfStringString_NegativeStart() {
		assertThrows(IllegalArgumentException.class, () -> new Comment(this.type, -205, 10000, new HashMap<>()));
	}

	@Test
	void testCommentLegendConfigurationTypeIntIntMapOfStringString_LowerEndThanStart() {
		assertThrows(IllegalArgumentException.class, () -> new Comment(this.type, 10001, 10000, new HashMap<>()));
	}

	@ParameterizedTest
	@CsvSource({ "0,1", "0,1000", "500,4000", "43127,3127450" })
	void testCompareTo_Lower(int start1, int start2) {
		Comment c1 = new Comment(this.type, start1, Integer.MAX_VALUE);
		Comment c2 = new Comment(this.type, start2, Integer.MAX_VALUE);
		assertEquals(-1, c1.compareTo(c2));
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 10, 1685, 10000, Integer.MAX_VALUE })
	void testCompareTo_Equals(int start) {
		Comment c1 = new Comment(this.type, start, Integer.MAX_VALUE);
		Comment c2 = new Comment(this.type, start, Integer.MAX_VALUE);
		assertEquals(0, c1.compareTo(c2));
	}

	@ParameterizedTest
	@CsvSource({ "1,0", "50,0", "10,8", "321083441,3128" })
	void testCompareTo_Greater(int start1, int start2) {
		Comment c1 = new Comment(this.type, start1, Integer.MAX_VALUE);
		Comment c2 = new Comment(this.type, start2, Integer.MAX_VALUE);
		assertEquals(1, c1.compareTo(c2));
	}

	@Test
	void testGetType() {
		assertEquals(this.type, this.comment.getType());
	}

	@Test
	void testGetType_IfNull() {
		this.comment = new Comment(null, this.START_INDEX, this.END_INDEX);
		assertNull(this.comment.getType());
	}

	@Test
	void testSetType(@Mock CommentType otherType) {
		this.comment.setType(otherType);
		assertEquals(otherType, this.comment.getType());
	}

	@Test
	void testSetTypeNull() {
		this.comment.setType(null);
		assertNull(this.comment.getType());
	}

	@Test
	void testGetStartIndex() {
		assertEquals(this.START_INDEX, this.comment.getStartIndex());
	}

	@Test
	void testSetStartIndex() {
		final int start = 0;
		this.comment.setStartIndex(start);
		assertEquals(start, this.comment.getStartIndex());
	}

	@Test
	void testSetStartIndex_Negative() {
		assertThrows(IllegalArgumentException.class, () -> this.comment.setStartIndex(-1329));
	}

	@Test
	void testSetStartIndex_EqualToEnd() {
		this.comment.setStartIndex(this.END_INDEX);
		assertEquals(this.END_INDEX, this.comment.getStartIndex());
	}

	@Test
	void testSetStartIndex_GreaterThanEnd() {
		assertThrows(IllegalArgumentException.class, () -> this.comment.setStartIndex(this.END_INDEX + 1));
	}

	@Test
	void testGetEndIndex() {
		assertEquals(this.END_INDEX, this.comment.getEndIndex());
	}

	@Test
	void testSetEndIndex() {
		final int end = this.END_INDEX * 2;
		this.comment.setEndIndex(end);
		assertEquals(end, this.comment.getEndIndex());
	}

	/**
	 * This will fail anyway since start cannot be negative, an end index negative can only be lower
	 * than the start.
	 *
	 * @see #testSetEndIndex_LowerThanStart()
	 */
	@Test
	void testSetEndIndex_Negative() {
		assertThrows(IllegalArgumentException.class, () -> this.comment.setEndIndex(-12387));
	}

	@Test
	void testSetEndIndex_LowerThanStart() {
		assertThrows(IllegalArgumentException.class, () -> this.comment.setEndIndex(this.START_INDEX - 2));
	}

	@ParameterizedTest
	@CsvSource({ "0,0", "20,30", "6541,619684" })
	void testSetBoundaries(int start, int end) {
		this.comment.setBoundaries(start, end);
		assertEquals(start, this.comment.getStartIndex());
		assertEquals(end, this.comment.getEndIndex());
	}

	@Test
	void testSetBoundaries_StartLowerThanZero() {
		assertThrows(IllegalArgumentException.class, () -> this.comment.setBoundaries(-128, this.END_INDEX));
	}

	/**
	 * Start cannot be lower than 0, so the assumption start <= end will be broken.
	 *
	 * @see #testSetBoundaries_StartLowerThanZero()
	 */
	@Test
	void testSetBoundaries_EndLowerThanZero() {
		assertThrows(IllegalArgumentException.class, () -> this.comment.setBoundaries(57, -717));
	}

	@Test
	void testSetBoundaries_EndLowerThanStart() {
		assertThrows(IllegalArgumentException.class, () -> this.comment.setBoundaries(587463, 3));
	}

	@ParameterizedTest
	@CsvSource({ "0,0", "2950,2800", "5042,1676", "2347,5313" })
	void testSetAdaptativeBoundaries(int index1, int index2) {
		this.comment.setAdaptativeBoundaries(index1, index2);
		assertEquals(Math.min(index1, index2), this.comment.getStartIndex());
		assertEquals(Math.max(index1, index2), this.comment.getEndIndex());
		assertTrue(this.comment.getStartIndex() <= this.comment.getEndIndex());
	}

	@ParameterizedTest
	@CsvSource({ "-456,0", "0,-3128", "-2138,-58123" })
	void testSetAdaptativeBoundaries_LowerThanZero(int index1, int index2) {
		assertThrows(IllegalArgumentException.class, () -> this.comment.setAdaptativeBoundaries(index1, index2));
	}

	@Test
	void testGetFields() {
		assertNotNull(this.comment.getFields());
		assertTrue(this.comment.getFields().isEmpty());
		assertThrows(UnsupportedOperationException.class, () -> this.comment.getFields().put("something", "somewhere"));
	}

	@Test
	void testSetFields() {
		Map<String, String> map = Map.of("one", "two", "three", "four", "five", "decollage");
		this.comment.setFields(map);
		assertEquals(map, this.comment.getFields());
		assertNotSame(map, this.comment.getFields());
	}

	@Test
	void testSetFields_Replace() {
		Map<String, String> map = Map.of("one", "two", "three", "four", "five", "decollage");
		this.comment.setFields(map);
		Map<String,String> map2 = Map.of("taking", "of", "rocket", "science");
		this.comment.setFields(map2);
		assertEquals(map2, this.comment.getFields());
		assertNotSame(map2, this.comment.getFields());
	}

	@Test
	void testSetFields_Null() {
		this.comment.setFields(null);
		assertNotNull(this.comment.getFields());
		assertTrue(this.comment.getFields().isEmpty());
	}

	@Test
	void testSetFields_NullWithPreviousSet() {
		Map<String, String> map = Map.of("one", "two", "three", "four", "five", "decollage");
		this.comment.setFields(map);
		this.comment.setFields(null);
		assertNotNull(this.comment.getFields());
		assertTrue(this.comment.getFields().isEmpty());
	}

	@Test
	void testSetFields_WithNullKey() {
		Map<String,String> map = new HashMap<>();
		map.put(null, "return");
		map.put("brave", "pine");
		assertThrows(NullPointerException.class, () -> this.comment.setFields(map));
	}

	@ParameterizedTest
	@ValueSource(strings= {"", " ", " 		  "})
	void testSetFields_WithBlankKey(String key) {
		Map<String,String> map = Map.of(key, "The Value");
		assertThrows(IllegalArgumentException.class, () -> this.comment.setFields(map));
	}

	@Test
	void testSetFields_WithNullOrEmptyValues() {
		Map<String, String> map = new HashMap<>(Map.of("widely", "cowboy", "blank1", " ", "into", "identity",
				"instrument", "anybody", "blank2", "  		 ", "end", "actual"));
		Map<String, String> expected = new HashMap<>(map);
		map.put("nullValue", null);
		expected.put("nullValue", "");
		this.comment.setFields(map);
		assertEquals(expected, this.comment.getFields());
	}

	@ParameterizedTest
	@CsvSource({ "hello,world", "EmptyValue,\"\"",
		"Text as value,\""
				+ """
						Lorem ipsum dolor sit amet, consectetur adipiscing elit.
						Phasellus ac luctus odio. Donec consequat et ipsum ut tempor. Nam purus eros, commodo nec nulla id,
						rhoncus fringilla dolor. Nulla pulvinar, diam et finibus vestibulum, mauris lorem venenatis metus, ac interdum quam dui
						ut metus. Phasellus non pellentesque felis. Integer suscipit, metus sed suscipit lobortis, urna est ultrices ex,
						id tincidunt nisl felis et neque. Vivamus consectetur malesuada purus, semper fringilla odio. Nunc egestas augue sed
						odio maximus feugiat. In vitae ex sodales, pretium nunc vitae, laoreet dolor. In neque ante, commodo eu sodales id,
						luctus eget metus. Curabitur justo erat, fermentum sed consectetur vel, tincidunt sit amet lacus.

						Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae.
						"""
						+ "\"" })
	void testSetFieldFromType(String fieldKey, String value) {
		when(this.type.hasField(fieldKey)).thenReturn(true);
		this.comment.setFieldFromType(fieldKey, value);
		Map<String, String> expected = Map.of(fieldKey, value);
		assertEquals(expected, this.comment.getFields());
	}

	@ParameterizedTest
	@CsvSource({ "foo,bar", "novalue?,", "blankValue,   \t" })
	void testSetFieldFromType_NullType(String key, String index) {
		this.comment.setType(null);
		this.comment.setFieldFromType(key, index);
		assertTrue(this.comment.getFields().isEmpty());
	}

	@Test
	void testSetFieldFromType_NullKey() {
		assertThrows(NullPointerException.class, () -> this.comment.setFieldFromType(null, "hello?"));
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "   ", " 		   " })
	void testSetFieldFromType_BlankKey(String key) {
		assertThrows(IllegalArgumentException.class, () -> this.comment.setFieldFromType(key, "Why are you blank?"));
	}

	@Test
	void testSetFieldFromType_NullValue() {
		final String fieldKey = "pjlivsqtxb";
		when(this.type.hasField(fieldKey)).thenReturn(true);
		this.comment.setFieldFromType(fieldKey, "");
		Map<String, String> expected = Map.of(fieldKey, "");
		assertEquals(expected, this.comment.getFields());
	}

	@Test
	void testSetFieldFromType_NonExistingField(@Mock CommentType type) {
		final String fieldKey = "pxdbxtlrrdkzdrzykrsnyowet";
		when(type.hasField(anyString())).thenReturn(false);
		this.comment.setType(type);
		this.comment.setFieldFromType(fieldKey, "lwvyuesxmdkxvljjyyrsstkxf");
		assertTrue(this.comment.getFields().isEmpty());
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "  		", "vapor", "Equator dry hearing speed" })
	void testSetFieldFromType_Replace(String value) {
		CommentType type = mock(CommentType.class);
		final String fieldKey = "beginning";
		final String origValue = "area";
		this.comment.setField(fieldKey, origValue);
		when(type.hasField(fieldKey)).thenReturn(true);
		this.comment.setType(type);
		this.comment.setFieldFromType(fieldKey, value);
		Map<String, String> expected = Map.of(fieldKey, value);
		assertEquals(expected, this.comment.getFields());
	}

	@ParameterizedTest
	@CsvSource({ "foo,bar", "novalue!?,", "blankValue,   \t", "null value," })
	void testSetField(String key, String value) {
		this.comment.setField(key, value);
		Map<String, String> expected = Map.of(key, value == null ? "" : value);
		assertEquals(expected, this.comment.getFields());
	}

	@Test
	void testSetField_Multiple() {
		Map<String, String> map = Map.of("flat", "cool", "frame", "interest", "transportation", "have");
		map.forEach((k, v) -> this.comment.setField(k, v));
		assertEquals(map, this.comment.getFields());
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "  		", "gold", " studying seems pure " })
	void testSetField_Replace(String value) {
		final String fieldKey = "shall";
		final String origValue = "hold";
		this.comment.setField(fieldKey, origValue);
		this.comment.setField(fieldKey, value);
		Map<String, String> expected = Map.of(fieldKey, value);
		assertEquals(expected, this.comment.getFields());
	}

	@Test
	void testSetField_NullKey() {
		assertThrows(NullPointerException.class, () -> this.comment.setField(null, "a value!"));
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "   ", " 		   " })
	void testSetField_BlankKey(String value) {
		final String key = "space";
		this.comment.setField(key, value);
		Map<String, String> expected = Map.of(key, value);
		assertEquals(expected, this.comment.getFields());
	}

	@Test
	void testSetField_NullValue() {
		final String key = "small";
		this.comment.setField(key, null);
		Map<String, String> expected = Map.of(key, "");
		assertEquals(expected, this.comment.getFields());
	}

	@ParameterizedTest
	@CsvSource({ "foo,bar", "novalue!?,\"\"", "blankValue,\"   \"", "null value," })
	void testRemoveField(String key, String value) {
		this.comment.setField(key, value);
		assertEquals(value == null ? "" : value, this.comment.removeField(key));
		assertNotNull(this.comment.getFields());
		assertTrue(this.comment.getFields().isEmpty());
	}

	@Test
	void testRemoveField_FromMultiple() {
		final String keyToRemove = "electricity";
		final String valueRemoved = "porch";
		final Map<String, String> map = Map.of("telephone", "closely", keyToRemove, valueRemoved, "pale", "love");
		this.comment.setFields(map);
		Map<String, String> expected = new HashMap<>(map);
		expected.remove(keyToRemove);
		assertEquals(valueRemoved, this.comment.removeField(keyToRemove));
		assertEquals(expected, this.comment.getFields());
	}

	@Test
	void testRemoveField_NotExisting() {
		assertNull(this.comment.removeField("I don't exist!"));
		assertNotNull(this.comment.getFields());
		assertTrue(this.comment.getFields().isEmpty());
	}

	@Test
	void testClearFields() {
		this.comment.clearFields();
		assertNotNull(this.comment.getFields());
		assertTrue(this.comment.getFields().isEmpty());
	}

	@Test
	void testClearFields_IfNotEmpty() {
		Map<String, String> map = Map.of("coming", "again", "sign", "willing", "apart", "joy");
		this.comment.setFields(map);
		this.comment.clearFields();
		assertNotNull(this.comment.getFields());
		assertTrue(this.comment.getFields().isEmpty());
	}

}
