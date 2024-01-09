package com.urosjarc.topsongs.app.song

class SongService {
	fun getEmotions(emotionGroup: String): MutableSet<String> {
		return Emotion.Type.values()
			.filter { it.name.split("_").first() == emotionGroup }
			.map { it.name.split("_").last() }
			.toMutableSet()
	}
}
