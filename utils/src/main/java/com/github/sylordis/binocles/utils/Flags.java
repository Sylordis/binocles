package com.github.sylordis.binocles.utils;

/**
 * This class works with binary integer flags.
 */
public final class Flags {

	/**
	 * Triggers a runnable if all provided flags are matching the desired values.
	 * 
	 * @param doing runnable to trigger
	 * @param flags tuples of flags, with (input flags, desired flag)
	 */
	public static void ifAllDo(Runnable doing, int... flags) {
		boolean flagged = flags.length >= 2;
		for (int i = 0; i + 1 < flags.length; i += 2)
			flagged = flagged && has(flags[i], flags[i + 1]);
		if (flagged)
			doing.run();
	}

	/**
	 * Checks if an input matches a flag.
	 * 
	 * @param input provided input
	 * @param flag  desired flag
	 * @return true if the flag matches, false otherwise
	 */
	public static boolean has(int input, int flag) {
		return (input & flag) == flag;
	}

	// Private constructor to prevent instantiation
	private Flags() {
		// Nothing to do here
	}

}
