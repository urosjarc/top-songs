package com.urosjarc.topsongs.app.song

class SongService(
	val songRepo: SongRepo,
) {

	fun songFolderTree(): SongNode {
		val queue = mutableListOf<Song>()
		queue.addAll(this.songRepo.data.filter { it.rated })

		//Create folder structure
		val root = SongNode(name = "Folders", song = null, parent = null)
		while (queue.isNotEmpty()) {
			val current = queue.removeFirst()

			val folders = current.folder?.split('/')?.toMutableList() ?: mutableListOf()

			var currentFolderSongNode: SongNode = root
			while (folders.isNotEmpty()) {
				val currentFolder = folders.removeFirst()
				currentFolderSongNode = currentFolderSongNode.children.find { it.name == currentFolder } ?: currentFolderSongNode.connect(name = currentFolder, song = null)
			}

			currentFolderSongNode.connect(name = current.name, song = current)
		}

		return root
	}

	fun songEmotionTree(): SongNode {
		val queue = mutableListOf<Song>()
		queue.addAll(this.songRepo.data.filter { it.rated })

		//Create folder structure
		val root = SongNode(name = "Emotions", song = null, parent = null)
		while (queue.isNotEmpty()) {
			val current = queue.removeFirst()
			if (current.emotion == null && current.style == null) continue

			val folderNode = root.children.find {
				it.name == current.emotion.toString()
			} ?: root.connect(name = current.emotion.toString(), song = null)

			val styleNode = folderNode.children.find {
				it.name == current.style.toString()
			} ?: folderNode.connect(name = current.style.toString(), song = null)

			styleNode.connect(name = current.name, song = current)
		}

		return root
	}
}
