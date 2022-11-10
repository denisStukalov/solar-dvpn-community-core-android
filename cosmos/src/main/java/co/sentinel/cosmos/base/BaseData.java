package co.sentinel.cosmos.base;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.protobuf2.Any;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import co.sentinel.cosmos.R;
import co.sentinel.cosmos.dao.Account;
import co.sentinel.cosmos.dao.Balance;
import co.sentinel.cosmos.dao.Password;
import co.sentinel.cosmos.dao.Price;
import co.sentinel.cosmos.model.NodeInfo;
import co.sentinel.cosmos.model.type.Coin;
import co.sentinel.cosmos.utils.WUtil;
import cosmos.base.v1beta1.CoinOuterClass;
import cosmos.distribution.v1beta1.Distribution;
import cosmos.staking.v1beta1.Staking;
import tendermint.p2p.Types;

public class BaseData {

    final private Context mApp;
    private SharedPreferences mSharedPreferences;
    private SQLiteDatabase mSQLiteDatabase;
    public String mCopySalt;

    public BaseData(Context apps) {
        this.mApp = apps;
        this.mSharedPreferences = getSharedPreferences();
        SQLiteDatabase.loadLibs(mApp);
    }

    private SharedPreferences getSharedPreferences() {
        if (mSharedPreferences == null)
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mApp);
        return mSharedPreferences;
    }

    public SQLiteDatabase getBaseDB() {
        if (mSQLiteDatabase == null) {
            mSQLiteDatabase = BaseDB.getInstance(mApp).getWritableDatabase(mApp.getString(R.string.db_password));
        }
        return mSQLiteDatabase;
    }

    public void clearDB() {
        if (mSQLiteDatabase != null) {
            clearTables(mSQLiteDatabase);
        }
    }

    public void clearTables(SQLiteDatabase db) {
        db.delete(BaseConstant.DB_TABLE_PASSWORD, null, null);
        db.delete(BaseConstant.DB_TABLE_ACCOUNT, null, null);
        db.delete(BaseConstant.DB_TABLE_BALANCE, null, null);
        db.delete(BaseConstant.DB_TABLE_BONDING, null, null);
        db.delete(BaseConstant.DB_TABLE_UNBONDING, null, null);
    }

    public ArrayList<Price> mPrices = new ArrayList<>();

    public Price getPrice(String denom) {
        for (Price price : mPrices) {
            if (price.denom.equals(denom.toLowerCase())) {
                return price;
            }
        }
        return null;
    }


    //COMMON DATA
    public NodeInfo mNodeInfo;

    public String getChainId() {
        if (mNodeInfo != null) {
            return mNodeInfo.network;
        }
        return "";
    }


    //gRPC
    public Types.DefaultNodeInfo mGRpcNodeInfo;
    public Any mGRpcAccount;
    public ArrayList<Staking.Validator> mGRpcTopValidators = new ArrayList<>();
    public ArrayList<Staking.Validator> mGRpcUnbondedValidators = new ArrayList<>();
    public ArrayList<Staking.Validator> mGRpcUnbondingValidators = new ArrayList<>();

    public ArrayList<Coin> mGrpcBalance = new ArrayList<>();
    public ArrayList<Coin> mGrpcVesting = new ArrayList<>();
    public ArrayList<Staking.DelegationResponse> mGrpcDelegations = new ArrayList<>();
    public ArrayList<Staking.UnbondingDelegation> mGrpcUndelegations = new ArrayList<>();
    public ArrayList<Distribution.DelegationDelegatorReward> mGrpcRewards = new ArrayList<>();


    //gRPC funcs
    public String getChainIdGrpc() {
        if (mGRpcNodeInfo != null) {
            return mGRpcNodeInfo.getNetwork();
        }
        return "";
    }


    public BigDecimal getAvailable(String denom) {
        BigDecimal result = BigDecimal.ZERO;
        for (Coin coin : mGrpcBalance) {
            if (coin.denom.equalsIgnoreCase(denom)) {
                result = new BigDecimal(coin.amount);
            }
        }
        return result;
    }

    public BigDecimal getVesting(String denom) {
        BigDecimal result = BigDecimal.ZERO;
        for (Coin coin : mGrpcVesting) {
            if (coin.denom.equalsIgnoreCase(denom)) {
                result = new BigDecimal(coin.amount);
            }
        }
        return result;
    }


    public BigDecimal getDelegatable(String denom) {
        return getAvailable(denom).add(getVesting(denom));
    }


    public BigDecimal getDelegation(String valOpAddress) {
        BigDecimal result = BigDecimal.ZERO;
        for (Staking.DelegationResponse delegation : mGrpcDelegations) {
            if (delegation.getDelegation().getValidatorAddress().equals(valOpAddress)) {
                result = new BigDecimal(delegation.getBalance().getAmount());
            }
        }
        return result;
    }

    public BigDecimal getUndelegation(String valOpAddress) {
        BigDecimal result = BigDecimal.ZERO;
        for (Staking.UnbondingDelegation undelegation : mGrpcUndelegations) {
            if (undelegation.getValidatorAddress().equals(valOpAddress)) {
                result = getAllUnbondingBalance(undelegation);
            }
        }
        return result;
    }


    public BigDecimal getAllUnbondingBalance(Staking.UnbondingDelegation undelegation) {
        BigDecimal result = BigDecimal.ZERO;
        if (undelegation != null && undelegation.getEntriesList().size() > 0) {
            for (Staking.UnbondingDelegationEntry entry : undelegation.getEntriesList()) {
                result = result.add(new BigDecimal(entry.getBalance()));
            }
        }
        return result;
    }

    public BigDecimal getReward(String denom, String valOpAddress) {
        BigDecimal result = BigDecimal.ZERO;
        for (Distribution.DelegationDelegatorReward reward : mGrpcRewards) {
            if (reward.getValidatorAddress().equals(valOpAddress)) {
                result = WUtil.decCoinAmount(reward.getRewardList(), denom);
            }
        }
        return result;
    }

    public CoinOuterClass.DecCoin decCoin(List<CoinOuterClass.DecCoin> coins, String denom) {
        for (CoinOuterClass.DecCoin coin : coins) {
            if (coin.getDenom().equals(denom)) {
                return coin;
            }
        }
        return null;
    }

    public void setLastUser(long user) {
        getSharedPreferences().edit().putLong(BaseConstant.PRE_USER_ID, user).commit();
    }

    public String getLastUser() {
        long result = -1;
        if (getSharedPreferences().getLong(BaseConstant.PRE_USER_ID, -1) != result) {
            result = getSharedPreferences().getLong(BaseConstant.PRE_USER_ID, -1);
        } else {
            if (onSelectAccounts().size() > 0) {
                result = onSelectAccounts().get(0).accountNumber;
            }
        }
        return "" + result;
    }

    public boolean hasUser() {
        long result = -1;
        if (getSharedPreferences().getLong(BaseConstant.PRE_USER_ID, -1) != result) {
            result = getSharedPreferences().getLong(BaseConstant.PRE_USER_ID, -1);
        } else {
            if (onSelectAccounts().size() > 0) {
                result = onSelectAccounts().get(0).accountNumber;
            }
        }
        return result != -1;
    }

    public int getLastChain() {
        int position = getSharedPreferences().getInt(BaseConstant.PRE_SELECTED_CHAIN, 0);
        if (BaseChain.SUPPORT_CHAINS().size() < position) {
            return 0;
        } else {
            return position;
        }
    }

    public void setLastChain(int position) {
        getSharedPreferences().edit().putInt(BaseConstant.PRE_SELECTED_CHAIN, position).commit();
    }

    public Password onSelectPassword() {
        Password result = null;
        Cursor cursor = getBaseDB().query(BaseConstant.DB_TABLE_PASSWORD, new String[]{"resource", "spec"}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            result = new Password(cursor.getString(0), cursor.getString(1));
        }
        cursor.close();
        return result;
    }

    public boolean onHasPassword() {
        boolean existed = false;
        Cursor cursor = getBaseDB().query(BaseConstant.DB_TABLE_PASSWORD, new String[]{"resource", "spec"}, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            existed = true;
        }
        cursor.close();
        return existed;
    }

    public long onInsertPassword(Password password) {
        long result = -1;
        if (onHasPassword()) return result;

        ContentValues values = new ContentValues();
        values.put("resource", password.resource);
        values.put("spec", password.spec);
        return getBaseDB().insertOrThrow(BaseConstant.DB_TABLE_PASSWORD, null, values);
    }

    public ArrayList<Account> onSelectAccounts() {
        ArrayList<Account> result = new ArrayList<>();
        Cursor cursor = getBaseDB().query(BaseConstant.DB_TABLE_ACCOUNT, new String[]{"id", "uuid", "nickName", "isFavo", "address", "baseChain",
                "hasPrivateKey", "resource", "spec", "fromMnemonic", "path",
                "isValidator", "sequenceNumber", "accountNumber", "fetchTime", "msize", "importTime", "lastTotal", "sortOrder", "pushAlarm", "newBip"}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Account account = new Account(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3) > 0,
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getInt(6) > 0,
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getInt(9) > 0,
                        cursor.getString(10),
                        cursor.getInt(11) > 0,
                        cursor.getInt(12),
                        cursor.getInt(13),
                        cursor.getLong(14),
                        cursor.getInt(15),
                        cursor.getLong(16),
                        cursor.getString(17),
                        cursor.getLong(18),
                        cursor.getInt(19) > 0,
                        cursor.getInt(20) > 0
                );
                account.setBalances(onSelectBalance(account.id));
                result.add(account);
            } while (cursor.moveToNext());
        }
        cursor.close();

        Iterator<Account> iterator = result.iterator();
        while (iterator.hasNext()) {
            Account account = iterator.next();
            if (!BaseChain.IS_SUPPORT_CHAIN(account.baseChain)) {
                iterator.remove();
            }
        }
        return result;
    }

    public Account onSelectAccount(String id) {
        Account result = null;
        Cursor cursor = getBaseDB().query(BaseConstant.DB_TABLE_ACCOUNT, new String[]{"id", "uuid", "nickName", "isFavo", "address", "baseChain",
                "hasPrivateKey", "resource", "spec", "fromMnemonic", "path",
                "isValidator", "sequenceNumber", "accountNumber", "fetchTime", "msize", "importTime", "lastTotal", "sortOrder", "pushAlarm", "newBip"}, "id == ?", new String[]{id}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            result = new Account(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3) > 0,
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getInt(6) > 0,
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getInt(9) > 0,
                    cursor.getString(10),
                    cursor.getInt(11) > 0,
                    cursor.getInt(12),
                    cursor.getInt(13),
                    cursor.getLong(14),
                    cursor.getInt(15),
                    cursor.getLong(16),
                    cursor.getString(17),
                    cursor.getLong(18),
                    cursor.getInt(19) > 0,
                    cursor.getInt(20) > 0
            );
            result.setBalances(onSelectBalance(result.id));
        }
        cursor.close();
        if (!BaseChain.IS_SUPPORT_CHAIN(result.baseChain)) {
            return onSelectAccounts().get(0);
        }
        return result;
    }

    public long onInsertAccount(Account account) {
        long result = -1;
        if (isDupleAccount(account.address, account.baseChain)) return result;
        ContentValues values = new ContentValues();
        values.put("uuid", account.uuid);
        values.put("nickName", account.nickName);
        values.put("isFavo", account.isFavo);
        values.put("address", account.address);
        values.put("baseChain", account.baseChain);
        values.put("hasPrivateKey", account.hasPrivateKey);
        values.put("resource", account.resource);
        values.put("spec", account.spec);
        values.put("fromMnemonic", account.fromMnemonic);
        values.put("path", account.path);
        values.put("isValidator", account.isValidator);
        values.put("sequenceNumber", account.sequenceNumber);
        values.put("accountNumber", account.accountNumber);
        values.put("fetchTime", account.fetchTime);
        values.put("msize", account.msize);
        values.put("importTime", account.importTime);
        values.put("sortOrder", 9999l);
        values.put("pushAlarm", account.pushAlarm);
        values.put("newBip", account.newBip44);
        return getBaseDB().insertOrThrow(BaseConstant.DB_TABLE_ACCOUNT, null, values);
    }

    public long onUpdateAccount(Account account) {
        ContentValues values = new ContentValues();
        if (!TextUtils.isEmpty(account.nickName))
            values.put("nickName", account.nickName);
        if (account.isFavo != null)
            values.put("isFavo", account.isFavo);
        if (account.sequenceNumber != null)
            values.put("sequenceNumber", account.sequenceNumber);
        if (account.accountNumber != null)
            values.put("accountNumber", account.accountNumber);
        if (account.fetchTime != null)
            values.put("fetchTime", account.fetchTime);
        if (account.baseChain != null)
            values.put("baseChain", account.baseChain);
        return getBaseDB().update(BaseConstant.DB_TABLE_ACCOUNT, values, "id = ?", new String[]{"" + account.id});
    }

    public long onOverrideAccount(Account account) {
        ContentValues values = new ContentValues();
        values.put("hasPrivateKey", account.hasPrivateKey);
        values.put("resource", account.resource);
        values.put("spec", account.spec);
        values.put("fromMnemonic", account.fromMnemonic);
        values.put("path", account.path);
        values.put("msize", account.msize);
        values.put("newBip", account.newBip44);
        return getBaseDB().update(BaseConstant.DB_TABLE_ACCOUNT, values, "id = ?", new String[]{"" + account.id});
    }


    public boolean isDupleAccount(String address, String chain) {
        boolean existed = false;
        Cursor cursor = getBaseDB().query(BaseConstant.DB_TABLE_ACCOUNT, new String[]{"id"}, "address == ? AND baseChain == ?", new String[]{address, chain}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            existed = true;
        }
        cursor.close();
        return existed;
    }

    public ArrayList<Balance> onSelectBalance(long accountId) {
        ArrayList<Balance> result = new ArrayList<>();
        Cursor cursor = getBaseDB().query(BaseConstant.DB_TABLE_BALANCE, new String[]{"accountId", "symbol", "balance", "fetchTime", "frozen", "locked"}, "accountId == ?", new String[]{"" + accountId}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Balance balance = new Balance(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getLong(3),
                        cursor.getString(4),
                        cursor.getString(5));
                result.add(balance);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public long onInsertBalance(Balance balance) {
        if (onHasBalance(balance)) {
            return onUpdateBalance(balance);
        } else {
            ContentValues values = new ContentValues();
            values.put("accountId", balance.accountId);
            values.put("symbol", balance.symbol);
            values.put("balance", balance.balance.toPlainString());
            values.put("fetchTime", balance.fetchTime);
            if (balance.frozen != null)
                values.put("frozen", balance.frozen.toPlainString());
            if (balance.locked != null)
                values.put("locked", balance.locked.toPlainString());
            return getBaseDB().insertOrThrow(BaseConstant.DB_TABLE_BALANCE, null, values);
        }
    }

    public long onUpdateBalance(Balance balance) {
        onDeleteBalance("" + balance.accountId);
        return onInsertBalance(balance);
    }

    public boolean onHasBalance(Balance balance) {
        boolean existed = false;
        Cursor cursor = getBaseDB().query(BaseConstant.DB_TABLE_BALANCE, new String[]{"accountId", "symbol", "balance", "fetchTime"}, "accountId == ? AND symbol == ? ", new String[]{"" + balance.accountId, balance.symbol}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            existed = true;
        }
        cursor.close();
        return existed;
    }

    public boolean onDeleteBalance(String accountId) {
        return getBaseDB().delete(BaseConstant.DB_TABLE_BALANCE, "accountId = ?", new String[]{accountId}) > 0;
    }

    public String getCurrencySymbol() {
        if (getCurrency() == 0) {
            return "$";
        } else if (getCurrency() == 1) {
            return "€";
        } else if (getCurrency() == 2) {
            return "₩";
        } else if (getCurrency() == 3) {
            return "¥";
        } else if (getCurrency() == 4) {
            return "¥";
        } else if (getCurrency() == 5) {
            return "₽";
        } else if (getCurrency() == 6) {
            return "£";
        } else if (getCurrency() == 7) {
            return "₹";
        } else if (getCurrency() == 8) {
            return "R$";
        } else if (getCurrency() == 9) {
            return "Rp";
        } else if (getCurrency() == 10) {
            return "Kr";
        } else if (getCurrency() == 11) {
            return "Kr";
        } else if (getCurrency() == 12) {
            return "Kr";
        } else if (getCurrency() == 13) {
            return "sFr";
        } else if (getCurrency() == 14) {
            return "AU$";
        }
        return "";
    }

    public int getCurrency() {
        return getSharedPreferences().getInt(BaseConstant.PRE_CURRENCY, 0);
    }

    public String getCurrencyString() {
        if (getCurrency() == 0) {
            return "USD";
        } else if (getCurrency() == 1) {
            return "EUR";
        } else if (getCurrency() == 2) {
            return "KRW";
        } else if (getCurrency() == 3) {
            return "JPY";
        } else if (getCurrency() == 4) {
            return "CNY";
        } else if (getCurrency() == 5) {
            return "RUB";
        } else if (getCurrency() == 6) {
            return "GBP";
        } else if (getCurrency() == 7) {
            return "INR";
        } else if (getCurrency() == 8) {
            return "BRL";
        } else if (getCurrency() == 9) {
            return "IDR";
        } else if (getCurrency() == 10) {
            return "DKK";
        } else if (getCurrency() == 11) {
            return "NOK";
        } else if (getCurrency() == 12) {
            return "SEK";
        } else if (getCurrency() == 13) {
            return "CHF";
        } else if (getCurrency() == 14) {
            return "AUD";
        }
        return "";
    }

}
