package com.github.sylordis.binocle.ui.answers;

import java.util.Optional;

import com.github.sylordis.binocle.model.review.Nomenclature;

public record CreateBookAnswer(String title, Optional<Nomenclature> nomenclature) {
	
}
