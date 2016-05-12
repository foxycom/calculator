import com.guliash.parser.ArithmeticParser;
import com.guliash.parser.Variable;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

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

    /**
     * The parser is case-sensitive. So it should be able to calculate a correct expression
     * with same variables
     */
    @Test
    public void sameVariablesButDifferentCaseTest() {
        List<Variable> variables = new ArrayList<>();
        variables.add(new Variable("x", 2d));
        variables.add(new Variable("X", 3d));
        assertEquals(5d, calculate("x + X", variables), EPS);
    }

    @Test
    public void constantsShouldHaveMorePriorityThanVariables() {
        List<Variable> variables = new ArrayList<>();
        variables.add(new Variable("e", 2d));
        assertEquals(Math.E, calculate("e", variables), EPS);
    }
}
