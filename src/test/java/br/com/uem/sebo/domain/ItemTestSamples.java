package br.com.uem.sebo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Item getItemSample1() {
        return new Item().id(1L).titulo("titulo1").ano(1).genero("genero1").autorArtista("autorArtista1");
    }

    public static Item getItemSample2() {
        return new Item().id(2L).titulo("titulo2").ano(2).genero("genero2").autorArtista("autorArtista2");
    }

    public static Item getItemRandomSampleGenerator() {
        return new Item()
            .id(longCount.incrementAndGet())
            .titulo(UUID.randomUUID().toString())
            .ano(intCount.incrementAndGet())
            .genero(UUID.randomUUID().toString())
            .autorArtista(UUID.randomUUID().toString());
    }
}
