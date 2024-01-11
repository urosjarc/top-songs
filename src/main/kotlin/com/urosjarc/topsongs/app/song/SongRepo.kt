package com.urosjarc.topsongs.app.song

import com.urosjarc.topsongs.shared.Repository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class SongRepo(val fileName: String) : Repository<Song>() {
	init {
		this.load()
	}

	override fun load() {
		val file = File(this.fileName)
		if (!file.exists()) return
		this.set(Json.decodeFromString(file.readText()))
	}

	override fun save() {
		val file = File(this.fileName)
		if (!file.exists()) file.createNewFile()
		file.writeText(Json.encodeToString(this.data))
	}

	fun delete(songName: String): Song {
		return this.data.first { it.name == songName }
	}

	/**
	 * If song allready exists it merge new song with the old one,
	 * save the file and notify listeners of the change
	 */
	override fun save(t: Song): Song {
		val oldSong = this.find(t)
		val song = if (oldSong == null) super.save(t) else {
			oldSong.merge(t)
			this.save()
			this.onDataNotify()
			oldSong
		}
		return song
	}
}
