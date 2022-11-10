package co.sentinel.cosmos.utils;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;
import static co.sentinel.cosmos.base.BaseConstant.DENOM_DVPN;

import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import co.sentinel.cosmos.base.BaseChain;
import co.sentinel.cosmos.base.BaseConstant;
import co.sentinel.cosmos.base.BaseData;
import co.sentinel.cosmos.dao.Price;

public class WDp {

    public static SpannableString getDpString(String input, int point) {
        SpannableString result;
        result = new SpannableString(input);
        result.setSpan(new RelativeSizeSpan(0.8f), result.length() - point, result.length(), SPAN_INCLUSIVE_INCLUSIVE);
        return result;
    }

    public static String getPath(BaseChain chain, int position, boolean newBip) {
        return BaseConstant.KEY_PATH + String.valueOf(position);
    }


    public static DecimalFormat getDecimalFormat(int cnt) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat decimalformat = (DecimalFormat) formatter;
        decimalformat.setRoundingMode(RoundingMode.DOWN);
        switch (cnt) {
            case 0:
                decimalformat.applyLocalizedPattern("###,###,###,###,###,###,###,##0");
                break;
            case 1:
                decimalformat.applyLocalizedPattern("###,###,###,###,###,###,###,##0.0");
                break;
            case 2:
                decimalformat.applyLocalizedPattern("###,###,###,###,###,###,###,##0.00");
                break;
            case 3:
                decimalformat.applyLocalizedPattern("###,###,###,###,###,###,###,##0.000");
                break;
            case 4:
                decimalformat.applyLocalizedPattern("###,###,###,###,###,###,###,##0.0000");
                break;
            case 5:
                decimalformat.applyLocalizedPattern("###,###,###,###,###,###,###,##0.00000");
                break;
            case 6:
                decimalformat.applyLocalizedPattern("###,###,###,###,###,###,###,##0.000000");
                break;
            case 7:
                decimalformat.applyLocalizedPattern("###,###,###,###,###,###,###,##0.0000000");
                break;
            case 8:
                decimalformat.applyLocalizedPattern("###,###,###,###,###,###,###,##0.00000000");
                break;
            case 9:
                decimalformat.applyLocalizedPattern("###,###,###,###,###,###,###,##0.000000000");
                break;
            case 10:
                decimalformat.applyLocalizedPattern("###,###,###,###,###,###,###,##0.0000000000");
                break;
            case 11:
                decimalformat.applyLocalizedPattern("###,###,###,###,###,###,###,##0.00000000000");
                break;
            case 12:
                decimalformat.applyLocalizedPattern("###,###,###,###,###,###,###,##0.000000000000");
                break;
            case 13:
                decimalformat.applyLocalizedPattern("###,###,###,###,###,###,###,##0.0000000000000");
                break;
            case 14:
                decimalformat.applyLocalizedPattern("###,###,###,###,###,###,###,##0.00000000000000");
                break;
            case 15:
                decimalformat.applyLocalizedPattern("###,###,###,###,###,###,###,##0.000000000000000");
                break;
            case 16:
                decimalformat.applyLocalizedPattern("###,###,###,###,###,###,###,##0.0000000000000000");
                break;
            case 17:
                decimalformat.applyLocalizedPattern("###,###,###,###,###,###,###,##0.00000000000000000");
                break;
            case 18:
                decimalformat.applyLocalizedPattern("###,###,###,###,###,###,###,##0.000000000000000000");
                break;

            default:
                decimalformat.applyLocalizedPattern("###,###,###,###,###,###,###,##0.000000");
                break;
        }
        return decimalformat;
    }

    public static String mainDenom(BaseChain chain) {
        return DENOM_DVPN;
    }

    // Current price

    public static SpannableString dpPerUserCurrencyValue(BaseData baseData, String denom) {
        BigDecimal totalValue = perUserCurrencyValue(baseData, denom);
        final String formatted = baseData.getCurrencySymbol() + " " + getDecimalFormat(3).format(totalValue);
        return dpCurrencyValue(formatted, 3);
    }

    public static BigDecimal perUserCurrencyValue(BaseData baseData, String denom) {
        if (baseData.getCurrency() == 0) {
            return perUsdValue(baseData, denom);
        } else if (baseData.getPrice("usdt") != null) {
            final Price usdtInfo = baseData.getPrice("usdt");
            final BigDecimal usdtPrice = usdtInfo.currencyPrice(baseData.getCurrencyString().toLowerCase());
            return perUsdValue(baseData, denom).multiply(usdtPrice).setScale(3, RoundingMode.DOWN);
        }
        return BigDecimal.ZERO.setScale(3, RoundingMode.DOWN);
    }

    public static SpannableString dpCurrencyValue(String input, int dpPoint) {
        return getDpString(input, dpPoint);
    }

    public static BigDecimal perUsdValue(BaseData baseData, String denom) {
        if (baseData.getPrice(denom) != null) {
            return baseData.getPrice(denom).currencyPrice("usd").setScale(3, RoundingMode.DOWN);
        }
        return BigDecimal.ZERO.setScale(3, RoundingMode.DOWN);
    }

    public static SpannableString dpValueChange(BaseData baseData, String denom) {
        return getDpString(valueChange(baseData, denom).toPlainString() + "% (24h)", 9);
    }

    public static BigDecimal valueChange(BaseData baseData, String denom) {
        if (baseData.getPrice(denom) != null) {
            return baseData.getPrice(denom).priceChange();
        }
        return BigDecimal.ZERO.setScale(2, RoundingMode.FLOOR);
    }
}
