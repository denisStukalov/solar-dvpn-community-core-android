package co.sentinel.cosmos.model.type;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AccountBalance implements Parcelable {

    @SerializedName("balances")
    public ArrayList<Coin> balances;

    @SerializedName("fetchTime")
    public Long fetchTime;

    public AccountBalance() {

    }

    public AccountBalance(ArrayList<Coin> balances, Long fetchTime) {
        this.balances = balances;
        this.fetchTime = fetchTime;
    }

    protected AccountBalance(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        balances = new ArrayList<>();
        in.readList(balances, Coin.class.getClassLoader());
        fetchTime = in.readLong();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(balances);
        dest.writeLong(fetchTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AccountBalance> CREATOR = new Creator<AccountBalance>() {
        @Override
        public AccountBalance createFromParcel(Parcel in) {
            return new AccountBalance(in);
        }

        @Override
        public AccountBalance[] newArray(int size) {
            return new AccountBalance[size];
        }
    };
}
