package br.com.uem.sebo.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class VendaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Venda getVendaSample1() {
        return new Venda().id(1L);
    }

    public static Venda getVendaSample2() {
        return new Venda().id(2L);
    }

    public static Venda getVendaRandomSampleGenerator() {
        return new Venda().id(longCount.incrementAndGet());
    }
}
