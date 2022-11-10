package co.sentinel.cosmos.dao;

import java.util.ArrayList;
import java.util.UUID;


public class Account {
    public Long id;
    public String uuid;
    public String nickName;
    public Boolean isFavo;
    public String address;
    public String baseChain;

    public Boolean hasPrivateKey;
    public String resource;
    public String spec;
    public Boolean fromMnemonic;
    public String path;

    public Boolean isValidator;
    public Integer sequenceNumber;
    public Integer accountNumber;
    public Long fetchTime;
    public int msize;
    public Long importTime;

    public String lastTotal;
    public Long sortOrder;
    public Boolean pushAlarm;
    public Boolean newBip44;

    public ArrayList<Balance> balances;


    public static Account getNewInstance() {
        Account result = new Account();
        result.uuid = UUID.randomUUID().toString();
        return result;
    }

    public Account() {
    }

    public Account(Long id, String uuid, String nickName, boolean isFavo, String address,
                   String baseChain, boolean hasPrivateKey, String resource, String spec,
                   boolean fromMnemonic, String path, boolean isValidator, int sequenceNumber,
                   int accountNumber, Long fetchTime, int msize, long importTime, String lastTotal, long sortOrder, boolean pushAlarm, boolean newBip) {
        this.id = id;
        this.uuid = uuid;
        this.nickName = nickName;
        this.isFavo = isFavo;
        this.address = address;
        this.baseChain = baseChain;
        this.hasPrivateKey = hasPrivateKey;
        this.resource = resource;
        this.spec = spec;
        this.fromMnemonic = fromMnemonic;
        this.path = path;
        this.isValidator = isValidator;
        this.sequenceNumber = sequenceNumber;
        this.accountNumber = accountNumber;
        this.fetchTime = fetchTime;
        this.msize = msize;
        this.importTime = importTime;
        this.lastTotal = lastTotal;
        this.sortOrder = sortOrder;
        this.pushAlarm = pushAlarm;
        this.newBip44 = newBip;
    }

    public ArrayList<Balance> getBalances() {
        return balances;
    }

    public void setBalances(ArrayList<Balance> balances) {
        this.balances = balances;
    }

}
