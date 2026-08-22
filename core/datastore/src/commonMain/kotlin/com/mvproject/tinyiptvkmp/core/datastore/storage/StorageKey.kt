package com.mvproject.tinyiptvkmp.core.datastore.storage

interface StorageKey<T : Any> {
    val name: String
    val defaultValue: T
}

data class DefaultStorageKey<T : Any>(
    override val name: String,
    override val defaultValue: T,
) : StorageKey<T>

fun <T : Any> storageKey(
    name: String,
    defaultValue: T,
): StorageKey<T> = DefaultStorageKey(name, defaultValue)
