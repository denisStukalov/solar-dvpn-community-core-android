package co.sentinel.dvpn.hub.tasks

import co.sentinel.cosmos.dao.Account
import co.sentinel.cosmos.model.type.Coin
import com.google.protobuf2.Any
import cosmos.base.v1beta1.CoinOuterClass
import sentinel.subscription.v1.Msg

object GenerateCreateNodeSubscriptionMessage {
    fun execute(account: Account, address: String, deposit: Coin): Any = Any.newBuilder()
        .setTypeUrl("/sentinel.subscription.v1.MsgSubscribeToNodeRequest")
        .setValue(
            Msg.MsgSubscribeToNodeRequest.newBuilder()
                .setFrom(account.address)
                .setAddress(address)
                .setDeposit(
                    CoinOuterClass.Coin.newBuilder()
                        .setDenom(deposit.denom)
                        .setAmount(deposit.amount)
                        .build()
                ).build()
                .toByteString()
        )
        .build()

}