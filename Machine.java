package enigma;

import java.util.Collection;

import static enigma.EnigmaException.*;



/** Class that represents a complete enigma machine.
 *  @author Kaley Wong
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        numberofpawls = pawls;
        numberofrotors = numRotors;
        _allRotors = allRotors.toArray();
        _rotors = new Rotor[numRotors];

    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return numberofrotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return numberofpawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < rotors.length; i++) {
            for (int j = 0; j < _allRotors.length; j++) {
                String rotorsting = rotors[i].toString();
                String allrotorsting = ((Rotor) _allRotors[j]).name();
                if (rotorsting.equals(allrotorsting)) {
                    _rotors[i] = (Rotor) _allRotors[j];
                }
            }
        }
        if (!_rotors[0].reflecting()) {
            throw new EnigmaException("First rotor is not reflector");
        }
        boolean f = false;
        for (int i = 0; i < _rotors.length; i++) {
            if (_rotors[i] == null) {
                throw new EnigmaException("Rotors do not match");
            }
        }
        int counter = 0;
        for (int i = 0; i < _rotors.length; i++) {
            if (_rotors[i].rotates()) {
                counter += 1;
            }
        }
        if (rotors.length != numberofrotors || counter != numberofpawls) {
            throw new EnigmaException("Wrong number of Rotors");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numberofrotors - 1) {
            throw error("Wrong Setting Lengths");
        }
        for (int i = 1; i < numRotors(); i++) {
            if (!_alphabet.contains(setting.charAt(i - 1))) {
                throw new EnigmaException("Mistyped setting string");
            }
            _rotors[i].set(setting.charAt(i - 1));
        }
        for (int j = 1; j < numRotors(); j++) {
            if (!_alphabet.contains(setting.charAt(j - 1))) {
                throw new EnigmaException("Initial Positions not in Alphabet");
            }
        }
    }


    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
      *  *  the machine. */
    int convert(int c) {
        if (_rotors[3].atNotch()) {
            _rotors[2].advance();
            _rotors[3].advance();
        }
        if (_rotors[4].atNotch()) {
            _rotors[3].advance();
        }
        _rotors[numberofrotors - 1].advance();
        int plugboardperm = _plugboard.permute(c);
        for (int i = numberofrotors - 1; i >= 0; i--) {
            plugboardperm = _rotors[i].convertForward(plugboardperm);
        }
        for (int j = 1; j < numberofrotors; j++) {
            plugboardperm = _rotors[j].convertBackward(plugboardperm);
        }
        plugboardperm = _plugboard.permute(plugboardperm);
        return plugboardperm;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
       the rotors accordingly. */
    String convert(String msg) {
        String message = "";
        for (int i = 0; i < msg.length(); i++) {
            int msgint = _alphabet.toInt(msg.charAt(i));
            char msgchar = _alphabet.toChar(convert(msgint));
            message += msgchar;
        }
        return message;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** Number of rotors this machine has.*/
    private int numberofrotors;
    /** number of pawls this mahcine has. */
    private int numberofpawls;
    /** collection of all rotors.*/
    private Object[] _allRotors;
    /** Array of rotors that the machine uses.*/
    private Rotor[] _rotors;
    /** Machines plugboard.*/
    private Permutation _plugboard;
}

