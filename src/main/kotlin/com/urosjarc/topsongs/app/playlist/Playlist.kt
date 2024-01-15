package com.urosjarc.topsongs.app.playlist

import com.urosjarc.topsongs.app.song.Song
import kotlinx.datetime.Instant

data class Playlist(
	val name: String,
	val created: Instant,
	val songs: MutableList<Song>
)
