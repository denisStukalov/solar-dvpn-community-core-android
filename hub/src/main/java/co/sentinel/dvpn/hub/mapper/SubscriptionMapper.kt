package co.sentinel.dvpn.hub.mapper

import co.sentinel.dvpn.domain.core.functional.Mapper
import co.sentinel.dvpn.domain.features.hub.model.Coin
import co.sentinel.dvpn.domain.features.hub.model.Subscription
import sentinel.subscription.v1.SubscriptionOuterClass
import sentinel.types.v1.StatusOuterClass
import java.time.Instant

object SubscriptionMapper : Mapper<SubscriptionOuterClass.Subscription, Subscription> {
    override fun map(obj: SubscriptionOuterClass.Subscription) = Subscription(
        id = obj.id,
        node = obj.node,
        owner = obj.owner,
        price = Coin(obj.price.denom, obj.price.amount),
        deposit = Coin(obj.deposit.denom, obj.deposit.amount),
        plan = obj.plan,
        denom = obj.denom,
        expirationDate = Instant.ofEpochSecond(obj.expiry.seconds, obj.expiry.nanos.toLong()),
        isActive = obj.status == StatusOuterClass.Status.STATUS_ACTIVE
    )
}