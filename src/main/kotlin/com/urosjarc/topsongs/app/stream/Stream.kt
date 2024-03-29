package com.urosjarc.topsongs.app.stream

import com.urosjarc.topsongs.app.radio.Radio
import com.urosjarc.topsongs.app.song.Song
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Stream(
	val radio: Radio,
	var song: Song?,
	val created: Instant,
	var info: String?
)
