package com.imdg.practicas;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.impl.predicates.EqualPredicate;
import com.imdg.pojos.MarketOrder;
import com.imdg.processors.OrderProcessor;

import java.util.Map;

public class P2VolumeProcessing {

    private static void rellenaCache(IMap<String, MarketOrder> mapCustomers) {

        // Limpia cache
        mapCustomers.clear();

        // Añade 1000 ordenes a la cache
        for (int i = 0; i < 1000; ++i) {
            MarketOrder order = new MarketOrder("BBVA", 400, 642);
            MarketOrder orderRep = new MarketOrder("Intel", 500, 3400);
            mapCustomers.set("Ibex35OrderID_" + i, order);
            mapCustomers.set("DowJonesOrderID_" + i, orderRep);
        }

        // Actualiza 100 ordenes
        for (int i = 0; i < 100; ++i) {
            MarketOrder order = new MarketOrder("BBVA", 555, 642);
            MarketOrder orderRep = new MarketOrder("Intel", 1000, 3400);
            mapCustomers.set("Ibex35OrderID_" + i, order);
            mapCustomers.set("DowJonesOrderID_" + i, orderRep);
        }
    }

    public static void main(String[] args) throws Exception {

        // Instanciar hazelcast Cliente y crear una cache
        Config config = new Config();
        config.getNetworkConfig().getJoin().getTcpIpConfig().addMember("localhost").setEnabled(true);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        HazelcastInstance client = Hazelcast.newHazelcastInstance(config);
        IMap<String, MarketOrder> mapCustomers = client.getMap("ordenesMercado");

        // Rellena cache (simulación, en un caso real la rellenaría la operativa diaria)
        rellenaCache(mapCustomers);

        // Ejecutar el EntryProcessor sobre todas las entradas de la caché
        Map<String, Object> ret = mapCustomers.executeOnEntries(new OrderProcessor());
        // ret contiene <Clave, VolumenAntiguoDevuelto>

        // TODO Calcular la suma de los resultados almacenados en "ret"
        int volumenTotal = 0;

        for (Map.Entry<String, Object> orderVolumes : ret.entrySet()) {
            Integer volumen = (Integer) orderVolumes.getValue();
            if (volumen != null) {
                volumenTotal += volumen;
            }
        }

        // TODO Mostrar por pantalla el volumen total calculado anteriormente
        System.out.println("Volumen total negociado durante el día = " + volumenTotal);

        if (mapCustomers.entrySet(new EqualPredicate("volume", 0)).isEmpty()) {
            throw new Exception("Los volumenes de todos los elementos deben quedar a 0");
        }

        client.shutdown();
    }
}
