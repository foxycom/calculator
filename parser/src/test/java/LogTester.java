import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by gulash on 03.01.16.
 */
public class LogTester extends BaseTester {

    @Test
    public void logTest1() {
        assertEquals(Math.log(Math.E), calculate("ln(e)"), EPS);
    }

    @Test
    public void logTest2() {
        assertEquals(log(2, 3), calculate("log(   2  ,   3)"), EPS);
    }

    @Test
    public void logTest3() {
        assertEquals(log(1, 1), calculate("log(1, 1)"), EPS);
    }

    @Test
    public void logTest4() {
        assertEquals(log(Math.E, Math.E * Math.E * Math.E), calculate("ln(pow(e, 3))"), EPS);
    }

    @Test
    public void logTest5() {
        assertEquals(log(Math.E, Math.E * Math.E * Math.E), calculate("log(e,  pow(e  ,   3))"), EPS);
    }

    @Test
    public void logTest6() {
        assertEquals(4, calculate("log10(10*10*10*10)"), EPS);
    }


    private double log(double x, double y) {
        return Math.log(y) / Math.log(x);
    }

}
