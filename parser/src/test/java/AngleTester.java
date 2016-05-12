import com.guliash.parser.Angle;

import org.junit.Test;

import static com.guliash.parser.Functions.convertAngles;
import static org.junit.Assert.assertEquals;

/**
 * Created by gulash on 03.01.16.
 */
public class AngleTester extends BaseTester {

    @Test
    public void angleTest1() {
        assertEquals(Math.cos(convertAngles(45, Angle.DEG, Angle.RAD)),
                calculate("cos(45)", Angle.DEG), EPS);
    }

    @Test
    public void angleTest2() {
        assertEquals(Math.sqrt(2), calculate("cos(45) + sin(45)", Angle.DEG), EPS);
    }

    @Test
    public void angleTest3() {
        assertEquals(-1, calculate("cos(pi)", Angle.RAD), EPS);
    }

    @Test
    public void angleTest4() {
        assertEquals(Math.cos(0.636172512), calculate("cos(40.5)", Angle.GRAD), EPS);
    }
}
