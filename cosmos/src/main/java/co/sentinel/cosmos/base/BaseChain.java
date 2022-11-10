package co.sentinel.cosmos.base;

import java.util.ArrayList;

public enum BaseChain {
    SENTINEL_MAIN("sentinel-mainnet");

    private final String chainName;

    BaseChain(final String chainname) {
        this.chainName = chainname;
    }

    public String getChain() {
        return chainName;
    }

    public static BaseChain getChain(String chainName) {
        return SENTINEL_MAIN;
    }

    public static ArrayList<BaseChain> SUPPORT_CHAINS() {
        ArrayList<BaseChain> result = new ArrayList<>();
        result.add(SENTINEL_MAIN);
        return result;
    }

    public static boolean IS_SUPPORT_CHAIN(String chain) {
        return SUPPORT_CHAINS().contains(getChain(chain));
    }

    public static boolean isGRPC(BaseChain baseChain) {
        return true;
    }
}
