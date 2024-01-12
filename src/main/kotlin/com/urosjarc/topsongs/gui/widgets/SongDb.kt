package com.urosjarc.topsongs.gui.widgets

import com.urosjarc.topsongs.app.song.SongNode
import com.urosjarc.topsongs.app.song.SongRepo
import com.urosjarc.topsongs.app.song.SongService
import com.urosjarc.topsongs.shared.setColumnWidth
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableColumn
import javafx.scene.control.TreeTableView
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


abstract class SongDbUi : KoinComponent {
	@FXML
	lateinit var foldersTTV: TreeTableView<SongNode>

	@FXML
	lateinit var songTTC_0: TreeTableColumn<SongNode, String>

	@FXML
	lateinit var placeTTC_0: TreeTableColumn<SongNode, String>

	@FXML
	lateinit var folderTTC_0: TreeTableColumn<SongNode, String>

	@FXML
	lateinit var emotionsTTV: TreeTableView<SongNode>

	@FXML
	lateinit var songTTC_1: TreeTableColumn<SongNode, String>

	@FXML
	lateinit var placeTTC_1: TreeTableColumn<SongNode, String>

	@FXML
	lateinit var folderTTC_1: TreeTableColumn<SongNode, String>
}

class SongDb : SongDbUi() {

	val songRepo by this.inject<SongRepo>()
	val songService by this.inject<SongService>()

	@FXML
	fun initialize() {
		this.foldersTTV.columnResizePolicy = TreeTableView.UNCONSTRAINED_RESIZE_POLICY
		setColumnWidth(this.songTTC_0, 33)
		setColumnWidth(this.placeTTC_0, 33)
		setColumnWidth(this.folderTTC_0, 33)
		this.songTTC_0.setCellValueFactory { ReadOnlyStringWrapper(it.value.value.song?.name) }
		this.placeTTC_0.setCellValueFactory { ReadOnlyStringWrapper((it.value.value.song?.place ?: "").toString()) }
		this.folderTTC_0.setCellValueFactory { ReadOnlyStringWrapper(if (it.value.value.song == null) it.value.value.name else "") }

		this.emotionsTTV.columnResizePolicy = TreeTableView.UNCONSTRAINED_RESIZE_POLICY
		setColumnWidth(this.songTTC_1, 25)
		setColumnWidth(this.placeTTC_1, 25)
		setColumnWidth(this.folderTTC_1, 33)
		this.songTTC_1.setCellValueFactory { ReadOnlyStringWrapper(it.value.value.song?.name) }
		this.placeTTC_1.setCellValueFactory { ReadOnlyStringWrapper((it.value.value.song?.place ?: "").toString()) }
		this.folderTTC_1.setCellValueFactory { ReadOnlyStringWrapper(if (it.value.value.song == null) it.value.value.name else "") }

		this.updateTree(ttv = this.emotionsTTV, this.songService.songEmotionTree())
		this.updateTree(ttv = this.foldersTTV, this.songService.songFolderTree())
	}

	fun updateTree(ttv: TreeTableView<SongNode>, root: SongNode) {
		ttv.root = TreeItem(root)
		ttv.root.isExpanded = true
		val queue = mutableListOf(ttv.root)
		while (queue.isNotEmpty()) {
			val current = queue.removeFirst()
			current.value.children.forEach {
				val newNode = TreeItem(it)
				newNode.isExpanded = false
				current.children.add(newNode)
				queue.add(newNode)
			}
		}
	}
}
