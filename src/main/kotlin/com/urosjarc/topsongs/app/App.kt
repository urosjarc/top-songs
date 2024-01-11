package com.urosjarc.topsongs.app

import com.urosjarc.topsongs.app.log.LogService
import com.urosjarc.topsongs.app.radio.LogRepo
import com.urosjarc.topsongs.app.radio.RadioRepo
import com.urosjarc.topsongs.app.song.SongRepo
import com.urosjarc.topsongs.app.song.SongService
import com.urosjarc.topsongs.app.stream.StreamRepo
import com.urosjarc.topsongs.app.stream.StreamService
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

object App {
	fun modul() = module {
		this.single<SongRepo> { SongRepo("songs.json") }
		this.single<RadioRepo> { RadioRepo("radios.json") }
		this.single<StreamRepo> { StreamRepo() }
		this.single<LogRepo> { LogRepo() }
		this.factory<StreamService> { StreamService() }
		this.factory<SongService> { SongService() }
		this.single { LogService(get()) }
	}

	fun init() {
		startKoin { this.modules(modul()) }
	}

	fun reset() = stopKoin()
}
