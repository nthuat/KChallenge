package com.ntt.kchallenge.data.model

import com.google.gson.annotations.SerializedName

data class Countries(
    @SerializedName("countries")
    val countries: List<Country>
)

data class Country(
    @SerializedName("name")
    val name: String
) {
    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Country) {
            return other.name == name
        } else false
    }
}