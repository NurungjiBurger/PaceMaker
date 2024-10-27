package com.maker.pacemaker.data.model

data class User(
    val name: String,
    val level: Int,
    val followers: Int,
    val isFollowing: Boolean
)