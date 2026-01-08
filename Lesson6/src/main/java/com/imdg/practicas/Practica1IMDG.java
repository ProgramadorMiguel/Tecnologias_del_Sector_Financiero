package com.imdg.practicas;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class Practica1IMDG {

    public static void main(String[] args) {
        // Configuración recomendada para la práctica
        Config config = new Config();
        config.getNetworkConfig().getJoin().getTcpIpConfig().addMember("localhost").setEnabled(true);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        // TODO Crear una instancia de hazelcast con la configuración config
        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);
        // TODO Crear una caché llamada customers
        IMap<String, String> customers = hz.getMap("customers");
        // TODO insertar una nueva entrada en la caché con la clave “Test” y un valor con vuestras iniciales
        customers.put("Test", "MPL"); // ← aquí tus iniciales

        System.out.println("Valor insertado: " + customers.get("Test"));
        // Leer el output de consola y ver como hazelcast va encontrando "miembros"
        // Comprobar que se conectan (en el output deberían verse 3 miembros en la consola) y capturarlo

        //while (true);
        while (true);
    }
}
