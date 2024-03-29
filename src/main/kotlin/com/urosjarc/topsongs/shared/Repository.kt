package com.urosjarc.topsongs.shared

import javafx.application.Platform
import org.koin.core.component.KoinComponent

abstract class Repository<T : Any> : KoinComponent {
	val data = mutableListOf<T>()
	var selected = mutableListOf<T>()
	var history = mutableListOf<T>()
	var future = mutableListOf<T>()
	var chosen: T? = null

	private val onDataCb = mutableListOf<Watcher<List<T>>>()
	private val onSelectCb = mutableListOf<Watcher<List<T>>>()
	private val onChoseCb = mutableListOf<Watcher<T>>()
	private val onErrorCb = mutableListOf<Watcher<String>>()

	class Watcher<CT>(
		val cb: (t: CT) -> Unit,
		val runLater: Boolean
	) {
		fun run(t: CT) = if (this.runLater) Platform.runLater { this.cb(t) } else this.cb(t)
	}

	fun onData(runLater: Boolean = false, cb: (t: List<T>) -> Unit) {
		this.onDataCb.add(Watcher(cb = cb, runLater = runLater))
	}

	fun onDataNotify() {
		this.onDataCb.forEach { it.run(this.data) }
	}

	fun onSelect(runLater: Boolean = false, cb: (t: List<T>) -> Unit) {
		this.onSelectCb.add(Watcher(cb = cb, runLater = runLater))
	}

	fun onSelectNotify() {
		this.onSelectCb.forEach { it.run(this.selected) }
	}

	fun onChose(runLater: Boolean = false, cb: (t: T) -> Unit) {
		this.onChoseCb.add(Watcher(cb = cb, runLater = runLater))
	}

	fun onChoseNotify() {
		val chosen = this.chosen
		if (chosen != null) this.onChoseCb.forEach { it.run(chosen) }
	}

	fun onError(runLater: Boolean = false, cb: (t: String) -> Unit) {
		this.onErrorCb.add(Watcher(cb = cb, runLater = runLater))
	}

	fun error(msg: String) {
		this.onErrorCb.forEach { it.run(msg) }
	}

	fun set(t: List<T>) {
		this.data.clear()
		this.data.addAll(t)
		this.resetHistory(all = true)
		this.onDataNotify()
		this.save()
	}

	open fun save(t: T): T {
		val old = this.data.firstOrNull { t == it }
		if (old == null) this.data.add(t) else return old
		this.onDataNotify()
		this.save()
		return t
	}

	fun select(t: List<T>) {
		this.selected = t.toMutableList()
		this.onSelectNotify()
	}

	fun chose(t: T) {
		this.resetHistory(all = false)
		this.chosen?.let { this.history.add(it) }
		this.chosen = t
		this.onChoseNotify()
	}

	fun find(t: T): T? {
		return this.data.firstOrNull { it == t }
	}

	fun delete(t: T) {
		this.resetHistory(all = false)
		this.history.removeAll { it == t }
		this.future.removeAll { it == t }

		if (this.data.removeAll { it == t }) {
			this.onDataNotify()
			this.save()
		}
		if (this.selected.removeAll { it == t }) {
			this.onSelectNotify()
		}
		if (this.chosen == t) {
			this.chosen = null
			this.onChoseNotify()
		}
	}

	private fun resetHistory(all: Boolean) {
		if (all) this.history.clear()
		else this.history += this.future
		this.future.clear()
	}

	// data: [1,2,3,4,5,6]
	// history, chosen, future: [1,2,3], 4, [5,6]
	fun undo() {
		this.history.removeLastOrNull()?.let { newChosen ->
			this.chosen?.let { oldChosen -> this.future.add(0, oldChosen) }
			this.chosen = newChosen
			this.onChoseNotify()
		}
	}

	fun redo() {
		this.future.removeFirstOrNull()?.let { newChosen ->
			this.chosen?.let { oldChosen -> this.history.add(oldChosen) }
			this.chosen = newChosen
			this.onChoseNotify()
		}
	}

	open fun load() {}

	open fun save() {}
}
