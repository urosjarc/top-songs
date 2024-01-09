package com.urosjarc.topsongs.app.radio

import kotlinx.serialization.Serializable

@Serializable
data class Radio(
	val name: String,
	val url: String
) {
	override fun hashCode(): Int {
		return this.url.hashCode()
	}
	override fun equals(other: Any?): Boolean {
		return this.hashCode() == other.hashCode()
	}
	override fun toString(): String {
		return "$name | $url"
	}
}
