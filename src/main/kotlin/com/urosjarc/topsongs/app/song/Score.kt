package com.urosjarc.topsongs.app.song

import kotlinx.serialization.Serializable

@Serializable
data class Score(
	val rating: Rating,
	val emotion: MutableList<Emotion> = mutableListOf(),
) {

	enum class Rating {
		BAD, POOR, AVERAGE, GOOD, EXCELENT, EXCEPTIONAL
	}

}
