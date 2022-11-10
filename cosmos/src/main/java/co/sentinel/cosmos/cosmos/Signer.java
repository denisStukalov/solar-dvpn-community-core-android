package co.sentinel.cosmos.cosmos;

import static co.sentinel.cosmos.utils.WUtil.integerToBytes;
import static cosmos.tx.signing.v1beta1.Signing.SignMode.SIGN_MODE_DIRECT;

import com.google.protobuf.ByteString;
import com.google.protobuf2.Any;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.crypto.DeterministicKey;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import co.sentinel.cosmos.crypto.Sha256;
import co.sentinel.cosmos.model.type.Coin;
import co.sentinel.cosmos.model.type.Fee;
import co.sentinel.cosmos.utils.WKey;
import cosmos.auth.v1beta1.Auth;
import cosmos.auth.v1beta1.QueryOuterClass;
import cosmos.base.v1beta1.CoinOuterClass;
import cosmos.tx.v1beta1.ServiceOuterClass;
import cosmos.tx.v1beta1.TxOuterClass;
import cosmos.vesting.v1beta1.Vesting;

public class Signer {

    public static String onParseAddress(QueryOuterClass.QueryAccountResponse auth) {
        try {
            if (auth.getAccount().getTypeUrl().contains(Auth.BaseAccount.getDescriptor().getFullName())) {
                Auth.BaseAccount account = Auth.BaseAccount.parseFrom(auth.getAccount().getValue());
                return account.getAddress();

            } else if (auth.getAccount().getTypeUrl().contains(Vesting.PeriodicVestingAccount.getDescriptor().getFullName())) {
                Vesting.PeriodicVestingAccount account = Vesting.PeriodicVestingAccount.parseFrom(auth.getAccount().getValue());
                return account.getBaseVestingAccount().getBaseAccount().getAddress();

            } else if (auth.getAccount().getTypeUrl().contains(Vesting.ContinuousVestingAccount.getDescriptor().getFullName())) {
                Vesting.ContinuousVestingAccount account = Vesting.ContinuousVestingAccount.parseFrom(auth.getAccount().getValue());
                return account.getBaseVestingAccount().getBaseAccount().getAddress();
            }

        } catch (Exception e) {
        }
        return "";
    }

    public static long onParseAccountNumber(QueryOuterClass.QueryAccountResponse auth) {
        try {
            if (auth.getAccount().getTypeUrl().contains(Auth.BaseAccount.getDescriptor().getFullName())) {
                Auth.BaseAccount account = Auth.BaseAccount.parseFrom(auth.getAccount().getValue());
                return account.getAccountNumber();

            } else if (auth.getAccount().getTypeUrl().contains(Vesting.PeriodicVestingAccount.getDescriptor().getFullName())) {
                Vesting.PeriodicVestingAccount account = Vesting.PeriodicVestingAccount.parseFrom(auth.getAccount().getValue());
                return account.getBaseVestingAccount().getBaseAccount().getAccountNumber();

            } else if (auth.getAccount().getTypeUrl().contains(Vesting.ContinuousVestingAccount.getDescriptor().getFullName())) {
                Vesting.ContinuousVestingAccount account = Vesting.ContinuousVestingAccount.parseFrom(auth.getAccount().getValue());
                return account.getBaseVestingAccount().getBaseAccount().getAccountNumber();

            }

        } catch (Exception e) {
        }
        return 0;
    }

    public static long onParseSequenceNumber(QueryOuterClass.QueryAccountResponse auth) {
        try {
            if (auth.getAccount().getTypeUrl().contains(Auth.BaseAccount.getDescriptor().getFullName())) {
                Auth.BaseAccount account = Auth.BaseAccount.parseFrom(auth.getAccount().getValue());
                return account.getSequence();

            } else if (auth.getAccount().getTypeUrl().contains(Vesting.PeriodicVestingAccount.getDescriptor().getFullName())) {
                Vesting.PeriodicVestingAccount account = Vesting.PeriodicVestingAccount.parseFrom(auth.getAccount().getValue());
                return account.getBaseVestingAccount().getBaseAccount().getSequence();

            } else if (auth.getAccount().getTypeUrl().contains(Vesting.ContinuousVestingAccount.getDescriptor().getFullName())) {
                Vesting.ContinuousVestingAccount account = Vesting.ContinuousVestingAccount.parseFrom(auth.getAccount().getValue());
                return account.getBaseVestingAccount().getBaseAccount().getSequence();

            }

        } catch (Exception e) {
        }
        return 0;
    }

    //gRpc Singer
    public static ServiceOuterClass.BroadcastTxRequest getGrpcSendReq(QueryOuterClass.QueryAccountResponse auth, String toAddress, ArrayList<Coin> amounts, Fee fee, String memo, DeterministicKey pKey, String chainId) {
        CoinOuterClass.Coin toSendCoin = CoinOuterClass.Coin.newBuilder().setAmount(amounts.get(0).amount).setDenom(amounts.get(0).denom).build();
        cosmos.bank.v1beta1.Tx.MsgSend msgSend = cosmos.bank.v1beta1.Tx.MsgSend.newBuilder().addAmount(toSendCoin).setFromAddress(onParseAddress(auth)).setToAddress(toAddress).build();
        Any msgSendAny = Any.newBuilder().setTypeUrl("/cosmos.bank.v1beta1.MsgSend").setValue(msgSend.toByteString()).build();

        TxOuterClass.TxBody txBody = getGrpcTxBody(msgSendAny, memo);
        TxOuterClass.SignerInfo signerInfo = getGrpcSignerInfo(auth, pKey);
        TxOuterClass.AuthInfo authInfo = getGrpcAuthInfo(signerInfo, fee);
        TxOuterClass.TxRaw rawTx = getGrpcRawTx(auth, txBody, authInfo, pKey, chainId);
        return ServiceOuterClass.BroadcastTxRequest.newBuilder().setModeValue(ServiceOuterClass.BroadcastMode.BROADCAST_MODE_SYNC.getNumber()).setTxBytes(rawTx.toByteString()).build();
    }

    //gRpc Singer
    public static ServiceOuterClass.BroadcastTxRequest getGrpcNodeSubscribeReq(QueryOuterClass.QueryAccountResponse auth, String toAccount, Fee fee, Any msgSendAny, DeterministicKey pKey, String chainId) {
        TxOuterClass.TxBody txBody = getGrpcTxBody(msgSendAny, "");
        TxOuterClass.SignerInfo signerInfo = getGrpcSignerInfo(auth, pKey);
        TxOuterClass.AuthInfo authInfo = getGrpcAuthInfo(signerInfo, fee);
        TxOuterClass.TxRaw rawTx = getGrpcRawTx(auth, txBody, authInfo, pKey, chainId);
        return ServiceOuterClass.BroadcastTxRequest.newBuilder().setModeValue(ServiceOuterClass.BroadcastMode.BROADCAST_MODE_SYNC.getNumber()).setTxBytes(rawTx.toByteString()).build();
    }

    //gRpc Singer
    public static ServiceOuterClass.BroadcastTxRequest getGrpcGenericReq(QueryOuterClass.QueryAccountResponse auth, Fee fee, List<Any> msgSendAny, DeterministicKey pKey, String chainId) {
        TxOuterClass.TxBody txBody = getGrpcTxBodys(msgSendAny, "");
        TxOuterClass.SignerInfo signerInfo = getGrpcSignerInfo(auth, pKey);
        TxOuterClass.AuthInfo authInfo = getGrpcAuthInfo(signerInfo, fee);
        TxOuterClass.TxRaw rawTx = getGrpcRawTx(auth, txBody, authInfo, pKey, chainId);
        return ServiceOuterClass.BroadcastTxRequest.newBuilder().setModeValue(ServiceOuterClass.BroadcastMode.BROADCAST_MODE_BLOCK.getNumber()).setTxBytes(rawTx.toByteString()).build();
    }


    public static TxOuterClass.TxBody getGrpcTxBody(Any msgAny, String memo) {
        return TxOuterClass.TxBody.newBuilder().addMessages(msgAny).setMemo(memo).build();
    }

    public static TxOuterClass.TxBody getGrpcTxBodys(List<Any> msgsAny, String memo) {
        TxOuterClass.TxBody.Builder builder = TxOuterClass.TxBody.newBuilder();
        for (Any msg : msgsAny) {
            builder.addMessages(msg);
        }
        return builder.setMemo(memo).build();
    }

    public static TxOuterClass.SignerInfo getGrpcSignerInfo(QueryOuterClass.QueryAccountResponse auth, DeterministicKey pKey) {
        Any pubKey = WKey.generateGrpcPubKeyFromPriv(pKey.getPrivateKeyAsHex());
        TxOuterClass.ModeInfo.Single singleMode = TxOuterClass.ModeInfo.Single.newBuilder().setMode(SIGN_MODE_DIRECT).build();
        TxOuterClass.ModeInfo modeInfo = TxOuterClass.ModeInfo.newBuilder().setSingle(singleMode).build();
        return TxOuterClass.SignerInfo.newBuilder().setPublicKey(pubKey).setModeInfo(modeInfo).setSequence(onParseSequenceNumber(auth)).build();
    }

    public static TxOuterClass.AuthInfo getGrpcAuthInfo(TxOuterClass.SignerInfo signerInfo, Fee fee) {
        CoinOuterClass.Coin feeCoin = CoinOuterClass.Coin.newBuilder().setAmount(fee.amount.get(0).amount).setDenom(fee.amount.get(0).denom).build();
        TxOuterClass.Fee txFee = TxOuterClass.Fee.newBuilder().addAmount(feeCoin).setGasLimit(Long.parseLong(fee.gas)).build();
        return TxOuterClass.AuthInfo.newBuilder().setFee(txFee).addSignerInfos(signerInfo).build();
    }

    public static TxOuterClass.TxRaw getGrpcRawTx(QueryOuterClass.QueryAccountResponse auth, TxOuterClass.TxBody txBody, TxOuterClass.AuthInfo authInfo, DeterministicKey pKey, String chainId) {
        TxOuterClass.SignDoc signDoc = TxOuterClass.SignDoc.newBuilder().setBodyBytes(txBody.toByteString()).setAuthInfoBytes(authInfo.toByteString()).setChainId(chainId).setAccountNumber(onParseAccountNumber(auth)).build();
        byte[] sigbyte = Signer.getGrpcByteSingleSignature(pKey, signDoc.toByteArray());
        return TxOuterClass.TxRaw.newBuilder().setBodyBytes(txBody.toByteString()).setAuthInfoBytes(authInfo.toByteString()).addSignatures(ByteString.copyFrom(sigbyte)).build();
    }

    public static byte[] getGrpcByteSingleSignature(DeterministicKey key, byte[] toSignByte) {
        MessageDigest digest = Sha256.getSha256Digest();
        byte[] toSignHash = digest.digest(toSignByte);
        ECKey.ECDSASignature Signature = key.sign(Sha256Hash.wrap(toSignHash));
        byte[] sigData = new byte[64];
        System.arraycopy(integerToBytes(Signature.r, 32), 0, sigData, 0, 32);
        System.arraycopy(integerToBytes(Signature.s, 32), 0, sigData, 32, 32);
        return sigData;
    }
}
