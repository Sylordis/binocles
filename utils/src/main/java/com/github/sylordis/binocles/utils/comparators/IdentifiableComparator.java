package com.github.sylordis.binocles.utils.comparators;

import java.util.Comparator;

import com.github.sylordis.binocles.utils.contracts.Identifiable;

/**
 * Comparator on {@link Identifiable} class. An {@link Identifiable} i1 will be located before i2 if
 * it doesn't have an ID or if its ID is before in natural order.
 * 
 * @see String#compareTo(String)
 * @author sylordis
 *
 */
public class IdentifiableComparator implements Comparator<Identifiable> {

	@Override
	public int compare(Identifiable o1, Identifiable o2) {
		int pos = 0;
		if (o1.hasId() && !o2.hasId())
			pos = 1;
		else if (!o1.hasId() && o2.hasId())
			pos = -1;
		else
			pos = o1.getId().compareTo(o2.getId());
		return pos;
	}

}
