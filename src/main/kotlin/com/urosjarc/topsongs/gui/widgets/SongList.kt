package com.urosjarc.topsongs.gui.widgets

import com.urosjarc.topsongs.app.song.Song
import com.urosjarc.topsongs.app.song.SongPlaceRepo
import com.urosjarc.topsongs.app.song.SongRepo
import com.urosjarc.topsongs.shared.setColumnWidth
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.input.MouseEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


abstract class SongListUi : KoinComponent {
	@FXML
	lateinit var songTV: TableView<Song>

	@FXML
	lateinit var nameTC: TableColumn<Song, String>

	@FXML
	lateinit var placeTC: TableColumn<Song, Int>
}

class SongList : SongListUi() {
	val songRepo by this.inject<SongRepo>()
	val songPlaceRepo by this.inject<SongPlaceRepo>()

	@FXML
	fun initialize() {
		this.songRepo.onSelect { this.songTV.items.setAll(it) }
		this.songTV.setOnMousePressed { this.clicked(it) }

		this.placeTC.setCellValueFactory { SimpleObjectProperty(it.value.place) }
		this.nameTC.setCellValueFactory { SimpleStringProperty(it.value.name) }

		this.songTV.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY;
		setColumnWidth(this.placeTC, 10)
		setColumnWidth(this.nameTC, 90)
	}

	private fun clicked(mouseEvent: MouseEvent) {
		this.songTV.selectionModel.selectedItem?.let {
			if (mouseEvent.isPrimaryButtonDown) this.songRepo.chose(it)
			else if (it.place != null) this.songPlaceRepo.chose(it.place!!)
		}
	}
}
