package com.github.sylordis.binocles.ui.listeners;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.github.sylordis.binocles.ui.listeners.ListenerValidator.FeedbackBehaviour;

import javafx.beans.value.ObservableValue;

class ListenerValidatorTest {

	/**
	 * Object under test, reset every test with setup.
	 */
	private ListenerValidator<String> validator;

	@BeforeEach
	void setUp() throws Exception {
		validator = new ListenerValidator<String>();
	}

	@Test
	void testListenerValidator() {
		assertNotNull(validator);
	}

	@Test
	void testAddErrorCondition() {
		final BiFunction<String, String, Boolean> validityCondition = (s1, s2) -> s2.equals("hello");
		validator.addValiditionCondition("a", validityCondition);
		assertEquals(Map.of("a", validityCondition), validator.getValidityConditions());
	}

	@Test
	void testAddErrorCondition_Multiple() {
		final BiFunction<String, String, Boolean> cond1 = (s1, s2) -> s2.equals("hello");
		final BiFunction<String, String, Boolean> cond2 = (s1, s2) -> false;
		validator.addValiditionCondition("b", cond1);
		validator.addValiditionCondition("c", cond2);
		assertEquals(Map.of("b", cond1, "c", cond2), validator.getValidityConditions());
	}

	@Test
	void testAddErrorCondition_NullBoth() {
		validator.addValiditionCondition(null, null);
		assertTrue(validator.getValidityConditions().isEmpty());
	}

	@Test
	void testAddErrorCondition_NullText() {
		final BiFunction<String, String, Boolean> validityCondition = (s1, s2) -> s2.equals("hello");
		validator.addValiditionCondition(null, validityCondition);
		assertTrue(validator.getValidityConditions().isEmpty());
	}

	@Test
	void testAddErrorCondition_NullValidator() {
		validator.addValiditionCondition("empty?", null);
		assertTrue(validator.getValidityConditions().isEmpty());
	}

	@Test
	void testAndThen() {
		int[] data = { 4 };
		final Runnable then = () -> data[0]++;
		validator.andThen(then);
		assertEquals(then, validator.getPostAction());
	}

	@Test
	@Tag("functional")
	void testChanged_NoError() {
		int[] data = { 0 };
		String[] dataString = { "", "" };
		final String idle = "Nothing to report";
		final String[] errors = { "Value changed", "Value is empty" };
		final String andThenMessage = "Job done";
		final BiFunction<String, String, Boolean> conditionChanged = (o, n) -> !o.equals(n);
		final BiFunction<String, String, Boolean> conditionEmpty = (o, n) -> !n.isBlank();
		validator.error(errors[0], conditionChanged).error(errors[1], conditionEmpty).onInvalid(b -> data[0]--)
		        .onValid(b -> data[0]++).feed(s -> dataString[0] = s).feedDefault(idle).andThen(() -> dataString[1] = andThenMessage);
		validator.changed(mock(ObservableValue.class), "", "abcd");
		assertEquals(1, data[0]);
		assertEquals(idle, dataString[0]);
		assertEquals(andThenMessage, dataString[1]);
		assertTrue(validator.getErrorMessages().isEmpty());
	}

	@Test
	@Tag("functional")
	void testChanged_Error() {
		int[] data = { 0 };
		final String error = "Value didn't change";
		final BiFunction<String, String, Boolean> conditionChanged = (o, n) -> !o.equals(n);
		validator.error(error, conditionChanged).onInvalid(b -> data[0]--).onValid(b -> data[0]++);
		validator.changed(mock(ObservableValue.class), "a", "a");
		assertEquals(-1, data[0]);
		assertEquals(List.of(error), validator.getErrorMessages());
	}

	@Test
	@Tag("functional")
	void testChanged_MultipleErrors() {
		int[] data = { 0 };
		final String[] errors = { "Value did change", "Value is empty" };
		final BiFunction<String, String, Boolean> conditionChanged = (o, n) -> o.equals(n);
		final BiFunction<String, String, Boolean> conditionEmpty = (o, n) -> !n.isBlank();
		validator.error(errors[0], conditionChanged).error(errors[1], conditionEmpty).onInvalid(b -> data[0]--)
		        .onValid(b -> data[0]++);
		validator.changed(mock(ObservableValue.class), "a", "");
		assertEquals(-1, data[0]);
		assertEquals(List.of(errors), validator.getErrorMessages());
	}

	@Test
	void testError() {
		final String error = "This will never work";
		final BiFunction<String, String, Boolean> condition = (s1, s2) -> false;
		Map<String, BiFunction<String, String, Boolean>> expected = Map.of(error, condition);
		validator.error(error, condition);
		assertEquals(expected, validator.getValidityConditions());
	}

	@Test
	void testError_Multiple() {
		final String error1 = "This will never work";
		final String error2 = "This will always work";
		final BiFunction<String, String, Boolean> condition1 = (s1, s2) -> false;
		final BiFunction<String, String, Boolean> condition2 = (s1, s2) -> true;
		Map<String, BiFunction<String, String, Boolean>> expected = Map.of(error1, condition1, error2, condition2);
		validator.error(error1, condition1).error(error2, condition2);
		assertEquals(expected, validator.getValidityConditions());
	}

	@Test
	void testFeedConsumerOfString() {
		String[] result = { "" };
		final Consumer<String> feed = s -> result[0] = s;
		final Function<List<String>, String> behaviour = validator.getFeedbackBehaviour();
		validator.feed(feed);
		assertEquals(feed, validator.getFeedbackConsumer());
		assertEquals(behaviour, validator.getFeedbackBehaviour());
	}

	@Test
	void testFeedConsumerOfStringFunctionOfListOfStringString() {
		String[] result = { "" };
		final Consumer<String> feed = s -> result[0] = s;
		final Function<List<String>, String> behaviour = l -> "";
		validator.feed(feed, behaviour);
		assertEquals(feed, validator.getFeedbackConsumer());
		assertEquals(behaviour, validator.getFeedbackBehaviour());
	}

	@Test
	void testFeedDefault() {
		final String msg = "Hello world!";
		validator.feedDefault(msg);
		assertEquals(msg, validator.getFeedbackDefault());
	}

	@Test
	void testFeedDefault_Null() {
		final String msg = "Hello world!";
		validator.feedDefault(msg);
		validator.feedDefault(null);
		assertEquals(ListenerValidator.FEEDBACK_DEFAULT, validator.getFeedbackDefault());
	}
	
	@Test
	void testGetActionWhenInvalid() {
		assertNull(validator.getActionWhenInvalid());
	}

	@Test
	void testGetActionWhenValid() {
		assertNull(validator.getActionWhenValid());
	}

	@Test
	void testGetErrorMessages() {
		assertNotNull(validator.getErrorMessages());
		assertTrue(validator.getErrorMessages().isEmpty());
	}

	@Test
	void testGetFeedbackBehaviour() {
		// There's no need to know which one it is, just that it is not null.
		assertNotNull(validator.getFeedbackBehaviour());
	}

	@Test
	void testGetFeedbackConsumer() {
		assertNull(validator.getFeedbackConsumer());
	}

	@Test
	void testGetFeedbackIdle() {
		assertEquals(ListenerValidator.FEEDBACK_DEFAULT, validator.getFeedbackDefault());
	}

	@Test
	void testGetPostAction() {
		assertNull(validator.getPostAction());
	}

	@Test
	void testGetValidityConditions() {
		assertNotNull(validator.getValidityConditions());
		assertTrue(validator.getValidityConditions().isEmpty());
	}

	@Test
	void testOnEither() {
		final Consumer<Boolean> action = b -> {
		};
		validator.onEither(action);
		assertEquals(action, validator.getActionWhenValid());
		assertEquals(action, validator.getActionWhenInvalid());
	}

	@Test
	void testOnInvalid() {
		final Consumer<Boolean> action = b -> {
		};
		validator.onInvalid(action);
		assertNull(validator.getActionWhenValid());
		assertEquals(action, validator.getActionWhenInvalid());
	}

	@Test
	void testOnValid() {
		final Consumer<Boolean> action = b -> {
		};
		validator.onValid(action);
		assertNull(validator.getActionWhenInvalid());
		assertEquals(action, validator.getActionWhenValid());
	}

	@Test
	void testSetActionWhenInvalid() {
		boolean[] result = { false };
		final Consumer<Boolean> action = b -> result[0] = true;
		validator.setActionWhenInvalid(action);
		assertEquals(action, validator.getActionWhenInvalid());
		assertFalse(result[0]);
	}

	@Test
	void testSetActionWhenValid() {
		boolean[] result = { true };
		final Consumer<Boolean> action = b -> result[0] = false;
		validator.setActionWhenValid(action);
		assertEquals(action, validator.getActionWhenValid());
		assertTrue(result[0]);
	}

	@Test
	void testSetErrorMessages() {
		final List<String> msgs = List.of("a", "b", "c");
		validator.setErrorMessages(msgs);
		assertEquals(msgs, validator.getErrorMessages());
	}

	@Test
	void testSetErrorMessages_Replace() {
		final List<String> msgs = List.of("a", "b", "c");
		final List<String> msgs2 = List.of("d", "e");
		validator.setErrorMessages(msgs);
		validator.setErrorMessages(msgs2);
		assertEquals(msgs2, validator.getErrorMessages());
	}

	@Test
	void testSetErrorMessages_Null() {
		validator.setErrorMessages(null);
		assertNotNull(validator.getErrorMessages());
		assertTrue(validator.getErrorMessages().isEmpty());
	}

	@Test
	void testSetErrorMessages_Empty() {
		validator.setErrorMessages(List.of());
		assertNotNull(validator.getErrorMessages());
		assertTrue(validator.getErrorMessages().isEmpty());
	}

	@Test
	void testSetFeedbackBehaviour() {
		final Function<List<String>, String> behaviour = l -> "";
		validator.setFeedbackBehaviour(behaviour);
		assertEquals(behaviour, validator.getFeedbackBehaviour());
	}

	@Test
	void testSetFeedbackBehaviour_Reset() {
		final Function<List<String>, String> behaviour = l -> "";
		validator.setFeedbackBehaviour(behaviour);
		validator.setFeedbackBehaviour(null);
		assertEquals(FeedbackBehaviour.DEFAULT, validator.getFeedbackBehaviour());
	}

	@Test
	void testSetFeedbackConsumer() {
		final Consumer<String> consumer = s -> {
		};
		validator.setFeedbackConsumer(consumer);
		assertEquals(consumer, validator.getFeedbackConsumer());
	}

	@Test
	void testSetFeedbackIdle() {
		final String idle = "I'm idle";
		validator.setFeedbackDefault(idle);
		assertEquals(idle, validator.getFeedbackDefault());
	}

	@Test
	void testSetPostAction() {
		final Runnable action = () -> {
		};
		validator.setPostAction(action);
		assertEquals(action, validator.getPostAction());
	}

	@Test
	void testSetPostAction_Replace() {
		int[] result = { 0 };
		final Runnable action = () -> {
		};
		final Runnable action2 = () -> result[0]++;
		validator.setPostAction(action);
		validator.setPostAction(action2);
		assertEquals(action2, validator.getPostAction());
	}

	@Test
	void testSetValidityConditions() {
		final Map<String, BiFunction<String, String, Boolean>> conditions = Map.of("condition 1 broken",
		        (s1, s2) -> true, "condition 2 broken", (s1, s2) -> false);
		validator.setValidityConditions(conditions);
		assertEquals(conditions, validator.getValidityConditions());
	}

	@Test
	void testSetValidityConditions_Replace() {
		final Map<String, BiFunction<String, String, Boolean>> conditions = Map.of("condition 1 broken",
		        (s1, s2) -> true, "condition 2 broken", (s1, s2) -> false);
		final Map<String, BiFunction<String, String, Boolean>> conditions2 = Map.of("condition 3 broken",
		        (s1, s2) -> false, "condition 4 broken", (s1, s2) -> true);
		validator.setValidityConditions(conditions);
		validator.setValidityConditions(conditions2);
		assertEquals(conditions2, validator.getValidityConditions());
	}

	@Test
	void testTriggerFeedback_BehaviourAggregate() {
		String[] result = { "not idle" };
		final List<String> errors = List.of("a1", "b1", "c1");
		final String expected = "a1\nb1\nc1";
		validator.setFeedbackConsumer(s -> result[0] = s);
		validator.setFeedbackBehaviour(FeedbackBehaviour.AGGREGATE);
		validator.setErrorMessages(errors);
		validator.triggerFeedback();
		assertEquals(expected, result[0]);
	}

	@Test
	void testTriggerFeedback_BehaviourAggregate_WithOne() {
		String[] result = { "not idle" };
		final List<String> errors = List.of("aggregateMe!");
		final String expected = "aggregateMe!";
		validator.setFeedbackConsumer(s -> result[0] = s);
		validator.setFeedbackBehaviour(FeedbackBehaviour.AGGREGATE);
		validator.setErrorMessages(errors);
		validator.triggerFeedback();
		assertEquals(expected, result[0]);
	}

	@Test
	void testTriggerFeedback_BehaviourFirst() {
		String[] result = { "not idle" };
		final List<String> errors = List.of("a1", "b1", "c1");
		final String expected = "a1";
		validator.setFeedbackConsumer(s -> result[0] = s);
		validator.setFeedbackBehaviour(FeedbackBehaviour.FIRST_ONLY);
		validator.setErrorMessages(errors);
		validator.triggerFeedback();
		assertEquals(expected, result[0]);
	}

	@Test
	void testTriggerFeedback_BehaviourFirst_WithOne() {
		String[] result = { "not idle" };
		final List<String> errors = List.of("onmeown");
		final String expected = "onmeown";
		validator.setFeedbackConsumer(s -> result[0] = s);
		validator.setFeedbackBehaviour(FeedbackBehaviour.FIRST_ONLY);
		validator.setErrorMessages(errors);
		validator.triggerFeedback();
		assertEquals(expected, result[0]);
	}

	@Test
	void testTriggerFeedback_BehaviourLast() {
		String[] result = { "not idle" };
		final List<String> errors = List.of("a1", "b1", "c1");
		final String expected = "c1";
		validator.setFeedbackConsumer(s -> result[0] = s);
		validator.setFeedbackBehaviour(FeedbackBehaviour.LAST_ONLY);
		validator.setErrorMessages(errors);
		validator.triggerFeedback();
		assertEquals(expected, result[0]);
	}

	@Test
	void testTriggerFeedback_BehaviourLast_WithOne() {
		String[] result = { "not idle" };
		final List<String> errors = List.of("first&last");
		final String expected = "first&last";
		validator.setFeedbackConsumer(s -> result[0] = s);
		validator.setFeedbackBehaviour(FeedbackBehaviour.LAST_ONLY);
		validator.setErrorMessages(errors);
		validator.triggerFeedback();
		assertEquals(expected, result[0]);
	}

	@Test
	void testTriggerFeedback_Idle() {
		String[] result = { "not idle" };
		validator.setFeedbackConsumer(s -> result[0] = s);
		validator.triggerFeedback();
		assertEquals(ListenerValidator.FEEDBACK_DEFAULT, result[0]);
	}

	@Test
	void testTriggerFeedback_NotSet() {
		String[] result = { "" };
		validator.triggerFeedback();
		assertEquals("", result[0]);
	}

	@Test
	void testTriggerPostAction() {
		int[] data = { 103 };
		final int expected = data[0] + 1;
		validator.setPostAction(() -> data[0]++);
		validator.triggerPostAction();
		assertEquals(expected, data[0]);
	}

	@Test
	void testTriggerPostAction_notSet() {
		validator.triggerPostAction();
		// Nothing to check?
	}

	@Test
	void testTriggerWhenInvalid() {
		boolean[] result = { true };
		validator.setActionWhenInvalid(b -> result[0] = false);
		validator.triggerWhenInvalid(anyBoolean());
		assertFalse(result[0]);
	}

	@Test
	void testTriggerWhenInvalid_NotSet() {
		boolean[] result = { true };
		validator.triggerWhenInvalid(anyBoolean());
		assertTrue(result[0]);
	}

	@Test
	void testTriggerWhenValid() {
		boolean[] result = { false };
		validator.setActionWhenValid(b -> result[0] = true);
		validator.triggerWhenValid(anyBoolean());
		assertTrue(result[0]);
	}

	@Test
	void testTriggerWhenValid_NotSet() {
		boolean[] result = { false };
		validator.triggerWhenInvalid(anyBoolean());
		assertFalse(result[0]);
	}

}
