package co.sentinel.solar.core.model

import com.google.gson.annotations.SerializedName

data class CountryData(
    @SerializedName("alpha2")
    val alpha2: String, // country code
    @SerializedName("capital")
    val capital: String,// code
    @SerializedName("area")
    val area: String,
    @SerializedName("population")
    val population: String,
    @SerializedName("continent")
    val continent: String
)