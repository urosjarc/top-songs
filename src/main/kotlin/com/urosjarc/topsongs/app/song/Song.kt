package com.urosjarc.topsongs.app.song

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable

data class Song(
	val name: String,
	val created: Instant,
	var rated: Boolean,
	var feeling: String? = null,
	var style: String? = null,
	var folder: String? = null,
	var place: Int? = null,
	var youtubeId: String? = null
) {
	fun merge(song: Song): Boolean {
		if (song == this) {
			this.rated = song.rated
			this.feeling = song.feeling
			this.style = song.style
			this.folder = song.folder
			this.place = song.place
			this.youtubeId = song.youtubeId
			return true
		}
		return false
	}

	override fun toString(): String {
		return this.name
	}

	override fun hashCode(): Int {
		return this.name.hashCode()
	}

	override fun equals(other: Any?): Boolean {
		return this.hashCode() == other.hashCode()
	}
}
