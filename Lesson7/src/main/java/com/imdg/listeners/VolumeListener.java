package com.imdg.listeners;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;
import com.imdg.pojos.MarketOrder;

import java.io.Serializable;

public class VolumeListener
        implements EntryAddedListener<String, MarketOrder>,
        EntryUpdatedListener<String, MarketOrder>, Serializable {

    private String instrumentoAControlar;
    private int volumenAcumulado = 0;

    public VolumeListener(String instrument) {
        this.instrumentoAControlar = instrument;
    }

    /**
     * Escuchar entradas que se añaden y sumarlo al volumen/imprimir alerta si llegamos a 30000
     *
     * @param entryEvent
     */
    @Override
    public void entryAdded(EntryEvent<String, MarketOrder> entryEvent) {
        // TODO Recuperar la propiedad instrument del objeto MarketOrder y comprobar si es igual a instrumentoAControlar
        MarketOrder nuevaOrden = entryEvent.getValue();
        String instrumento = nuevaOrden.getInstrument();

        // TODO Si es igual, actualizar el volumen acumulado de este listener con el volumen del objeto MarketOrder
        if (instrumentoAControlar.equals(instrumento)) {
            volumenAcumulado += nuevaOrden.getVolume();

            // TODO Comprobar si el volumen acumulado es mayor que 30000
            if (volumenAcumulado >= 30000) {

                // TODO Si es mayor, poner el volumen acumulado a 0 e imprimir por pantalla un mensaje de alerta
                System.out.println("ALERTA: volumen acumulado de " + instrumentoAControlar
                        + " superior a 30000. Volumen acumulado = " + volumenAcumulado);
                volumenAcumulado = 0;
            }
        }
    }

    /**
     * Escuchar entradas que se añaden, restar valor antiguo y
     * sumar el nuevo al volumen/imprimir alerta si llegamos a 30000
     *
     * @param entryEvent
     */
    @Override
    public void entryUpdated(EntryEvent<String, MarketOrder> entryEvent) {
        MarketOrder ordenAntigua = entryEvent.getOldValue();
        MarketOrder ordenNueva = entryEvent.getValue();

        // Si la orden no es del instrumento que controlamos, no hacemos nada
        if (!instrumentoAControlar.equals(ordenNueva.getInstrument())) {
            return;
        }

        // Restar volumen antiguo (corrige lo que ya habíamos sumado)
        if (ordenAntigua != null) {
            volumenAcumulado -= ordenAntigua.getVolume();
        }

        // Sumar volumen nuevo
        volumenAcumulado += ordenNueva.getVolume();

        // Comprobar umbral y lanzar alerta si procede
        if (volumenAcumulado >= 30000) {
            System.out.println("ALERTA (UPDATE): volumen acumulado de " + instrumentoAControlar
                    + " superior a 30000 tras actualización. Volumen acumulado = " + volumenAcumulado);
            volumenAcumulado = 0;
        }
    }
}
