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
		if (oldSong == null) {
			if (t.place == null) return super.save(t)
			return this.insertInFolder(song = t, new = true)
		} else {
			oldSong.merge(t)
			return this.insertInFolder(song = oldSong, new = false)
		}
	}

	private fun insertInFolder(song: Song, new: Boolean): Song {
		if (!new) {
			//Remove from data stack
			this.data.remove(song)
		}

		//Get folder songs
		val folderSongs = this.data
			.filter { it.folder == song.folder }
			.sortedBy { it.place!! }

		//Force reordering of the songs
		folderSongs.forEachIndexed { i, t -> t.place = i + 1 }

		if (folderSongs.isEmpty()) song.place = 1
		else {
			val first = folderSongs.first()
			val last = folderSongs.last()

			//Limit song place
			if (song.place!! < first.place!!) song.place = first.place
			if (song.place!! > last.place!!) song.place = last.place!! + 1

			// Increment others to make place for the new song
			folderSongs
				.filter { it.place!! >= song.place!! }
				.forEach { it.place = it.place!! + 1 }
		}

		return super.save(t = song)
	}
}
