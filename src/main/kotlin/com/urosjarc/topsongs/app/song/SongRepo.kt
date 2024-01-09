package com.urosjarc.topsongs.app.song

import com.urosjarc.topsongs.shared.Repository
import java.io.File
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SongRepo(val fileName: String) : Repository<Song>(){
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

	override fun save(t: Song){
		super.save(t)
		this.chose(t)
	}
}
