package co.sentinel.cosmos.network;

import static co.sentinel.cosmos.base.BaseChain.SENTINEL_MAIN;

import java.util.concurrent.Executors;

import co.sentinel.cosmos.base.BaseChain;
import ee.solarlabs.constants.BaseUrl;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ChannelBuilder {

    private final static String GRPC_SENTINEL_MAIN = BaseUrl.DVPN;
    private final static int PORT_SENTINEL_MAIN = 993;


    public final static int TIME_OUT = 30;


    public static ManagedChannel getChain(BaseChain chain) {
        if (chain.equals(SENTINEL_MAIN)) {
            return getSentinelMain();
        }
        return null;
    }

    //Channel for sentinel main
    private static ManagedChannel channel_sentinel_main = null;

    public static ManagedChannel getSentinelMain() {
        if (channel_sentinel_main == null) {
            synchronized (ChannelBuilder.class) {
                channel_sentinel_main = ManagedChannelBuilder.forAddress(GRPC_SENTINEL_MAIN, PORT_SENTINEL_MAIN)
                        .usePlaintext()
                        .executor(Executors.newSingleThreadExecutor())
                        .build();
            }
        }
        return channel_sentinel_main;
    }

    public static void resetSentinelMain() {
        if (channel_sentinel_main != null) {
            synchronized (ChannelBuilder.class) {
                channel_sentinel_main.shutdownNow();
                channel_sentinel_main = ManagedChannelBuilder.forAddress(GRPC_SENTINEL_MAIN, PORT_SENTINEL_MAIN)
                        .usePlaintext()
                        .executor(Executors.newSingleThreadExecutor())
                        .build();
            }
        }
    }

}
