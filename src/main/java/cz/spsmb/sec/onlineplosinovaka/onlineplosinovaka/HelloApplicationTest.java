package cz.spsmb.sec.onlineplosinovaka.onlineplosinovaka;

import org.junit.Test;


public class HelloApplicationTest {

    @Test
    public void parsePlatforms() {
        HelloApplication.parsePlatforms("[MAP] {0,880,800,20};{810,770,400,20};{1200,690,400,20}");

    }
}