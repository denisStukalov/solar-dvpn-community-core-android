package co.sentinel.dvpn.domain.features.hub.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

open class Coin : Parcelable {
    @SerializedName("denom")
    var denom: String? = null

    @SerializedName("amount")
    var amount: String? = null

    constructor(denom: String, amount: String) {
        this.denom = denom
        this.amount = amount
    }

    protected constructor(`in`: Parcel) {
        readFromParcel(`in`)
    }

    private fun readFromParcel(`in`: Parcel) {
        denom = `in`.readString()
        amount = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(denom)
        dest.writeString(amount)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Coin> = object : Parcelable.Creator<Coin> {
            override fun createFromParcel(`in`: Parcel): Coin {
                return Coin(`in`)
            }

            override fun newArray(size: Int): Array<Coin?> {
                return arrayOfNulls(size)
            }
        }
    }
}
