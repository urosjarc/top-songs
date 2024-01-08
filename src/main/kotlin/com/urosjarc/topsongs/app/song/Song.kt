package com.urosjarc.topsongs.app.song

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Song(
	val name: String,
	val created: Instant,
	val atStart: Score? = null,
	val atEnd: Score? = null,
	val voice: Score? = null,
	val lyrics: Score? = null,
	val melody: Score? = null,
	val harmony: Score? = null,
	val complexity: Score? = null,
	val progression: Score? = null,
	val originality: Score? = null,
	val repetivness: Score? = null,
	val story: Score? = null,
) {
	override fun hashCode(): Int {
		return this.name.hashCode()
	}
	override fun equals(other: Any?): Boolean {
		return this.hashCode() == other.hashCode()
	}
}
