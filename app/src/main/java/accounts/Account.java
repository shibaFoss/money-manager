package accounts;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Account implements Serializable {

    @SerializedName("accountName")
    public String accountName;

    @SerializedName("currency")
    public String currency;

    @SerializedName("availableBalance")
    public double availableBalance;

    @SerializedName("note")
    public String note;

}
