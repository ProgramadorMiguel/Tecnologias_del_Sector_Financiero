package com.imdg.practicas;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.cp.ICountDownLatch;
import com.imdg.pojos.Person;

public class Practica2cIMDG {

    public static void main(String[] args) throws InterruptedException {
        // Configuración recomendada para la práctica
        Config config = new Config();
        config.getNetworkConfig().getJoin().getTcpIpConfig().addMember("localhost").setEnabled(true);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);

        IMap<String, Person> customers = hz.getMap("customers");

        ICountDownLatch latch = hz.getCPSubsystem().getCountDownLatch("startLatch");
        latch.trySetCount(3);

        customers.put("nodo3", new Person("MPL-3", 28003, "3", "3"));

        latch.countDown();
        latch.await(10, java.util.concurrent.TimeUnit.SECONDS);

        System.out.println("[Nodo3] nodo1 = " + customers.get("nodo1"));
        System.out.println("[Nodo3] nodo2 = " + customers.get("nodo2"));
        System.out.println("[Nodo3] nodo3 = " + customers.get("nodo3"));

        while (true);
    }
}
