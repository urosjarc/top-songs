package com.urosjarc.topsongs.gui.windows

import com.urosjarc.topsongs.gui.widgets.RadioList
import com.urosjarc.topsongs.gui.widgets.SongRater
import javafx.fxml.FXML
import org.koin.core.component.KoinComponent

abstract class AacRadioUi : KoinComponent {
	@FXML
	lateinit var songRaterController: SongRater
	@FXML
	lateinit var radioListController: RadioList
}

class AacRadio : AacRadioUi()
