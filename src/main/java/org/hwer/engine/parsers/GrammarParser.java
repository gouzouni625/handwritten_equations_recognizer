package org.hwer.engine.parsers;


import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;

import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory;
import org.hwer.engine.utilities.Utilities;
import org.hwer.engine.utilities.logging.SingleLineFormatter;
import org.hwer.engine.utilities.traces.*;


/**
 * @class GrammarParser
 * @brief Implements a Parser that uses a Symbol Grammar
 */
public class GrammarParser extends Parser {
    /**
     * @brief Constructor
     *
     * @param grammar
     *     The Grammar of this GrammarParser
     */
    public GrammarParser (Grammar grammar) {
        equation_ = "";
        calculateEquation_ = true;

        grammar_ = grammar;

        setupLogger();
    }

    /**
     * @brief Sets up the logger
     */
    private void setupLogger(){
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SingleLineFormatter());

        logger_.setUseParentHandlers(false);

        logger_.addHandler(consoleHandler);
    }

    /**
     * @brief Parses a given set of Symbols setting the relationships between them
     */
    @Override
    public synchronized void parse (Symbol[] symbols) {
        symbols_ = symbols;

        if (symbols_ == null) {
            return;
        }

        parseRecursively(symbols_);

        for (Symbol symbol : symbols_) {
            if (symbol.getClazz() == SymbolFactory.Classes.AMBIGUOUS) {
                symbol.reEvaluate(true);
            }
        }

        calculateEquation_ = true;
    }

    private void parseRecursively (Symbol[] symbols) {
        int numberOfSymbols = symbols.length;

        switch (numberOfSymbols) {
            case 0:
                return;
            case 1:
                symbols[0].reEvaluate(true);
                return;
            default:
                break;
        }

        Arrays.sort(symbols, new Comparator<Symbol>() {
            public int compare (Symbol symbol1, Symbol symbol2) {
                double x1 = symbol1.getTraceGroup().getTopLeftCorner().x_;
                double x2 = symbol2.getTraceGroup().getTopLeftCorner().x_;

                if (x1 > x2) {
                    return 1;
                }
                else if (x1 == x2) {
                    return 0;
                }
                else {
                    return - 1;
                }
            }
        });

        /* ===== Logs Start ===== */
        if (logger_.getLevel() != Level.OFF) {
            logger_.info("Symbols sorted by abscissa... ===== Start =====");

            for (int i = 0; i < numberOfSymbols; i++) {
                logger_.info("Symbol " + i + ": " + symbols[i]);
            }

            logger_.info("Symbols sorted by abscissa... ===== End =====");
        }
        /* ===== Logs End ===== */

        Hashtable<Integer, int[]> pathsTable = new Hashtable<Integer, int[]>();
        for (int i = 0; i < numberOfSymbols - 1; i++) {
            int[][] paths = this.processPath(symbols, symbols[i], i, symbols[i + 1], i + 1);

            for (int[] path : paths) {
                pathsTable.put(Utilities.pathHashKey(path), path);
            }
        }

        int numberOfPaths = pathsTable.size();
        int[][] paths = new int[numberOfPaths][2];
        Iterator<int[]> iterator = pathsTable.values().iterator();
        int index = 0;
        while (iterator.hasNext()) {
            paths[index] = iterator.next();
            index++;
        }

        /* ===== Logs Start ===== */
        if (logger_.getLevel() != Level.OFF) {
            logger_.info("Paths... ===== Start =====");

            for (int i = 0; i < numberOfPaths; i++) {
                logger_.info("Symbol path " + i + ": " + paths[i][0] + ", " + paths[i][1]);
            }

            logger_.info("Paths... ===== End =====");
        }
        /* ===== Logs End ===== */

        Arrays.sort(paths, new Comparator<int[]>() {
            public int compare (int[] path1, int[] path2) {
                if (path1[0] > path2[0]) {
                    return 1;
                }
                else if (path1[0] == path2[0]) {
                    if (path1[1] > path2[1]) {
                        return 1;
                    }
                    else if (path1[1] == path2[1]) {
                        return 0;
                    }
                    else {
                        return - 1;
                    }
                }
                else {
                    return - 1;
                }
            }
        });

        /* ===== Logs Start ===== */
        if (logger_.getLevel() != Level.OFF) {
            logger_.info("Paths after sorting them... ===== Start =====");

            for (int i = 0; i < numberOfPaths; i++) {
                logger_.info("path " + i + " = ");

                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < paths[i].length; j++) {
                    stringBuilder.append(paths[i][j]).append(", ");
                }

                logger_.info(stringBuilder.toString());
            }

            logger_.info("Paths after sorting them... ===== End =====");
        }
        /* ===== Logs End ===== */

        for (int i = 0; i < numberOfPaths; i++) {
            grammar_.parse(symbols[paths[i][0]], symbols[paths[i][1]]);
        }

        String previousState;
        do {
            previousState = this.toString();
            for (int i = 0; i < numberOfPaths; i++) {
                grammar_.parse(symbols[paths[i][0]], symbols[paths[i][1]]);
            }
        } while (! previousState.equals(this.toString()));

        for (Symbol symbol : symbols) {
            List<List<Symbol>> children = symbol.getChildren();
            for (List<Symbol> samePositionChildrenList : children) {

                Symbol[] samePositionChildrenArray = new Symbol[samePositionChildrenList.size()];
                for (int i = 0; i < samePositionChildrenArray.length; i++) {
                    samePositionChildrenArray[i] = samePositionChildrenList.get(i);
                }

                parseRecursively(samePositionChildrenArray);

                samePositionChildrenList.clear();
                for (Symbol child : samePositionChildrenArray) {
                    if (child.getParent() == symbol) {
                        samePositionChildrenList.add(child);
                    }
                }
            }
        }
    }

    /**
     * @brief Processes a pair of symbols to check if there is another symbol between them
     *
     * @param symbols
     *     All the symbols that are currently being processed
     * @param symbol1
     *     The first symbol to be processed
     * @param index1
     *     The position of the first symbol into symbols
     * @param symbol2
     *     The second symbol to be processed
     * @param index2
     *     The position of the second symbol into symbols
     *
     * @return The paths that result from pre-processing
     */
    private int[][] processPath (Symbol[] symbols, Symbol symbol1, int index1,
                                 Symbol symbol2, int index2) {
        Trace connectionLine = new Trace();
        Point[] closestPoints = TraceGroup.closestPoints(symbol1.getTraceGroup(),
            symbol2.getTraceGroup());
        connectionLine.add(closestPoints[0]);
        connectionLine.add(closestPoints[1]);

        for (int i = 0; i < symbols.length; i++) {
            if (i == index1 || i == index2) {
                continue;
            }

            for (int j = 0; j < symbols[i].getTraceGroup().size(); j++) {
                if (Trace.areOverlapped(connectionLine, symbols[i].getTraceGroup().get(j))) {
                    int[] path1 = symbols[i].getTraceGroup().getTopLeftCorner().x_ <
                                     symbol1.getTraceGroup().getTopLeftCorner().x_ ?
                        new int[] {i, index1} : new int[] {index1, i};
                    int[] path2 = symbols[i].getTraceGroup().getTopLeftCorner().x_ <
                                     symbol2.getTraceGroup().getTopLeftCorner().x_ ?
                        new int[] {i, index2} : new int[] {index2, i};
                    return (new int[][] {path1, path2});
                }
            }
        }

        return (new int[][] {{index1, index2}});
    }

    /**
     * @brief Returns the equation created by the parsed Symbols
     *
     * @return The equation created by the parsed Symbols
     */
    @Override
    public String getEquation () {
        if (calculateEquation_) {
            buildEquation();
        }

        return equation_;
    }

    /**
     * @brief Builds the equation based upon the current state of the Symbols
     */
    @Override
    public synchronized void buildEquation () {
        if (! calculateEquation_) {
            return;
        }

        if (symbols_ != null && symbols_.length > 0) {
            equation_ = symbols_[0].buildExpression();

            for (int i = 0; i < symbols_.length - 1; i++) {
                if (symbols_[i].getNextSymbol() != null) {
                    if (symbols_[i].getNextSymbol().getParent() == null) {
                        equation_ += symbols_[i].getNextSymbol().buildExpression();
                    }
                }
            }
        }
        else {
            equation_ = "";
        }

        calculateEquation_ = false;
    }

    /**
     * @brief Resets this Parser
     *        Resetting a Parser should bring it to the state just after it was instantiated
     */
    @Override
    public synchronized void reset () {
        symbols_ = null;

        calculateEquation_ = true;
    }

    /**
     * @brief Getter method for the Symbols of this Parser
     *
     * @return The Symbols of this Parser
     */
    @Override
    public synchronized Symbol[] getSymbols () {
        return symbols_;
    }

    /**
     * @interface Grammar
     * @brief Deinfos the API that a Symbol grammar should provide
     */
    public interface Grammar {
        /**
         * @param primary
         *     The first Symbol
         * @param secondary
         *     The second Symbol
         *
         * @brief Parses two Symbols defining the relationship between them
         */
        void parse (Symbol primary, Symbol secondary);

    }

    private String equation_; //!< The equation of this GrammarParser
    private boolean calculateEquation_; //!< Flag indicating that the equation should be rebuild

    protected final Grammar grammar_; //!< The grammar of this GrammarParser

}
