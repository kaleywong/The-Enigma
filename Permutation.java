package enigma;


import java.util.HashMap;


import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Kaley Wong
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        pcycles = new HashMap<Character, Character>();
        icycles = new HashMap<Character, Character>();
        String permutetemp = cycles;
        String inverttemp = cycles;
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        cycles.replaceAll("\\s+", "");
        for (int i = 0; i < cycles.length(); i++) {
            if (cycles.charAt(i) == '(' || cycles.charAt(i) == ')') {
                continue;
            }
            for (int j = i + 1; j < cycles.length(); j++) {
                if (cycles.charAt(j) == '(' || cycles.charAt(j) == ')'
                        || cycles.charAt(j) == ' ') {
                    continue;
                }
                if (cycles.charAt(i) == (cycles.charAt(j))) {
                    throw new EnigmaException("No duplicates allowed cycle");
                }
            }
        }
        for (int i = 0; i < cycles.length(); i++) {
            if (permutetemp.charAt(i) == '(') {
                a = i + 1;
            }
            if (permutetemp.charAt(i) == ')') {
                b = i;
                cpermute = permutetemp.substring(a, b);
                for (int j = 0; j <= cpermute.length() - 1; j++) {
                    if (j == cpermute.length() - 1) {
                        pcycles.put(cpermute.charAt(j), cpermute.charAt(0));
                    } else {
                        pcycles.put(cpermute.charAt(j),
                                cpermute.charAt(j + 1));
                    }
                }
            }
        }
        for (int i = 0; i < cycles.length(); i++) {
            if (inverttemp.charAt(i) == '(') {
                c = i + 1;
            }
            if (inverttemp.charAt(i) == ')') {
                d = i;
                cinvert = inverttemp.substring(c, d);
                for (int j = cinvert.length() - 1; j >= 0; j--) {
                    if (j == 0) {
                        icycles.put(cinvert.charAt(j),
                                cinvert.charAt(cinvert.length() - 1));
                    } else {
                        icycles.put(cinvert.charAt(j), cinvert.charAt(j - 1));
                    }
                }
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();

    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int a = wrap(p);
        char letter = _alphabet.toChar(a);
        if (a < 0) {
            a += _alphabet.size();
        }
        if (pcycles.containsKey(letter)) {
            return _alphabet.toInt(pcycles.get(_alphabet.toChar(a)));
        }
        return a;

    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int a = wrap(c);
        char letter = _alphabet.toChar(a);
        if (a < 0) {
            a += _alphabet.size();
        }
        if (icycles.containsKey(letter)) {
            return _alphabet.toInt(icycles.get(_alphabet.toChar(a)));
        }
        return a;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (_alphabet.contains(p)) {
            return _alphabet.toChar(permute(_alphabet.toInt(p)));
        } else {
            throw new EnigmaException("Letter is not in the alphabet");
        }
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (_alphabet.contains(c)) {
            return _alphabet.toChar(invert(_alphabet.toInt(c)));
        } else {
            throw new EnigmaException("Letter is not in the alphabet");
        }
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < _alphabet.size(); i++) {
            if (permute(i) == i) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation.*/
    private Alphabet _alphabet;
    /** Hashmap for Permute cycles.*/
    private HashMap<Character, Character> pcycles;
    /** Hashmap for invert cycles.*/
    private HashMap<Character, Character> icycles;
    /** cycles for permute.*/
    private String cpermute;
    /** cycles for invert.*/
    private String cinvert;

}
