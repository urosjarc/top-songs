package com.urosjarc.topsongs.gui.widgets

import com.urosjarc.topsongs.app.song.Song
import com.urosjarc.topsongs.app.song.SongRepo
import com.urosjarc.topsongs.app.song.SongService
import com.urosjarc.topsongs.app.stream.Stream
import com.urosjarc.topsongs.app.stream.StreamRepo
import com.urosjarc.topsongs.shared.startThread
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class SongQueueUi : KoinComponent {

	@FXML
	lateinit var songL: Label

	@FXML
	lateinit var onAirL: Label

	@FXML
	lateinit var saveB: Button

	@FXML
	lateinit var songLV: ListView<Song>

}

class SongQueue : SongQueueUi() {
	val streamRepo by this.inject<StreamRepo>()
	val songRepo by this.inject<SongRepo>()
	val songService by this.inject<SongService>()

	@FXML
	fun initialize() {
		this.streamRepo.onChose { this.onStream(stream=it) }
		this.songRepo.onData { this.update() }
		this.saveB.setOnAction { this.songRepo.save(this.streamRepo.chosen!!.song) }
		this.songLV.setOnMouseClicked { this.clicked() }

		this.update()
	}

	fun onStream(stream: Stream) {
		val now = Clock.System.now()
		val diff = (now - stream.updated)
		val sec = diff.inWholeSeconds % 60
		val min = diff.inWholeMinutes
		this.onAirL.text = if(min > 0) "${min} min, ${sec} sec" else "${sec} sec"
		this.songL.text = stream.song.name
	}

	fun clicked() = startThread {
		val song = this.songLV.selectionModel.selectedItem
		if (song != null) {
			this.songRepo.chose(song)
			this.songService.youtubeSearch(song.name)
		}
	}

	fun update() {
		val unratedSongs = this.songRepo.data.filter { !it.rated }
		this.songLV.items.setAll(unratedSongs)
	}
}
