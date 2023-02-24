package blockchain.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class MineUtilTest {

    @Test
    public void startsWithValidZeros() {
        String hashToCheck = "000ASFASF";
        int numberOfZeros = 3;
        assertTrue(MineUtil.startsWithValidZeros(hashToCheck, numberOfZeros));
    }

    @Test
    public void startsWithValidZeros2() {
        String hashToCheck = "000ASFASF";
        int numberOfZeros = 4;
        assertFalse(MineUtil.startsWithValidZeros(hashToCheck, numberOfZeros));
    }

    @Test
    public void getRandomMagicLong() {
        assertTrue(MineUtil.getRandomMagicLong() != MineUtil.getRandomMagicLong());
    }

    @Test
    public void timeSinceInSeconds() {
        long startTime = System.currentTimeMillis();
        assertTrue(MineUtil.timeSinceInSeconds(startTime) >= 0);
    }
}