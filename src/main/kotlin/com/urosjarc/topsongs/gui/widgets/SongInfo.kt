package com.urosjarc.topsongs.gui.widgets

import com.urosjarc.topsongs.app.log.LogService
import com.urosjarc.topsongs.app.song.*
import com.urosjarc.topsongs.shared.matchRatio
import com.urosjarc.topsongs.shared.startThread
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


abstract class SongInfoUi : KoinComponent {

	@FXML
	lateinit var nameTF: TextField

	@FXML
	lateinit var youtubeIdTF: TextField

	@FXML
	lateinit var feelingsCB: ComboBox<String>

	@FXML
	lateinit var placeCB: ComboBox<Int>

	@FXML
	lateinit var folderCB: ComboBox<String>

	@FXML
	lateinit var styleCB: ComboBox<String>

	@FXML
	lateinit var youtubeB: Button

	@FXML
	lateinit var saveB: Button

	@FXML
	lateinit var clearB: Button

	@FXML
	lateinit var deleteB: Button

	@FXML
	lateinit var searchB: Button

	@FXML
	lateinit var folderB: Button
}

class SongInfo : SongInfoUi() {
	val songRepo by this.inject<SongRepo>()
	val log by this.inject<LogService>()
	val songPlaceRepo by this.inject<SongPlaceRepo>()
	val songFolderRepo by this.inject<SongFolderRepo>()
	val songService by this.inject<SongService>()

	@FXML
	fun initialize() {
		this.saveB.setOnAction { this.save() }
		this.songRepo.onChose { this.setFields(it) }
		this.songRepo.onData { this.updateSongs() }
		this.songFolderRepo.onChose { this.updateFolder(folder = it) }
		this.songPlaceRepo.onChose { this.placeCB.value = it }

		this.folderCB.setOnAction { this.updateSongs() }
		this.folderB.setOnAction { this.updateFolder() }
		this.clearB.setOnAction { this.setFields(song = null) }
		this.deleteB.setOnAction { this.deleteSong() }
		this.youtubeB.setOnAction { this.youtube() }
		this.searchB.setOnAction { this.search() }

		this.resetFields()
	}

	private fun deleteSong() {
		this.songRepo.delete(songName = this.nameTF.text)
		this.setFields()
	}

	private fun updateFolder(folder: String? = null) {
		this.folderCB.value = folder ?: "${this.feelingsCB.value}/${this.styleCB.value}"
	}

	private fun resetFields() {
		val feelings = mutableSetOf<String>()
		val style = mutableSetOf<String>()
		val folders = mutableSetOf<String>()
		this.songRepo.data.forEach {
			style.add(it.style.toString())
			feelings.add(it.feeling.toString())
			folders.add(it.folder.toString())
		}
		this.styleCB.items.setAll(style)
		this.folderCB.items.setAll(folders)
		this.feelingsCB.items.setAll(feelings)
		this.setFields()
	}

	private fun updateSongs() {
		val folderSongs = this.songRepo.data
			.filter { if (it.folder != null) this.folderCB.value == it.folder else false }
			.sortedBy { it.place }

		if (folderSongs.isNotEmpty()) {
			this.songRepo.select(folderSongs)
			this.placeCB.items.setAll((1..folderSongs.size).toList())
		}
	}

	private fun setFields(song: Song? = null) {
		this.placeCB.value = song?.place
		this.styleCB.value = song?.style ?: ""
		this.feelingsCB.value = song?.feeling ?: ""
		this.folderCB.value = song?.folder ?: ""
		this.nameTF.text = song?.name ?: ""
		this.youtubeIdTF.text = song?.youtubeId ?: ""
	}

	private fun save() {
		val song = this.getSong()
		if (song == null) {
			this.log.warn("Song is missing info.")
			return
		}
		song.rated = true
		this.songRepo.save(song)
		this.songRepo.chose(song)
		this.resetFields()
	}

	fun youtube() = startThread {
		val name = this.nameTF.text
		val id = this.youtubeIdTF.text
		if (id.isBlank()) {
			this.songService.youtubeSearch(query = name)
		} else {
			this.songService.youtubePlay(id = id)
		}
	}

	private fun getSong(): Song? {
		val song = Song(
			name = this.nameTF.text,
			created = Clock.System.now(),
			rated = false,
			feeling = this.feelingsCB.value,
			style = this.styleCB.value,
			folder = this.folderCB.value,
			place = this.placeCB.value.toString().toIntOrNull(),
			youtubeId = this.songService.youtubeId(link = this.youtubeIdTF.text)
		)

		if (
			song.feeling?.isBlank() == true ||
			song.style?.isBlank() == true ||
			song.folder?.isBlank() == true ||
			song.youtubeId?.isBlank() == true ||
			song.place == null
		) return null

		return song
	}

	fun search() = startThread {
		val name = this.nameTF.text
		val folder = this.folderCB.value
		val style = this.styleCB.value
		val feeling = this.feelingsCB.value
		val youtube = this.youtubeIdTF.text
		val place = this.placeCB.value.toString().toIntOrNull()

		val songs: List<Song> = this.songRepo.data.filter {
			var pass = true
			if (folder.isNotBlank() && folder != it.folder) pass = false
			if (style.isNotBlank() && style != it.style) pass = false
			if (feeling.isNotBlank() && feeling != it.feeling) pass = false
			if (youtube.isNotBlank() && youtube != it.youtubeId) pass = false
			if (place != null && place != it.place) pass = false
			pass
		}.sortedByDescending { matchRatio(first = it.name, second = name) }

		this.songRepo.select(songs)
	}
}
