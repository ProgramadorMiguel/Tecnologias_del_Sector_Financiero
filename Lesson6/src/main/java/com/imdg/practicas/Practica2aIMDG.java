package com.imdg.practicas;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.cp.ICountDownLatch;
import com.imdg.pojos.Person;

public class Practica2aIMDG {

    public static void main(String[] args) throws InterruptedException {
        // Configuración recomendada para la práctica
        Config config = new Config();
        config.getNetworkConfig().getJoin().getTcpIpConfig().addMember("localhost").setEnabled(true);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        // Crear instancia de Hazelcast
        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);

        // Caché distribuida
        IMap<String, Person> customers = hz.getMap("customers");

        // Latch distribuido
        ICountDownLatch latch = hz.getCPSubsystem().getCountDownLatch("startLatch");

        // Solo el primero que llegue lo inicializa a 3
        latch.trySetCount(3);

        // Este nodo escribe su valor
        customers.put("nodo1", new Person("MPL-1", 28001, "1", "1"));

        // Indica que ha terminado
        latch.countDown();

        // Espera a que los otros dos nodos terminen
        latch.await(10, java.util.concurrent.TimeUnit.SECONDS);

        // Ahora ya deberían estar los 3 en la caché
        System.out.println("[Nodo1] nodo1 = " + customers.get("nodo1"));
        System.out.println("[Nodo1] nodo2 = " + customers.get("nodo2"));
        System.out.println("[Nodo1] nodo3 = " + customers.get("nodo3"));

        while (true);
    }
}
