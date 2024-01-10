package com.urosjarc.topsongs.gui.widgets

import com.urosjarc.topsongs.app.song.Emotion
import com.urosjarc.topsongs.app.song.Score
import com.urosjarc.topsongs.app.song.Song
import com.urosjarc.topsongs.app.song.SongService
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.util.Callback
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField


abstract class SongRatingUi : KoinComponent {

	@FXML
	lateinit var songL: Label

	@FXML
	lateinit var saveB: Button

	@FXML
	lateinit var songLV: ListView<Song>

	@FXML
	lateinit var backgroundGP: GridPane
}

class SongRating : SongRatingUi() {
	val songService by this.inject<SongService>()

	fun initBackgroundNode(node: Node) {
		node.maxWidth(Double.MAX_VALUE)
		node.prefWidth(Double.MAX_VALUE)
		node.minWidth(ComboBox.USE_COMPUTED_SIZE)
		HBox.setHgrow(node, Priority.ALWAYS)
		GridPane.setHgrow(node, Priority.ALWAYS)
		when(node) {
			is ComboBox<*> -> {
				node.cellFactory = Callback<ListView<Any>, ListCell<Any>> {
					object : ListCell<Any>() {
						override fun updateItem(item: Any?, empty: Boolean) {
							super.updateItem(item, empty)
							text = when (item) {
								is String -> item
								is Enum<*> -> item.name
								else -> ""
							} + " ".repeat(50)
						}
					}
				}
			}
			is Label -> {
				node.minWidth = 100.0
			}
		}
	}

	@FXML
	fun initialize() {
		var row = 0

		val emotions = Emotion.Type.values().map { it.name.split("_").first() }.toMutableSet()
		Song::class.declaredMemberProperties
			.filter { it.javaField?.type?.name?.endsWith("Score") ?: false }
			.forEach { prop ->
				val nameL = Label(prop.name).also { this.initBackgroundNode(it) }
				val rateCB = ComboBox<Enum<*>>().also { this.initBackgroundNode(it) }
				val intensityCB = ComboBox<Emotion.Intensity>().also { this.initBackgroundNode(it) }
				val emotionGroupCB = ComboBox<String>().also { this.initBackgroundNode(it) }
				val emotionTypeCB = ComboBox<String>().also { this.initBackgroundNode(it) }

				rateCB.items.setAll(Score.Rating.values().toMutableSet())
				intensityCB.items.setAll(Emotion.Intensity.values().toMutableSet())
				emotionGroupCB.items.setAll(emotions)
				emotionGroupCB.setOnAction { _ ->
					emotionTypeCB.items.clear()
					val selectedGroup = emotionGroupCB.selectionModel.selectedItem
					val selectedEmotions = this.songService.getEmotions(emotionGroup = selectedGroup)
					emotionTypeCB.items.setAll(selectedEmotions)
				}

				this.backgroundGP.add(nameL, 0, row)
				this.backgroundGP.add(rateCB, 1, row)
				this.backgroundGP.add(intensityCB, 2, row)
				this.backgroundGP.add(emotionGroupCB, 3, row)
				this.backgroundGP.add(emotionTypeCB, 4, row)

				row++
			}
	}
}
