package com.urosjarc.topsongs.gui.widgets

import com.urosjarc.topsongs.app.song.*
import com.urosjarc.topsongs.shared.setColumnWidth
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableColumn
import javafx.scene.control.TreeTableView
import javafx.scene.input.MouseEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


abstract class SongDbUi : KoinComponent {
	@FXML
	lateinit var foldersTTV: TreeTableView<SongNode>

	@FXML
	lateinit var songTTC_0: TreeTableColumn<SongNode, String>

	@FXML
	lateinit var placeTTC_0: TreeTableColumn<SongNode, Int>

	@FXML
	lateinit var folderTTC_0: TreeTableColumn<SongNode, String>

	@FXML
	lateinit var feelingsTTV: TreeTableView<SongNode>

	@FXML
	lateinit var songTTC_1: TreeTableColumn<SongNode, String>

	@FXML
	lateinit var placeTTC_1: TreeTableColumn<SongNode, Int>

	@FXML
	lateinit var folderTTC_1: TreeTableColumn<SongNode, String>
}

class SongDb : SongDbUi() {

	val songRepo by this.inject<SongRepo>()
	val songService by this.inject<SongService>()
	val songFolderRepo by this.inject<SongFolderRepo>()
	val songPlaceRepo by this.inject<SongPlaceRepo>()

	@FXML
	fun initialize() {
		this.songRepo.onData { this.updateTrees() }
		this.foldersTTV.setOnMousePressed { this.clicked(it, this.foldersTTV) }
		this.feelingsTTV.setOnMousePressed { this.clicked(it, this.feelingsTTV) }

		this.foldersTTV.columnResizePolicy = TreeTableView.CONSTRAINED_RESIZE_POLICY
		setColumnWidth(this.folderTTC_0, 20)
		setColumnWidth(this.placeTTC_0, 20)
		setColumnWidth(this.songTTC_0, 60)
		this.songTTC_0.setCellValueFactory { ReadOnlyStringWrapper(it.value.value.song?.name) }
		this.placeTTC_0.setCellValueFactory { SimpleObjectProperty(it.value.value.song?.place) }
		this.folderTTC_0.setCellValueFactory { ReadOnlyStringWrapper(if (it.value.value.song == null) it.value.value.name else "") }

		this.feelingsTTV.columnResizePolicy = TreeTableView.CONSTRAINED_RESIZE_POLICY
		setColumnWidth(this.folderTTC_1, 20)
		setColumnWidth(this.placeTTC_1, 20)
		setColumnWidth(this.songTTC_1, 60)
		this.songTTC_1.setCellValueFactory { ReadOnlyStringWrapper(it.value.value.song?.name) }
		this.placeTTC_1.setCellValueFactory { SimpleObjectProperty((it.value.value.song?.place)) }
		this.folderTTC_1.setCellValueFactory { ReadOnlyStringWrapper(if (it.value.value.song == null) it.value.value.name else "") }

		this.updateTrees()
	}

	private fun clicked(mouseEvent: MouseEvent, ttc: TreeTableView<SongNode>) {
		val songNode: SongNode = ttc.selectionModel.selectedItem?.value ?: return
		val song = songNode.song
		if (song != null) {
			if (mouseEvent.isPrimaryButtonDown) this.songRepo.chose(song)
			else if(ttc == this.foldersTTV) {
				this.songFolderRepo.chose(song.folder!!)
				this.songPlaceRepo.chose(song.place!!)
			}
		} else if (mouseEvent.isSecondaryButtonDown && ttc == this.foldersTTV) {
			this.songFolderRepo.chose(songNode.folderPath())
		}
	}

	private fun updateTrees() {
		this.updateTree(ttv = this.feelingsTTV, this.songService.songFeelingsTree())
		this.updateTree(ttv = this.foldersTTV, this.songService.songFoldersTree())
	}

	private fun updateTree(ttv: TreeTableView<SongNode>, root: SongNode) {
		ttv.root = TreeItem(root)
		ttv.root.isExpanded = true
		val queue = mutableListOf(ttv.root)
		while (queue.isNotEmpty()) {
			val current = queue.removeFirst()
			current.value.children.forEach {
				val newNode = TreeItem(it)
				newNode.isExpanded = true
				current.children.add(newNode)
				queue.add(newNode)
			}
		}
	}
}
