package com.urosjarc.topsongs.app.playlist

import com.urosjarc.topsongs.shared.Repository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class PlaylistRepo(val fileName: String) : Repository<Playlist>() {
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

	override fun save(t: Playlist): Playlist {
		val old = super.save(t)
		this.chose(old)
		return old
	}
}
