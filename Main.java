package enigma;


import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Kaley Wong
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine A = readConfig();
        String setting = _input.nextLine();
        if (!setting.contains("*")) {
            throw new EnigmaException("Wrong input format");
        }
        setUp(A, setting);
        while (_input.hasNextLine()) {
            String testing = _input.nextLine();
            if (testing.contains("*")) {
                setUp(A, testing);
                continue;
            }
            String result = A.convert(testing.replace(" ", ""));
            printMessageLine(result);
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            int numrotors = _config.nextInt();
            int numpawls = _config.nextInt();
            ArrayList<Rotor> allrotors = new ArrayList<Rotor>();
            while (_config.hasNext()) {
                allrotors.add(readRotor());
            }
            return new Machine(_alphabet, numrotors, numpawls, allrotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String rotorname = _config.next();
            if (_config.hasNext("M([^()*\\s]*)*")) {
                String rotornotch = _config.next().substring(1);
                String temp = "";
                while (_config.hasNext("(\\([^()*\\s]*\\))+")) {
                    temp += _config.findInLine("\\([^()*\\s]*\\)") + " ";
                }
                Permutation a = new Permutation(temp, _alphabet);
                return new MovingRotor(rotorname, a, rotornotch);
            } else if (_config.hasNext("N")) {
                _config.next();
                String temp = "";
                while (_config.hasNext("(\\([^()*\\s]*\\))+")) {
                    temp += _config.findInLine("\\([^()*\\s]*\\)") + " ";
                }
                Permutation b = new Permutation(temp, _alphabet);
                return new FixedRotor(rotorname, b);
            } else if (_config.hasNext("R")) {
                _config.next();
                String temp = "";
                while ((_config.hasNext("(\\([^()*\\s]*\\))"))) {
                    temp += _config.next("\\([^()*\\s]*\\)");
                }
                Permutation c = new Permutation(temp, _alphabet);
                return new Reflector(rotorname, c);
            } else {
                throw error("Bad Rotor Description");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String x = settings;
        Scanner setting = new Scanner(settings);
        setting.next();
        String[] temprotors = new String[M.numRotors()];
        for (int i = 0; i < M.numRotors(); i++) {
            temprotors[i] = setting.next();
        }
        for (int i = 0; i < temprotors.length - 1; i++) {
            for (int j = i + 1; j < temprotors.length; j++) {
                if (temprotors[i].equals(temprotors[j])) {
                    throw new EnigmaException("Duplicate Rotor Name");
                }
            }
        }
        M.insertRotors(temprotors);
        M.setRotors(setting.next());
        String permstrings = "";
        while (setting.hasNext()) {
            if (!setting.hasNext("([(][^()*]*[)])")) {
                throw error("Is not a correct permutation");
            }
            permstrings += setting.next();
        }
        M.setPlugboard(new Permutation(permstrings, _alphabet));
    }



    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        System.setOut(_output);
        String whitespace = msg.replaceAll("\\s", "");
        String messageline = "";
        while (whitespace.length() >= 5) {
            messageline += whitespace.substring(0, 5) + " ";
            whitespace = whitespace.substring(5);
        }
        messageline += whitespace;
        _output.println(messageline);

    }

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

}
