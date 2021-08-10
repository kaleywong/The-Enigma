package enigma;


/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Kaley Wong
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _chars = chars;
        for (int i = 0; i < _chars.length(); i++) {
            if (_chars.charAt(i) == '(' || _chars.charAt(i) == ')') {
                continue;
            }
            for (int j = i + 1; j < _chars.length(); j++) {
                if (_chars.charAt(j) == '(' || _chars.charAt(j) == ')') {
                    continue;
                }
                if (_chars.charAt(i) == (_chars.charAt(j))) {
                    throw new EnigmaException("No duplicates in the Alphabet");
                }
            }
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _chars.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return _chars.contains(String.valueOf(ch));
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return _chars.charAt(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {

        return _chars.indexOf(ch + "");
    }
    /** additional. */

    private String _chars;

}
