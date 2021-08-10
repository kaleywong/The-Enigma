package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Kaley Wong
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    @Test
    public void testInvertChar() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertEquals('B', p.invert('A'));
        assertEquals('A', p.invert('C'));
        assertEquals('C', p.invert('D'));
        assertEquals('D', p.invert('B'));

        Permutation a = new Permutation("(DA)(T)", new Alphabet("DAT"));
        assertEquals('D', a.invert('A'));
        assertEquals('A', a.invert('D'));
        assertEquals('T', a.invert('T'));

        Permutation b = new Permutation("", new Alphabet("CAT"));
        assertEquals('C', b.invert('C'));
        assertEquals('A', b.invert('A'));
        assertEquals('T', b.invert('T'));

        Permutation c = new Permutation("(K)", new Alphabet("K"));
        assertEquals('K', c.invert('K'));



    }
    @Test
    public void testPermuteChar() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertEquals('A', p.permute('B'));
        assertEquals('C', p.permute('A'));
        assertEquals('D', p.permute('C'));
        assertEquals('B', p.permute('D'));

        Permutation a = new Permutation("(DA)(T)", new Alphabet("DAT"));
        assertEquals('A', a.permute('D'));
        assertEquals('D', a.permute('A'));
        assertEquals('T', a.permute('T'));

        Permutation b = new Permutation("", new Alphabet("CAT"));
        assertEquals('C', b.permute('C'));
        assertEquals('A', b.permute('A'));
        assertEquals('T', b.permute('T'));

        Permutation c = new Permutation("(K)", new Alphabet("K"));
        assertEquals('K', c.permute('K'));


    }
    @Test
    public void testSizeint() {

        Permutation b = new Permutation("(BCDA)", new Alphabet("ABCD"));
        assertEquals(4, b.size());

        Permutation c = new Permutation("", new Alphabet(""));
        assertEquals(0, c.size());
    }
    @Test
    public void testPermuteint() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertEquals(0, p.permute(1));
        assertEquals(2, p.permute(0));
        assertEquals(3, p.permute(2));
        assertEquals(1, p.permute(3));
        assertEquals(2, p.permute(4));

        Permutation a = new Permutation("(DA)(T)", new Alphabet("DAT"));
        assertEquals(1, a.permute(0));
        assertEquals(0, a.permute(1));
        assertEquals(2, a.permute(2));

        Permutation b = new Permutation("", new Alphabet("CAT"));
        assertEquals(0, b.permute(0));
        assertEquals(1, b.permute(1));
        assertEquals(2, b.permute(2));


    }
    @Test
    public void testinverseint() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertEquals(1, p.invert(0));
        assertEquals(0, p.invert(2));
        assertEquals(2, p.invert(3));
        assertEquals(3, p.invert(1));
        assertEquals(1, p.invert(4));

        Permutation a = new Permutation("(DA)(T)", new Alphabet("DAT"));
        assertEquals(0, a.invert(1));
        assertEquals(1, a.invert(0));
        assertEquals(2, a.invert(2));

        Permutation b = new Permutation("", new Alphabet("CAT"));
        assertEquals(0, b.invert(0));
        assertEquals(1, b.invert(1));
        assertEquals(2, b.invert(2));
    }
    @Test
    public void testderangement() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertEquals(true, p.derangement());

        Permutation a = new Permutation("(DA)(T)", new Alphabet("DAT"));
        assertEquals(false, a.derangement());

        Permutation c = new Permutation("", new Alphabet("CAT"));
        assertEquals(false, c.derangement());

        Permutation d = new Permutation("K", new Alphabet("K"));
        assertEquals(false, d.derangement());
    }
    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        p.invert('F');
    }
    @Test(expected = EnigmaException.class)
    public void testNoduplicatesinAlphabet() {
        Permutation p = new Permutation("(ACB)", new Alphabet("ABCC"));
        p.permute('C');
    }
    @Test(expected = EnigmaException.class)
    public void testNoduplicatesinCycles() {
        Permutation p = new Permutation("(ACBC)", new Alphabet("ABC"));
        p.permute('B');
    }


    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

}
