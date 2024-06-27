package com.github.sylordis.binocles.ui.functional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

import com.github.sylordis.binocles.ui.views.BinoclesTabPane;

import javafx.scene.control.Tab;

public class TabFilterPredicate implements Predicate<Tab> {

	private Collection<Predicate<Object>> predicates;

	public TabFilterPredicate() {
		predicates = new ArrayList<>();
	}

	@Override
	public boolean test(Tab t) {
		BinoclesTabPane pane = (BinoclesTabPane) t.getContent();
		boolean flag = true;
		for (var predicate : predicates) {
			flag = flag && predicate.test(pane.getItem());
		}
		return flag;
	}

	public TabFilterPredicate isItemType(Class<?> type) {
		predicates.add(o -> o != null && o.getClass().equals(type));
		return this;
	}

	public TabFilterPredicate isItem(Object item) {
		predicates.add(o -> o != null && o.equals(item));
		return this;
	}

	public TabFilterPredicate add(Predicate<Object> predicate) {
		predicates.add(predicate);
		return this;
	}
}
