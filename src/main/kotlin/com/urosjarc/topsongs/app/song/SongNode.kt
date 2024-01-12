package com.urosjarc.topsongs.app.song

data class SongNode(
	val name: String,
	val song: Song?,
	var parent: SongNode?,
	val children: MutableList<SongNode> = mutableListOf(),
) {
	fun connect(name: String, song: Song?): SongNode {
		val child = SongNode(name = name, song = song, parent = null)
		this.children.add(child)
		child.parent = this
		return child
	}
}
