package com.ntt.kchallenge.data.model

data class Country(val id: String, val name: String) {
    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Country) {
            return other.id == id && other.name == name
        } else false
    }
}