package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Kaley Wong
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }
    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return true;
    }
    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        for (int i = 0; i < _notches.length(); i++) {
            if (this._notches.charAt(i) == alphabet().toChar(setting())) {
                return true;
            }
        }
        return false;
    }

    @Override
    void advance() {
        set(wrap(setting() + 1));
    }

/** the notches in the rotor.*/
    private String _notches;

}
