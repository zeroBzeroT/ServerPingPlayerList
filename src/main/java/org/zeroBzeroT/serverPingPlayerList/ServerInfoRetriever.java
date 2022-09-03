package org.zeroBzeroT.serverPingPlayerList;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Test a Server Connection
 */
public class ServerInfoRetriever implements Callback<ServerPing> {
    public ServerPing ping = null;
    public final FutureTask<Object> task = new FutureTask<>(() -> {
    }, new Object());

    public static ServerPing getServerPing(String name) throws ExecutionException, InterruptedException, TimeoutException {
        ServerInfoRetriever sig = new ServerInfoRetriever();
        ProxyServer.getInstance().getServerInfo(name).ping(sig);
        sig.task.get(1000, TimeUnit.MILLISECONDS);

        return sig.ping;
    }

    @Override
    public void done(ServerPing serverPing, Throwable throwable) {
        if (throwable == null)
            ping = serverPing;

        task.run();
    }
}