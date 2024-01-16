package com.urosjarc.topsongs.app.song

import java.awt.Desktop
import java.net.URI
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class SongService(
	val songRepo: SongRepo,
) {

	fun songFoldersTree(): SongNode {
		val queue = mutableListOf<Song>()
		queue.addAll(this.songRepo.data.filter { it.rated })

		//Create folder structure
		val root = SongNode(name = "Folders", song = null, parent = null, root = true)
		while (queue.isNotEmpty()) {
			val current = queue.removeFirst()

			val folders = current.folder?.split('/')?.toMutableList() ?: mutableListOf()

			var currentFolderSongNode: SongNode = root
			while (folders.isNotEmpty()) {
				val currentFolder = folders.removeFirst()
				currentFolderSongNode = currentFolderSongNode.children.find { it.name == currentFolder } ?: currentFolderSongNode.connect(
					name = currentFolder,
					song = null
				)
			}

			currentFolderSongNode.connect(name = current.name, song = current)
		}

		return root
	}

	fun songFeelingsTree(): SongNode {
		val queue = mutableListOf<Song>()
		queue.addAll(this.songRepo.data.filter { it.rated })

		//Create folder structure
		val root = SongNode(name = "Feelings", song = null, parent = null, root = true)
		while (queue.isNotEmpty()) {
			val current = queue.removeFirst()
			if (current.feeling == null && current.style == null) continue

			val folderNode = root.children.find {
				it.name == current.feeling.toString()
			} ?: root.connect(name = current.feeling.toString(), song = null)

			val styleNode = folderNode.children.find {
				it.name == current.style.toString()
			} ?: folderNode.connect(name = current.style.toString(), song = null)

			styleNode.connect(name = current.name, song = current)
		}

		return root
	}

	fun youtubeId(link: String): String {
		try {
			val url = URL(link).toURI()
			url.query.split("&").forEach {
				val (name, value) = it.split("=")
				if(name == "v") return value
			}
		} catch (_: Exception) {
		}
		return link
	}

	fun youtubeSearch(query: String)  {
		val encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8)
		return this.browse("results?search_query=$encodedQuery")
	}
	fun youtubePlay(id: String) = this.browse("watch?v=$id")

	private fun browse(path: String) {
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			Desktop.getDesktop().browse(URI("https://www.youtube.com/$path"));
		}
	}
}
