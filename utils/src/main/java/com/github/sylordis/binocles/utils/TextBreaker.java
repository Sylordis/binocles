package com.github.sylordis.binocles.utils;

import java.text.BreakIterator;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import com.google.common.base.Preconditions;

/**
 * Class to break texts in a human readable fashion.
 */
public class TextBreaker {

	/**
	 * Default breakpoints.
	 */
	public static final String[] DEFAULT_BREAKPOINTS = new String[] { ".", "\n" };
	/**
	 * Value for no threshold for a given value.
	 */
	public static int NO_THRESHOLD = -1;

	/**
	 * The policy used to determine where to break.
	 */
	public enum BreakingPolicy {
		/**
		 * Break at first sentence break found in the allowed range.
		 */
		FIRST,
		/**
		 * Break at last sentence break found in the allowed range.
		 */
		LAST;
	}

	/**
	 * Helper enum to set the breaking direction in reading terms.
	 */
	public enum ReadDirection {
		/**
		 * Read the text backwards to find the next breaking point.
		 */
		BACKWARD,
		/**
		 * Read the text forward to find the next breaking point.
		 */
		FORWARD;
	}

	/**
	 * Breaking priority. Default is {@link BreakingPolicy#FIRST}.
	 */
	private BreakingPolicy breakingPolicy;
	/**
	 * Direction of the text breaking. <code>true</code> is forward, <code>false</code> is backward.
	 * Default is forward.
	 */
	private boolean readsForward;
	/**
	 * Definition of break points. Default is ".".
	 */
	private Set<String> breakpoints;
	/**
	 * Definition of mandatory break points. Default is new line (LF, \n).
	 */
	private Set<String> mandatoryBreakpoints;
	// TODO Text breaking policy leniency: if lenient, includes the last word if a sentence breaking
	// point can be found after (according to breaking direction).
	// TODO Text breaking style: last word or break directly at max threshold if no breakpoint is found.

	/**
	 * Creates a forward text breaker that will break on first sentence break point found.
	 */
	public TextBreaker() {
		this(BreakingPolicy.FIRST, ReadDirection.FORWARD);
	}

	/**
	 * Creates a new text breaker with default breakpoints.
	 * 
	 * @param policy    breaking policy
	 * @param direction breaking direction
	 */
	public TextBreaker(BreakingPolicy policy, ReadDirection direction) {
		this(policy, direction, null, null);
	}

	/**
	 * Creates a new text breaker without mandatory breakpoints.
	 * @param policy               breaking policy
	 * @param direction            breaking direction
	 * @param breakpoints
	 */
	public TextBreaker(BreakingPolicy policy, ReadDirection direction, Collection<String> breakpoints) {
		this(policy, direction, breakpoints, null);
	}
	
	/**
	 * Creates a new text breaker fully customised.
	 * 
	 * @param policy               breaking policy
	 * @param direction            breaking direction
	 * @param breakpoints
	 * @param mandatoryBreakpoints
	 */
	public TextBreaker(BreakingPolicy policy, ReadDirection direction, Collection<String> breakpoints,
	        Collection<String> mandatoryBreakpoints) {
		this.breakingPolicy = policy;
		this.readsForward = direction == ReadDirection.FORWARD ? true : false;
		this.breakpoints = new HashSet<>();
		if (breakpoints != null)
			this.breakpoints.addAll(breakpoints);
		else
			this.breakpoints.addAll(Arrays.asList(DEFAULT_BREAKPOINTS));
		this.mandatoryBreakpoints = new HashSet<>();
		if (mandatoryBreakpoints != null)
			this.mandatoryBreakpoints.addAll(mandatoryBreakpoints);
	}

	/**
	 * Tries to find where the next break point of the text is, starting from a position, allowing
	 * thresholds for minimum and maximum break point position. If the maximum breaking position is
	 * inferior to the minimum, then it will be considered to be no maximum threshold at all.
	 * 
	 * @param text text to break
	 * @param from starting index of the search in the text
	 * @param min  minimum length before starting to try to break, in number of characters
	 * @param max  maximum length allowed to reach to find the break point, in number of characters
	 * @return -1 if there's no closest text break point, if the text is null or from is out of bounds.
	 * @throws NullPointerException      if text is null
	 * @throws IndexOutOfBoundsException if from is out of text bounds
	 */
	public int findClosestBreakingPoint(String text, int from, int min, int max) {
		// TODO Backward
		Preconditions.checkNotNull(text, "Cannot find breaking point on a null text.");
		Preconditions.checkElementIndex(from, text.length(),
		        "Cannot find breaking point from an out of bounds index (" + from + " in " + text.length() + ").");
		int index = -1;
		// Check that minimum threshold can be done
		if ((readsForward && from + min < text.length()) || (!readsForward && from - min > 0)) {
			Function<BreakIterator, Integer> iterator = readsForward ? BreakIterator::next : BreakIterator::previous;
			Function<BreakIterator, Integer> stepBack = readsForward ? BreakIterator::previous : BreakIterator::next;
			// Set true max if it's inferior to min and having from+max > text.length() does not matter because
			// the break iterator will stop at the end of the text
			int trueMax = max < min ? Integer.MAX_VALUE : max;
			BreakIterator wb = BreakIterator.getWordInstance();
			wb.setText(text);
			int lastFound = -1;
			// Start point
			int previous = from + min * (readsForward ? 1 : -1);
			// Skip to start point
			wb.following(previous);
			// Get beginning of next word
			int current = iterator.apply(wb);
//			System.out.println("(start) from=" + from + " min=" + min + " max=" + max + " length="
//			        + Math.abs(from - current) + " word=" + substringAdapt(text, previous, current));
			// Go along the text as long you don't go over the true maximum threshold
			while (Math.abs(from - current) < trueMax && current != BreakIterator.DONE) {
//				System.out.print("(curr) from=" + from + " min=" + min + " max=" + max + " lastFound=" + lastFound
//				        + " current=" + current + " length=" + Math.abs(from - current));
//				System.out.println(" word=[" + substringAdapt(text, previous, current) + "]");
				// Found a break point
				if (breakpoints.contains(substringAdapt(text, previous, current))) {
					// Set last breakpoint found, including the breakpoint character itself
					lastFound = current;
					if (breakingPolicy == BreakingPolicy.FIRST)
						break;
				}
				previous = current;
				current = iterator.apply(wb);
			}
			// Manage last index
			if (lastFound == -1 && readsForward) {
//				System.out.println("forward not found");
				// No breakpoint was found, so cancel last 2 previous advances (current word and previous
				// whitespace).
				index = stepBack.apply(wb);
//				System.out.println("(back) text=[" + substringAdapt(text, from, index) + "]");
				index = stepBack.apply(wb);
			} else if (lastFound == -1 && !readsForward) {
//				System.out.println("backward not found");
				// If reading backward, just step back once (whitespace).
				if (current == -1)
					index = 0;
				else if (min > 0) {
					index = stepBack.apply(wb);
//					System.out.println("(back) text=[" + substringAdapt(text, from, index) + "]");
					index = stepBack.apply(wb);
				} else
					index = current;
			} else if (readsForward) {
//				System.out.println("forward found");
				index = lastFound;
			} else if (!readsForward) {
//				System.out.println("backward found");
				index = wb.following(lastFound);
				while (!Character.isLetter(text.charAt(index))) {
					index = stepBack.apply(wb);
//					System.out.println("(step) from=" + from + " min=" + min + " max=" + max + " lastFound=" + lastFound
//					        + " length=" + Math.abs(from - index) + " index=" + index + " charAt(i)=" + text.charAt(index));
				}
			}
//			System.out.print("(end ) from=" + from + " min=" + min + " max=" + max + " lastFound=" + lastFound
//			        + " current=" + current + " previous=" + previous + " length=" + Math.abs(from - current)
//			        + " index=" + index);
//			System.out.println(" text=[" + substringAdapt(text, from, index) + "]");
		} else
			index = readsForward ? text.length() : 0;
		return index;
	}

	/**
	 * Extracts a portion of text in a range. Calls {@link String#substring(int, int)} but always take
	 * the lowest between a and b for begin index and the highest for end index.
	 * 
	 * @param text the text to substring
	 * @param a    index
	 * @param b    index
	 * @return
	 */
	private String substringAdapt(String text, int a, int b) {
		return text.substring(Math.min(a, b), Math.max(a, b));
	}

	/**
	 * Breaks the text from a given point and provides a text excerpt according to reading direction.
	 * 
	 * @param text text to break
	 * @param from index to start the breaking from
	 * @param min  minimum length before breaking
	 * @param max  maximum length before breaking
	 * @return null if the text is null
	 * @throws IndexOutOfBoundsException if from is out of text bounds
	 * @throws NullPointerException      if the text is null
	 */
	public String breakText(String text, int from, int min, int max) {
		Preconditions.checkNotNull(text, "Cannot break a null text.");
		Preconditions.checkElementIndex(from, text.length(),
		        "Cannot start breaking out of bounds of the text (" + from + ").");
		String result = null;
		int index = findClosestBreakingPoint(text, from, min, max);
		if (index != -1) {
			if (readsForward)
				result = text.substring(from, index);
			else
				result = text.substring(index, from);
		}
		return result;
	}

	/**
	 * @return the break policy
	 */
	public BreakingPolicy getBreakPolicy() {
		return breakingPolicy;
	}

	/**
	 * @param breakingPolicy the breakPriority to set
	 */
	public void setBreakingPolicy(BreakingPolicy breakingPolicy) {
		this.breakingPolicy = breakingPolicy;
	}

	/**
	 * Checks if this breaker is set to break in a forward direction.
	 * 
	 * @return the forward
	 */
	public boolean doesReadForward() {
		return readsForward;
	}

	/**
	 * @param direction the breaking direction to set
	 */
	public void setReadDirection(ReadDirection direction) {
		this.readsForward = direction == ReadDirection.FORWARD;
	}

	/**
	 * @return the breakpoints
	 */
	public Set<String> getBreakpoints() {
		return breakpoints;
	}

	/**
	 * @param breakpoints the breakPoints to set
	 */
	public void setBreakpoints(Set<String> breakpoints) {
		this.breakpoints = breakpoints;
	}

	/**
	 * @return the mandatory breakpoints
	 */
	public Set<String> getMandatoryBreakpoints() {
		return mandatoryBreakpoints;
	}

	/**
	 * @param breakpoints the mandatoryBreakpoints to set
	 */
	public void setMandatoryBreakpoints(Set<String> breakpoints) {
		this.mandatoryBreakpoints = breakpoints;
	}

}
