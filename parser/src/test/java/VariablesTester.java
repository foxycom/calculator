import com.guliash.parser.ArithmeticParser;
import com.guliash.parser.Variable;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by gulash on 07.01.16.
 */
public class VariablesTester extends BaseTester {

    @Test
    public void variablesTest1() {
        ArrayList<Variable> variables = new ArrayList<>();
        variables.add(new Variable("x", -1.0));
        variables.add(new Variable("y", 2.0));
        assertEquals(Math.exp(-1d) * Math.sin(2d), calculate("exp(x)*sin(y)", variables), EPS);
    }

    @Test
    public void variablesTest2() {
        ArrayList<Variable> variables = new ArrayList<>();
        variables.add(new Variable("x", -1.0));
        variables.add(new Variable("y", 2.0));
        assertEquals(Math.min(-1.d, 2.d), calculate("min(x, y)", variables), EPS);
    }

    @Test(expected = ArithmeticParser.ArithmeticParserException.class)
    public void variablesTest3() {
        ArrayList<Variable> variables = new ArrayList<>();
        variables.add(new Variable("exp", 3.0));
        variables.add(new Variable("y", 2.0));
        calculate("exp", variables);
    }
}
