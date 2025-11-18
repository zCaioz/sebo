package br.com.uem.sebo.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DiscoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Disco getDiscoSample1() {
        return new Disco().id(1L).duracaoMinutos(1);
    }

    public static Disco getDiscoSample2() {
        return new Disco().id(2L).duracaoMinutos(2);
    }

    public static Disco getDiscoRandomSampleGenerator() {
        return new Disco().id(longCount.incrementAndGet()).duracaoMinutos(intCount.incrementAndGet());
    }
}
