package com.urosjarc.topsongs.gui.windows

import com.urosjarc.topsongs.gui.widgets.RadioList
import com.urosjarc.topsongs.gui.widgets.SongList
import javafx.fxml.FXML
import org.koin.core.component.KoinComponent

abstract class AacRadioUi : KoinComponent {
	@FXML
	lateinit var songListController: SongList
	@FXML
	lateinit var radioListController: RadioList
}

class AacRadio : AacRadioUi()
