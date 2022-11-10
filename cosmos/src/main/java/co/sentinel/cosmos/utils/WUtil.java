package co.sentinel.cosmos.utils;

import static co.sentinel.cosmos.base.BaseConstant.YEAR_SEC;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

import co.sentinel.cosmos.base.BaseChain;
import cosmos.base.v1beta1.CoinOuterClass;

public class WUtil {

    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] integerToBytes(BigInteger s, int length) {
        byte[] bytes = s.toByteArray();

        if (length < bytes.length) {
            byte[] tmp = new byte[length];
            System.arraycopy(bytes, bytes.length - tmp.length, tmp, 0, tmp.length);
            return tmp;
        } else if (length > bytes.length) {
            byte[] tmp = new byte[length];
            System.arraycopy(bytes, 0, tmp, tmp.length - bytes.length, bytes.length);
            return tmp;
        }
        return bytes;
    }

    public static BigDecimal decCoinAmount(List<CoinOuterClass.DecCoin> coins, String denom) {
        BigDecimal result = BigDecimal.ZERO;
        for (CoinOuterClass.DecCoin coin : coins) {
            if (coin.getDenom().equals(denom)) {
                return new BigDecimal(coin.getAmount()).movePointLeft(18).setScale(0, RoundingMode.DOWN);
            }
        }
        return result;
    }

    public static BigDecimal getRealBlockTime(BaseChain chain) {
        return BigDecimal.ZERO;
    }

    public static BigDecimal getRealBlockPerYear(BaseChain chain) {
        if (getRealBlockTime(chain) == BigDecimal.ZERO) {
            return BigDecimal.ZERO;
        }
        return YEAR_SEC.divide(getRealBlockTime(chain), 2, RoundingMode.DOWN);
    }

    public static byte[] HexStringToByteArray(String s) throws IllegalArgumentException {
        int len = s.length();
        if (len % 2 == 1) {
            throw new IllegalArgumentException("Hex string must have even number of characters");
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * token price
     */
    public static String marketPrice(BaseChain basechain) {
        return "usdt" + "," + WDp.mainDenom(basechain);
    }
}
