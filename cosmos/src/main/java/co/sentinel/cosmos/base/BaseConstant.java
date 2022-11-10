package co.sentinel.cosmos.base;

import java.math.BigDecimal;

public class BaseConstant {
    public final static boolean IS_SHOTimber              = false;

    public final static String DB_NAME                  = "WannaBit";
    public final static int DB_VERSION                  = 5;
    public final static String DB_TABLE_PASSWORD        = "paswd";
    public final static String DB_TABLE_ACCOUNT         = "accnt";
    public final static String DB_TABLE_BALANCE         = "balan";
    public final static String DB_TABLE_BONDING         = "bondi";
    public final static String DB_TABLE_UNBONDING       = "unbond";
    public final static String DB_TABLE_PRICE           = "price";

    public final static String PRE_USER_ID                  = "PRE_USER_ID";
    public final static String PRE_SELECTED_CHAIN           = "PRE_SELECTED_CHAIN";
    public final static String PRE_CURRENCY                 = "PRE_CURRENCY";


    public final static int TASK_INIT_PW                                = 2000;
    public final static int TASK_INIT_ACCOUNT                           = 2002;
    public final static int TASK_INIT_EMPTY_ACCOUNT                     = 2003;
    public final static int TASK_PASSWORD_CHECK                         = 2015;
    public final static int TASK_OVERRIDE_ACCOUNT                       = 2019;
    public final static int TASK_DELETE_USER                            = 2024;
    public final static int TASK_CHECK_MNEMONIC                         = 2025;
    //gRPC
    public final static int TASK_GRPC_FETCH_BALANCE                     = 4001;
    public final static int TASK_GRPC_FETCH_BONDED_VALIDATORS           = 4002;
    public final static int TASK_GRPC_FETCH_UNBONDED_VALIDATORS         = 4003;
    public final static int TASK_GRPC_FETCH_UNBONDING_VALIDATORS        = 4004;
    public final static int TASK_GRPC_FETCH_DELEGATIONS                 = 4005;
    public final static int TASK_GRPC_FETCH_UNDELEGATIONS               = 4006;
    public final static int TASK_GRPC_FETCH_ALL_REWARDS                 = 4007;
    public final static int TASK_GRPC_FETCH_NODE_INFO                   = 4024;
    public final static int TASK_GRPC_FETCH_AUTH                        = 4025;
    public final static int TASK_GRPC_BROAD_SEND                        = 4303;



    public final static int ERROR_CODE_UNKNOWN              = 8000;
    public final static int ERROR_CODE_NETWORK              = 8001;
    public final static int ERROR_CODE_INVALID_PASSWORD     = 8002;
    public final static int ERROR_CODE_TIMEOUT              = 8003;
    public final static int ERROR_CODE_BROADCAST            = 8004;

    public final static String DENOM_DVPN           = "udvpn";

    public final static String KEY_PATH             = "44'/118'/0'/0/";

    public final static long CONSTANT_S = 1000l;
    public final static long CONSTANT_10S = CONSTANT_S * 10;
    public final static long CONSTANT_30S = CONSTANT_S * 30;
    public final static long CONSTANT_M = CONSTANT_S * 60;
    public final static long CONSTANT_H = CONSTANT_M * 60;
    public final static long CONSTANT_D = CONSTANT_H * 24;



    public final static BigDecimal DAY_SEC = new BigDecimal("86400");
    public final static BigDecimal MONTH_SEC = DAY_SEC.multiply(new BigDecimal("30"));
    public final static BigDecimal YEAR_SEC = DAY_SEC.multiply(new BigDecimal("365"));


    // SOLAR ADDED ERRORS
    public final static int ERROR_INSUFFICIENT_FUNDS = 5;
}
