package com.urosjarc.topsongs.gui.widgets

import com.urosjarc.topsongs.app.log.LogService
import com.urosjarc.topsongs.app.song.Song
import com.urosjarc.topsongs.app.song.SongRepo
import com.urosjarc.topsongs.shared.setColumnWidth
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.control.*
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


abstract class SongListUi : KoinComponent {

	@FXML
	lateinit var nameTF: TextField

	@FXML
	lateinit var emotionCB: ComboBox<String>

	@FXML
	lateinit var placeCB: ComboBox<String>

	@FXML
	lateinit var folderCB: ComboBox<String>

	@FXML
	lateinit var styleCB: ComboBox<String>

	@FXML
	lateinit var saveB: Button

	@FXML
	lateinit var clearB: Button

	@FXML
	lateinit var deleteB: Button
}

class SongList : SongListUi() {
	lateinit var songTV: TableView<Song>
	lateinit var nameTC: TableColumn<Song, String>
	lateinit var placeTC: TableColumn<Song, Int>
	val songRepo by this.inject<SongRepo>()
	val log by this.inject<LogService>()

	@FXML
	fun initialize() {
		this.saveB.setOnAction { this.save() }
		this.songRepo.onChose { this.updateFields(it) }
		this.songRepo.onData { this.updateSongs() }
		this.folderCB.setOnAction { this.updateSongs() }
		this.emotionCB.setOnAction { this.updateFolder() }
		this.styleCB.setOnAction { this.updateFolder() }
		this.songTV.setOnMouseClicked { this.selectSong() }
		this.clearB.setOnAction { this.updateFields(song = null) }
		this.deleteB.setOnAction { this.deleteSong() }

		this.placeTC.setCellValueFactory { SimpleObjectProperty(it.value.place) }
		this.nameTC.setCellValueFactory { SimpleStringProperty(it.value.name) }

		this.songTV.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY;
		setColumnWidth(this.placeTC, 10)
		setColumnWidth(this.nameTC, 90)

		this.init()
	}

	private fun deleteSong() = this.getSong()?.let { this.songRepo.delete(it) }

	private fun selectSong() {
		this.songTV.selectionModel.selectedItem?.let {
			if (this.nameTF.text.isNotEmpty()) this.placeCB.value = it.place.toString()
			else this.updateFields(song = it)
		}
	}

	private fun updateFolder() {
		this.folderCB.value = "${this.emotionCB.value}/${this.styleCB.value}"
	}

	private fun init() {
		val emotions = mutableSetOf<String>()
		val style = mutableSetOf<String>()
		val folders = mutableSetOf<String>()
		this.songRepo.data.forEach {
			style.add(it.style.toString())
			emotions.add(it.emotion.toString())
			folders.add(it.folder.toString())
		}
		this.styleCB.items.setAll(style)
		this.folderCB.items.setAll(folders)
		this.emotionCB.items.setAll(emotions)

		this.updateSongs()
	}

	private fun updateSongs() {
		val folderSongs = this.songRepo.data.filter {
			if (it.folder != null) this.folderCB.value == it.folder else false
		}
		this.placeCB.items.setAll((1..folderSongs.size).map { it.toString() })
		this.songTV.items.setAll(folderSongs)
	}

	private fun updateFields(song: Song?) {
		this.styleCB.value = song?.style ?: ""
		this.emotionCB.value = song?.emotion ?: ""
		this.folderCB.value = song?.folder ?: ""
		this.placeCB.value = song?.place.toString()
		this.nameTF.text = song?.name ?: ""
	}

	private fun save() {
		val song = this.getSong()
		if (song == null) {
			this.log.warn("Song is missing info.")
			return
		}
		this.songRepo.save(song)
		this.songRepo.chose(song)
		this.init()
	}

	private fun getSong(): Song? {
		val song = Song(
			name = this.nameTF.text,
			created = Clock.System.now(),
			rated = true,
			emotion = this.emotionCB.value,
			style = this.styleCB.value,
			folder = this.folderCB.value,
			place = this.placeCB.value.toIntOrNull()
		)

		if (
			song.emotion?.isBlank() != false ||
			song.style?.isBlank() != false ||
			song.folder?.isBlank() != false ||
			song.place == null
		) return null

		return song
	}
}
