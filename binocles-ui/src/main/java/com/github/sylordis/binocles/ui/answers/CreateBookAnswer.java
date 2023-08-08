package com.github.sylordis.binocles.ui.answers;

import java.util.Optional;

import com.github.sylordis.binocles.model.review.Nomenclature;

public record CreateBookAnswer(String title, Optional<Nomenclature> nomenclature) {
	
}
