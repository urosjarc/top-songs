package com.urosjarc.topsongs.app.song

data class SongNode(
	val name: String,
	val song: Song?,
	var parent: SongNode?,
	val root: Boolean = false,
	val children: MutableList<SongNode> = mutableListOf(),
) {
	fun connect(name: String, song: Song?): SongNode {
		val child = SongNode(name = name, song = song, parent = null)
		this.children.add(child)
		child.parent = this
		return child
	}

	fun folderPath(): String {
		val folders = mutableListOf<String>()
		var node = this
		while(!node.root){
			folders.add(node.name)
			node = node.parent ?: break
		}
		return folders
			.reversed()
			.joinToString("/")
	}

	override fun toString(): String {
		return "${this.name} ${this.song} ${this.parent}"
	}

	override fun hashCode(): Int {
		return this.toString().hashCode()
	}

	override fun equals(other: Any?): Boolean {
		return this.hashCode() == other.hashCode()
	}
}
