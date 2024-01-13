package com.urosjarc.topsongs.app.log

import com.urosjarc.topsongs.shared.Repository

class LogRepo : Repository<Log>() {
	override fun save(t: Log): Log {
		super.save(t)
		this.chose(t)
		return t
	}
}
