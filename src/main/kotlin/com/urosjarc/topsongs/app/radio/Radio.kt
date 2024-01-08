package com.urosjarc.topsongs.app.radio

import kotlinx.serialization.Serializable

@Serializable
data class Radio(
	val name: String,
	val url: String
)
