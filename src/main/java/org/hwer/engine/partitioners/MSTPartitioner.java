package org.hwer.engine.partitioners;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;

import org.hwer.engine.classifiers.Classifier;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.Utilities;
import org.hwer.engine.utilities.logging.SingleLineFormatter;
import org.hwer.engine.utilities.math.MinimumSpanningTree;
import org.hwer.engine.utilities.traces.*;
import org.hwer.engine.utilities.Utilities.PathExtensionCheck;


/**
 * @class MSTPartitioner
 * @brief A Partitioner that uses a Minimum Spanning Tree to partition the given equation
 */
public class MSTPartitioner extends Partitioner {
    /**
     * @brief Constructor
     *
     * @param classifier
     *     The classifier of this Partitioner
     */
    public MSTPartitioner (Classifier classifier) {
        super(classifier);

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
     * @brief Partitions a TraceGroup to Symbols
     *
     * @param traceGroup
     *     The TraceGroup to be partitioned
     *
     * @return The Symbols that the given TraceGroup is partitioned to
     */
    @Override
    public Symbol[] partition (TraceGroup traceGroup) throws IllegalArgumentException {
        if (traceGroup == null) {
            return null;
        }

        expression_ = traceGroup;

        SymbolFactory symbolFactory = SymbolFactory.getInstance();

        int numberOfTraces = traceGroup.size();

        if (numberOfTraces == 0) {
            return new Symbol[0];
        }
        else if (numberOfTraces == 1) {
            if (traceGroup.get(0).size() == 1) {
                Symbol symbolObject = null;
                try {
                    symbolObject = symbolFactory.create(Labels.DOT, traceGroup);

                    symbolObject.setConfidence(10000);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return new Symbol[] {symbolObject};
            }
            else {
                /* ===== Logs Start ===== */
                if (logger_.getLevel() != Level.OFF) {
                    logger_.info("Partitioning single size TraceGroup");
                }
                /* ===== Logs End ===== */

                Symbol symbol = classifier_.classify(traceGroup, null, false, false);

                /* ===== Logs Start ===== */
                if (logger_.getLevel() != Level.OFF) {
                    logger_.info("Classified to " + symbol);
                    logger_.info("Confidence " + symbol.getConfidence());
                }
                /* ===== Logs End ===== */

                return (new Symbol[] {symbol});
            }
        }

        double[] distances = this.calculateDistancesBetweenTraces(traceGroup);


        /* ===== Logs Start ===== */
        if (logger_.getLevel() != Level.OFF) {
            logger_.info("Distances between traces... ===== Start =====");

            for (double distance : distances) {
                logger_.info(String.valueOf(distance));
            }

            logger_.info("Distances between traces... ===== End =====");
        }
        /* ===== Logs End ===== */

        MinimumSpanningTree minimumSpanningTree = MinimumSpanningTree.
            kruskal(distances, numberOfTraces);

        /* ===== Logs Start ===== */
        if (logger_.getLevel() != Level.OFF) {
            logger_.info("Minimum Spanning Tree... ===== Start =====");

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < numberOfTraces; i++) {
                for (int j = 0; j < numberOfTraces; j++) {
                    stringBuilder.append(minimumSpanningTree.areConnected(i, j)).append(", ");
                }

                stringBuilder.append("\n");
            }
            logger_.info(stringBuilder.toString());

            logger_.info("Minimum Spanning Tree... ===== End =====");
        }
        /* ===== Logs End ===== */

        int[][] paths = minimumSpanningTree.getUniquePaths(maxTracesInSymbol_);
        int numberOfPaths = paths.length;

        /* ===== Logs Start===== */
        if (logger_.getLevel() != Level.OFF) {
            logger_.info("Paths upon the Minimum Spanning Tree... ===== Start =====");

            for (int i = 0; i < numberOfPaths; i++) {
                logger_.info("path " + i + " = ");

                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < paths[i].length; j++) {
                    stringBuilder.append(paths[i][j]).append(", ");
                }

                logger_.info(stringBuilder.toString());
            }

            logger_.info("Paths upon the Minimum Spanning Tree... ===== End =====");
        }
        /* ===== Logs End ===== */

        int[] dots = findDots(traceGroup);
        int numberOfDots = dots.length;

        HashSet<Integer> pathsToClear = new HashSet<Integer>();
        for (int path = 0; path < numberOfPaths; path++) {
            for (int dot = 0; dot < numberOfDots; dot++) {
                if (Utilities.arrayContains(paths[path], dot) && paths[path].length > 1) {
                    pathsToClear.add(path);
                }
            }
        }
        paths = Utilities.removeRows(paths, pathsToClear);
        numberOfPaths = paths.length;

        int[][] overlaps = Utilities.concatenateArrays(this.findOverlaps(traceGroup),
            this.findEqualsSymbol(traceGroup));

        /* ===== Logs Start ===== */
        if (logger_.getLevel() != Level.OFF) {
            logger_.info("Overlaps... ===== Start =====");

            for (int[] overlap : overlaps) {
                logger_.info(overlap[0] + ", " + overlap[1]);
            }

            logger_.info("Overlaps... ===== End =====");
        }
        /* ===== Logs End ===== */

        pathsToClear.clear();
        boolean foundFirstOverlapIndex;
        boolean foundSecondOverlapIndex;
        boolean clearFlag = false;
        for (int path = 0; path < numberOfPaths; path++) {
            for (int[] overlap : overlaps) {
                foundFirstOverlapIndex = false;
                foundSecondOverlapIndex = false;

                for (int trace = 0; trace < paths[path].length; trace++) {
                    if (paths[path][trace] == overlap[0]) {
                        foundFirstOverlapIndex = true;
                    }

                    if (paths[path][trace] == overlap[1]) {
                        foundSecondOverlapIndex = true;
                    }
                }

                if (foundFirstOverlapIndex != foundSecondOverlapIndex) {
                    clearFlag = true;
                    break;
                }
            }

            if (clearFlag) {
                pathsToClear.add(path);
            }

            clearFlag = false;
        }
        paths = Utilities.removeRows(paths, pathsToClear);
        numberOfPaths = paths.length;

        /* ===== Logs Start ===== */
        if (logger_.getLevel() != Level.OFF) {
            logger_.info("Paths after forcing overlaps... ===== Start =====");

            for (int i = 0; i < numberOfPaths; i++) {
                logger_.info("path " + i + " = ");

                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < paths[i].length; j++) {
                    stringBuilder.append(paths[i][j]).append(", ");
                }

                logger_.info(stringBuilder.toString());
            }

            logger_.info("Paths after forcing overlaps... ===== End =====");
        }
        /* ===== Logs End ===== */

        double[] pathsRates = new double[numberOfPaths];
        Symbol[] pathSymbols = new Symbol[numberOfPaths];

        TraceGroup symbol;

        for (int i = 0; i < numberOfPaths; i++) {
            symbol = traceGroup.subTraceGroup(paths[i]);

            if (paths[i].length == 1 && Utilities.arrayContains(dots, paths[i][0])) {
                Symbol symbolObject = null;
                try {
                    symbolObject = symbolFactory.create(Labels.DOT, symbol);

                    symbolObject.setConfidence(10000);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                pathSymbols[i] = symbolObject;
            }
            else {
                pathSymbols[i] = classifier_.classify(symbol, null, false, false);
            }
            pathsRates[i] = pathSymbols[i].getConfidence();

            /* ===== Logs Start ===== */
            if (logger_.getLevel() != Level.OFF) {
                logger_.info("Path rate and label... ===== Start =====");

                logger_.info("path " + i + " subSymbolCheck: " + false);
                logger_.info("path " + i + " rate: " + pathsRates[i]);
                logger_.info("path " + i + " label: " + pathSymbols[i].getLabel());

                logger_.info("Path rate and label... ===== End =====");
            }
            /* ===== Logs End ===== */
        }

        boolean[][] connections = new boolean[numberOfPaths][numberOfPaths];
        for (int i = 0; i < numberOfPaths; i++) {
            for (int j = i + 1; j < numberOfPaths; j++) {
                connections[i][j] = this.areCombinable(paths[i], paths[j]);
                connections[j][i] = connections[i][j];
            }

            connections[i][i] = false;
        }

        int[][] partitions = Utilities.findUniquePaths(connections, numberOfPaths,
            new PartitionCheck(paths, numberOfTraces));
        int numberOfPartitions = partitions.length;

        /* ===== Logs Start ===== */
        if (logger_.getLevel() != Level.OFF) {
            logger_.info("All partitions... ===== Start =====");

            for (int i = 0; i < numberOfPartitions; i++) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < partitions[i].length; j++) {
                    stringBuilder.append(partitions[i][j]).append(", ");
                }

                logger_.info(stringBuilder.toString());
            }

            logger_.info("All partitions... ===== End =====");
        }
        /* ===== Logs End ===== */

        ArrayList<Integer> partitionsToRemove = new ArrayList<Integer>();
        for (int partition = 0; partition < numberOfPartitions; partition++) {
            if (! this.isPartitionEligible(partitions[partition], paths, numberOfTraces)) {
                partitionsToRemove.add(partition);
            }
        }
        partitions = Utilities.removeRows(partitions, partitionsToRemove);
        numberOfPartitions = partitions.length;

        /* ===== Logs Start ===== */
        if (logger_.getLevel() != Level.OFF) {
            logger_.info("Eligible partitions... ===== Start =====");

            for (int i = 0; i < numberOfPartitions; i++) {
                logger_.info("partition " + i + " = ");

                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < partitions[i].length; j++) {
                    stringBuilder.append(partitions[i][j]).append(", ");
                }

                logger_.info(stringBuilder.toString());
            }

            logger_.info("Eligible partitions... ===== End =====");
        }
        /* ===== Logs End ===== */

        double currentRate = 0;
        for (int path = 0; path < partitions[0].length; path++) {
            currentRate += pathsRates[partitions[0][path]];
        }

        double maxRate = currentRate;
        int bestPartition = 0;

        /* ===== Logs Start ===== */
        if (logger_.getLevel() != Level.OFF) {
            logger_.info("Partition rate... ===== Start =====");
            logger_.info("partition 0 rate: " + currentRate);
            logger_.info("Partition rate... ===== End =====");
        }
        /* ===== Logs End ===== */

        for (int partition = 1; partition < numberOfPartitions; partition++) {
            // Calculate the rate of this partition.
            currentRate = 0;
            for (int path = 0; path < partitions[partition].length; path++) {
                currentRate += pathsRates[partitions[partition][path]];
            }

            /* ===== Logs Start ===== */
            if (logger_.getLevel() != Level.OFF) {
                logger_.info("Partition rate... ===== Start =====");
                logger_.info("partition " + partition + " rate: " + currentRate);
                logger_.info("Partition rate... ===== End =====");
            }
            /* ===== Logs End ===== */

            if (currentRate > maxRate) {
                maxRate = currentRate;
                bestPartition = partition;
            }
        }

        int bestPartitionLength = partitions[bestPartition].length;
        Symbol[] symbols = new Symbol[bestPartitionLength];
        for (int i = 0; i < bestPartitionLength; i++) {
            symbols[i] = pathSymbols[partitions[bestPartition][i]];
        }

        return symbols;
    }

    /**
     * @brief Appends a group of Traces to an existent group of Symbols
     *
     * @param symbols
     *     The Symbols already identified
     * @param newTraces
     *     The group of Traces to be partitioned
     *
     * @return All the Symbols including the identified and the newly identified
     */
    @Override
    public Symbol[] append (Symbol[] symbols, TraceGroup newTraces) {
        if (symbols == null) {
            return partition(newTraces);
        }

        if (symbols.length == 0) {
            return partition(newTraces);
        }

        if (newTraces == null) {
            return symbols;
        }

        if (newTraces.size() == 0) {
            return symbols;
        }

        int numberOfSymbols = symbols.length;
        int numberOfNewTraces = newTraces.size();

        TraceGroup freeTraces = new TraceGroup();

        HashSet<Integer> changedSymbols = new HashSet<Integer>();

        boolean continueFlag = false;
        for (int i = 0; i < numberOfNewTraces; i++) {
            for (int j = 0; j < numberOfSymbols; j++) {
                if (symbols[j].getTraceGroup().isOverlappedBy(newTraces.get(i))) {
                    symbols[j].getTraceGroup().add(newTraces.get(i));
                    changedSymbols.add(j);

                    continueFlag = true;
                    break;
                }
                else {
                    TraceGroup combined = new TraceGroup(
                        symbols[j].getTraceGroup()).add(newTraces.get(i));
                    Symbol symbol = classifier_.classify(combined, null, false, false);

                    if (symbol.getLabel() == Labels.EQUALS &&
                        (
                            symbols[j].getLabel() == Labels.MINUS ||
                            symbols[j].getLabel() == Labels.HORIZONTAL_LINE ||
                            (
                                symbols[j].getLabel() == Labels.FRACTION_LINE &&
                                    ! symbols[j].hasChildren()
                            )
                        )) {
                        symbols[j].getTraceGroup().add(newTraces.get(i));
                        changedSymbols.add(j);

                        continueFlag = true;
                        break;
                    }
                }
            }

            if (continueFlag) {
                continueFlag = false;
            }
            else {
                freeTraces.add(newTraces.get(i));
            }
        }

        for (Integer index : changedSymbols) {
            symbols[index] = classifier_.classify(symbols[index].getTraceGroup(),
                null, false, false);
        }

        Symbol[] newSymbols = partition(freeTraces);
        int numberOfNewSymbols = newSymbols.length;

        Symbol[] allSymbols = new Symbol[numberOfSymbols + numberOfNewSymbols];

        System.arraycopy(symbols, 0, allSymbols, 0, numberOfSymbols);
        System.arraycopy(newSymbols, 0, allSymbols, numberOfSymbols, numberOfNewSymbols);

        for (Symbol symbol : allSymbols) {
            symbol.reset();
        }

        return allSymbols;
    }

    /**
     * @brief Removes a group of Traces from an existent group of Symbols
     *
     * @param symbols
     *     The Symbols already identified
     * @param tracesToBeRemoved
     *     The group of Traces to be removed
     *
     * @return The re-evaluated Symbols after the Traces were removed
     */
    @Override
    public Symbol[] remove (Symbol[] symbols, TraceGroup tracesToBeRemoved) {
        if (symbols == null) {
            return new Symbol[] {};
        }

        if (symbols.length == 0) {
            return symbols;
        }

        if (tracesToBeRemoved == null) {
            return symbols;
        }

        if (tracesToBeRemoved.size() == 0) {
            return symbols;
        }

        for (Symbol symbol : symbols) {
            symbol.reset();
        }

        HashSet<Integer> changedSymbols = new HashSet<Integer>();

        for (int i = 0, n = symbols.length; i < n; i++) {
            for (int j = 0, m = tracesToBeRemoved.size(); j < m; j++) {
                if (symbols[i].getTraceGroup().remove(tracesToBeRemoved.get(j))) {
                    changedSymbols.add(i);
                }
            }
        }

        int numberOfNullSymbols = 0;
        for (Integer index : changedSymbols) {
            if (symbols[index].getTraceGroup().size() != 0) {
                symbols[index] = classifier_.classify(symbols[index].getTraceGroup(),
                    null, false, false);
            }
            else {
                symbols[index] = null;
                numberOfNullSymbols++;
            }
        }

        Symbol[] finalSymbols = new Symbol[symbols.length - numberOfNullSymbols];
        int index = 0;
        for (Symbol symbol : symbols) {
            if (symbol != null) {
                finalSymbols[index] = symbol;
                index++;
            }
        }

        return finalSymbols;
    }

    /**
     * @brief Returns the indices of Traces that are dots
     *
     * @param traceGroup
     *     The group of Traces to check
     *
     * @return The indices of Traces that are dots
     */
    private int[] findDots (TraceGroup traceGroup) {
        ArrayList<Integer> dotIndices = new ArrayList<Integer>();

        for (int i = 0, n = traceGroup.size(); i < n; i++) {
            if (traceGroup.get(i).size() == 1) {
                dotIndices.add(i);
            }
        }

        int numberOfDots = dotIndices.size();
        int[] dotIndicesArray = new int[numberOfDots];
        for (int i = 0; i < numberOfDots; i++) {
            dotIndicesArray[i] = dotIndices.get(i);
        }

        return dotIndicesArray;
    }

    /**
     * @brief Returns true if a partition is eligible
     *        Concretely, this method returns true if and only if a Trace is used only by a single
     *        path in the given partition
     *
     * @param partition
     *     The partition to be checked
     * @param paths
     *     All the paths upon the minimum spanning tree
     * @param numberOfTraces
     *     The number of Traces
     *
     * @return True if a partition is eligible
     */
    private boolean isPartitionEligible (int[] partition, int[][] paths, int numberOfTraces) {
        int[] tracesOccurrenceCounter = new int[numberOfTraces];
        for (int i = 0; i < numberOfTraces; i++) {
            tracesOccurrenceCounter[i] = 0;
        }

        for (int path = 0; path < partition.length; path++) {
            for (int trace = 0; trace < paths[partition[path]].length; trace++) {
                tracesOccurrenceCounter[paths[partition[path]][trace]]++;
            }
        }

        for (int i = 0; i < numberOfTraces; i++) {
            if (tracesOccurrenceCounter[i] != 1) {
                return false;
            }
        }

        return true;
    }

    /**
     * @brief Returns the distances between all the given Traces
     *
     * @param traceGroup
     *     The group of Traces
     *
     * @return The distances between all the given Traces
     */
    private double[] calculateDistancesBetweenTraces (TraceGroup traceGroup) {
        int numberOfTraces = traceGroup.size();

        double[] distances = new double[numberOfTraces * (numberOfTraces - 1) / 2];
        int index = 0;
        for (int i = 0; i < numberOfTraces; i++) {
            for (int j = i + 1; j < numberOfTraces; j++) {
                distances[index] = distanceOfTraces(traceGroup.get(i), traceGroup.get(j));
                index++;
            }
        }

        return distances;
    }

    /**
     * @brief Returns the distance between two Traces
     *        The distance between two Traces is defined as the distance between their centers of
     *        mass.
     *
     * @param trace1
     *     The first Trace
     * @param trace2
     *     The second Trace
     *
     * @return The distance between two Traces
     */
    private double distanceOfTraces (Trace trace1, Trace trace2) {
        if (trace1.size() == 0 || trace2.size() == 0) {
            return - 1;
        }

        Point centerOfMass1 = trace1.getCenterOfMass();
        Point centerOfMass2 = trace2.getCenterOfMass();

        return (Point.distance(centerOfMass1, centerOfMass2));
    }

    /**
     * @brief Returns the pairs of Traces that are overlapped
     *
     * @param traceGroup
     *     The group of Traces
     *
     * @return The pairs of Traces that are overlapped
     */
    private int[][] findOverlaps (TraceGroup traceGroup) {
        int numberOfTraces = traceGroup.size();

        ArrayList<int[]> overlaps = new ArrayList<int[]>();

        for (int i = 0; i < numberOfTraces; i++) {
            for (int j = i + 1; j < numberOfTraces; j++) {
                if (Trace.areOverlapped(traceGroup.get(i), traceGroup.get(j))) {
                    overlaps.add(new int[] {i, j});
                }
            }
        }

        // Convert array list to array.
        int[][] overlapsArray = new int[overlaps.size()][2];
        for (int i = 0; i < overlaps.size(); i++) {
            overlapsArray[i][0] = overlaps.get(i)[0];
            overlapsArray[i][1] = overlaps.get(i)[1];
        }

        return overlapsArray;
    }

    /**
     * @brief Returns the pairs of Traces that create an equals sign
     *
     * @param traceGroup
     *     The group of Traces
     *
     * @return The pairs of Traces that create an equals sign
     */
    private int[][] findEqualsSymbol (TraceGroup traceGroup) {
        int numberOfTraces = traceGroup.size();

        ArrayList<int[]> equals = new ArrayList<int[]>();

        for (int i = 0; i < numberOfTraces; i++) {
            for (int j = i + 1; j < numberOfTraces; j++) {
                if (areEqualsSymbol(traceGroup.get(i), traceGroup.get(j))) {
                    equals.add(new int[] {i, j});
                }
            }
        }

        // Convert array list to array.
        int[][] equalsArray = new int[equals.size()][2];
        for (int i = 0; i < equals.size(); i++) {
            equalsArray[i][0] = equals.get(i)[0];
            equalsArray[i][1] = equals.get(i)[1];
        }

        return equalsArray;
    }

    /**
     * @brief Returns true if the given Traces create an equals sign
     *
     * @param trace1
     *     The first trace
     * @param trace2
     *     The second trace
     *
     * @return True if the given Traces create an equals sign
     */
    private boolean areEqualsSymbol (Trace trace1, Trace trace2) {
        boolean classifierDecision;
        boolean algebraicDecision = false;

        TraceGroup traceGroup = new TraceGroup().add(trace1).add(trace2);

        Symbol symbol = classifier_.classify(traceGroup, null, false, false);

        classifierDecision = ((symbol.getLabel() == Labels.EQUALS) &&
            symbol.getConfidence() > 0.50);

        double trace1Slope = Math.atan(
            (trace1.getOuterRightPoint().y_ - trace1.getOuterLeftPoint().y_) /
                (trace1.getOuterRightPoint().x_ - trace1.getOuterLeftPoint().x_));
        double trace2Slope = Math.atan(
            (trace2.getOuterRightPoint().y_ - trace2.getOuterLeftPoint().y_) /
                (trace2.getOuterRightPoint().x_ - trace2.getOuterLeftPoint().x_));

        if ((trace2.getBottomRightCorner().x_ >= trace1.getTopLeftCorner().x_ &&
            trace2.getTopLeftCorner().x_ <= trace1.getBottomRightCorner().x_) &&
            (trace1.getHeight() <= 0.40 * trace1.getWidth()) &&
            (trace2.getHeight() <= 0.40 * trace2.getWidth()) &&
            (trace1Slope >= - Math.PI / 4 && trace1Slope <= Math.PI / 4) &&
            (trace2Slope >= - Math.PI / 4 && trace2Slope <= Math.PI / 4) &&
            (Trace.minimumDistance(trace1, trace2) <
                Math.min(trace1.getWidth(), trace2.getWidth())) &&
            (Math.abs(trace1.getWidth() - trace2.getWidth()) <
                Math.min(trace1.getWidth(), trace2.getWidth()))) {

            Trace smaller;
            Trace bigger;
            if (trace1.getWidth() > trace2.getWidth()) {
                smaller = trace2;
                bigger = trace1;
            }
            else {
                smaller = trace1;
                bigger = trace2;
            }

            for (int i = 0; i < smaller.size(); i++) {
                Trace connectionLine = new Trace();
                connectionLine.add(new Point(smaller.get(i)));
                connectionLine.add(bigger.closestPoint(smaller.get(i)));

                for (int j = 0; j < expression_.size(); j++) {
                    if (expression_.get(j) == trace1 || expression_.get(j) == trace2) {
                        continue;
                    }

                    if (Trace.areOverlapped(connectionLine, expression_.get(j))) {
                        algebraicDecision = false;
                    }
                }
            }

            algebraicDecision = true;
        }

        return (classifierDecision && algebraicDecision);
    }

    /**
     * @brief Returns true if the given paths are combinable
     *        Two paths are combinable if they don't contain the same Trace
     *
     * @param path1
     *     The first path
     * @param path2
     *     The second path
     *
     * @return True if the given paths are combinable
     */
    private boolean areCombinable (int[] path1, int[] path2) {
        for (int traceIndex1 : path1) {
            for (int traceIndex2 : path2) {
                if (traceIndex1 == traceIndex2) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * @class PartitionCheck
     * @brief Implements PathExtensionCheck to check whether a partition is eligible or not
     */
    private class PartitionCheck implements PathExtensionCheck {
        /**
         * @brief Constructor
         *
         * @param paths
         *     The paths upon the minimum spanning tree
         * @param numberOfTraces
         *     The number of Traces
         */
        public PartitionCheck (int[][] paths, int numberOfTraces) {
            paths_ = paths;
            numberOfTraces_ = numberOfTraces;
        }

        /**
         *  @brief The check that will be performed to decide whether the given path is valid or not
         *
         *  @param path
         *      The path that should be checked for validity
         *
         *  @return True if the path is valid
         */
        public boolean check (ArrayList<Integer> path) {
            int[] tracesOccurrenceCounter = new int[numberOfTraces_];
            for (int i = 0; i < numberOfTraces_; i++) {
                tracesOccurrenceCounter[i] = 0;
            }

            for (Integer traceIndex : path) {
                for (int trace = 0; trace < paths_[traceIndex].length; trace++) {
                    tracesOccurrenceCounter[paths_[traceIndex][trace]]++;
                }
            }

            for (int i = 0; i < numberOfTraces_; i++) {
                if (tracesOccurrenceCounter[i] > 1) {
                    return false;
                }
            }

            return true;
        }

        private int[][] paths_; //!< The paths upon the minimum spanning tree
        private int numberOfTraces_; //!< The number of Traces

    }

    private TraceGroup expression_; //!< The TraceGroup to be partitioned

}
