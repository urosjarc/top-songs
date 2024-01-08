package com.urosjarc.topsongs.gui.widgets

import com.urosjarc.topsongs.app.radio.Radio
import com.urosjarc.topsongs.app.radio.RadioRepo
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class RadioListUi : KoinComponent {

	@FXML
	lateinit var nameTF: TextField

	@FXML
	lateinit var urlTF: TextField

	@FXML
	lateinit var connectB: Button

	@FXML
	lateinit var radioLV: ListView<Radio>
}

class RadioList : RadioListUi() {
	val radioRepo by this.inject<RadioRepo>()

	@FXML
	fun initialize() {
		this.radioLV.items.setAll(this.radioRepo.data)
		this.radioRepo.onData { this.radioLV.items.setAll(this.radioRepo.data) }
		this.connectB.setOnAction { this.connect() }
		this.radioLV.setOnMouseClicked { this.select(it) }
	}

	fun select(mouseEvent: MouseEvent) {
		if (mouseEvent.clickCount == 2) {
			this.connect()
			return
		}
		var radio: Radio = this.radioLV.selectionModel.selectedItem ?: return
		radio = this.radioRepo.find(radio) ?: radio
		this.nameTF.text = radio.name
		this.urlTF.text = radio.url
	}

	fun connect() {
		val radio = Radio(
			name = nameTF.text,
			url = urlTF.text
		)
		radioRepo.save(radio)
		radioRepo.chose(radio)
	}
}
