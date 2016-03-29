package org.hwer.engine.utilities;


import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;


/**
 * @class Utilities
 * @brief Contains general methods for Arrays, ArrayLists and data
 */
public class Utilities {
    /**
     * @brief Returns the sorted in ascending order indices of a given array
     *
     * @param array
     *     The array
     *
     * @return The sorted in ascending order indices of the given array
     */
    public static int[] sortArray (double[] array) {
        int length = array.length;
        double[] arrayClone = new double[length];

        System.arraycopy(array, 0, arrayClone, 0, length);

        int[] indices = new int[length];
        for (int i = 0; i < length; i++) {
            indices[i] = i;
        }

        for (int i = 0; i < length; i++) {
            for (int j = 1; j < length - i; j++) {
                if (arrayClone[j - 1] > arrayClone[j]) {
                    double temp = arrayClone[j - 1];
                    arrayClone[j - 1] = arrayClone[j];
                    arrayClone[j] = temp;

                    int tempIndex = indices[j - 1];
                    indices[j - 1] = indices[j];
                    indices[j] = tempIndex;
                }
            }
        }

        return indices;
    }

    /**
     * @brief Returns true if a given array contains a given value
     *
     * @param array
     *     The array
     * @param value
     *     The value to be search within the array
     *
     * @return True if a given array contains a given value
     */
    public static boolean arrayContains (int[] array, int value) {
        for (int arrayValue : array) {
            if (arrayValue == value) {
                return true;
            }
        }

        return false;
    }

    /**
     * @brief Returns the context indices of the given indices based on the connections between them
     *        A graph is composed by vertices and edges. Edges connect vertices. Given the
     *        connections of a graph and an array of vertices, this method will return the vertices
     *        that are connected to the given vertices on the given graph. Each one of the returned
     *        vertices will be connected to, at least, one of the given vertices. A usage example
     *        would be:
     *        @code
     *          boolean[][] connections = new boolean[][] {
     *              {true , false, false, true },
     *              {false, true , true , false},
     *              {false, true , true , false},
     *              {true , false, false, true }
     *          };
     *          int[] vertices = new int[] {0, 1};
     *          int[] context = Utilities.getContext(vertices, connections);
     *        @endcode
     *        In this example there are four vertices. Vertex 0 is connected with vertex 3 and
     *        vertex 1 is connected with vertex 2. the context of vertices 0, 1 is 2, 3.
     *
     * @param vertices
     *     The vertices the context of which should be found
     * @param connections
     *     The connections between the vertices
     *
     * @return The context indices of the given indices based on the connections between them
     */
    public static int[] getContext (int[] vertices, boolean[][] connections) {
        HashSet<Integer> uniqueContext = new HashSet<Integer>();
        for (int vertex : vertices) {
            for (int j = 0; j < connections.length; j++) {
                // Don't add the given vertices to context.
                if (Utilities.arrayContains(vertices, j)) {
                    continue;
                }
                if (connections[vertex][j]) {
                    uniqueContext.add(j);
                }
            }
        }

        // Convert the hash set to an array of integers.
        int[] context = new int[uniqueContext.size()];
        int index = 0;
        for (Integer anUniqueContext : uniqueContext) {
            context[index] = anUniqueContext;
            index++;
        }

        return context;
    }

    /**
     * @brief Returns the context indices of the given indices based on the connections between them
     *
     * @param vertices
     *     The vertices the context of which should be found
     * @param connections
     *     The connections between the vertices
     *
     * @return The context indices of the given indices based on the connections between them
     *
     * @sa getContext
     */
    public static int[] getContext (ArrayList<Integer> vertices, boolean[][] connections) {
        int length = vertices.size();
        int[] verticesInt = new int[length];

        for (int i = 0; i < length; i++) {
            verticesInt[i] = vertices.get(i);
        }

        return (Utilities.getContext(verticesInt, connections));
    }

    /**
     * @brief Returns unique paths of given length on a graph
     *        The paths can be forced to comply with some checks.
     *
     * @param connections
     *     The connections of the indices of the graph
     * @param maxPathLength
     *     The maximum length of the paths
     * @param checks
     *     The checks that every path must comply with in order to be chosen
     *
     * @return The unique paths of given length on the graph
     */
    public static int[][] findUniquePaths (boolean[][] connections, int maxPathLength,
                                           PathExtensionCheck... checks) {
        Hashtable<Integer, ArrayList<Integer>> hashTable = new Hashtable<Integer, ArrayList<Integer>>();
        int hashTableOldSize;

        // Add every single vertex to the hash table.
        for (int vertex = 0; vertex < connections.length; vertex++) {
            ArrayList<Integer> path = new ArrayList<Integer>();
            path.add(vertex);

            hashTable.put(pathHashKey(path), path);
        }

        int currentPathLength = 1;
        if (maxPathLength > currentPathLength) {
            do {
                Iterator<ArrayList<Integer>> existingPathsIterator = hashTable.values().iterator();

                // Find all possible new paths(even duplicates).
                ArrayList<ArrayList<Integer>> newPaths = new ArrayList<ArrayList<Integer>>();
                while (existingPathsIterator.hasNext()) {
                    ArrayList<Integer> currentPath = existingPathsIterator.next();

                    int[] context = Utilities.getContext(currentPath, connections);

                    for (int contextVertex : context) {
                        ArrayList<Integer> currentPathClone = new ArrayList<Integer>(currentPath);
                        currentPathClone.add(contextVertex);

                        boolean checksPassed = true;
                        for (PathExtensionCheck check : checks) {
                            if (! check.check(currentPathClone)) {
                                checksPassed = false;
                                break;
                            }
                        }

                        if (checksPassed) {
                            newPaths.add(currentPathClone);
                        }
                    }
                }

                hashTableOldSize = hashTable.size();

                // Add new paths to the hash table to eliminate duplicates.
                for (ArrayList<Integer> newPath : newPaths) {
                    hashTable.put(Utilities.pathHashKey(newPath), newPath);
                }

                currentPathLength++;
            } while (hashTableOldSize != hashTable.size() && currentPathLength < maxPathLength);
        }

        // Convert the hash table to an array of arrays of integers.
        int[][] uniquePaths = new int[hashTable.size()][];
        int uniquePathsIndex = 0;

        for (ArrayList<Integer> path : hashTable.values()) {
            int pathSize = path.size();

            uniquePaths[uniquePathsIndex] = new int[pathSize];
            for (int vertex = 0; vertex < pathSize; vertex++) {
                uniquePaths[uniquePathsIndex][vertex] = path.get(vertex);
            }

            uniquePathsIndex++;
        }

        return uniquePaths;
    }

    /**
     * @interface PathExtensionCheck
     * @brief Interface that can be implemented to create checks for findUniquePaths method
     *
     * @sa findUniquePaths
     */
    public interface PathExtensionCheck {
        /**
         *  @brief The check that will be performed to decide whether the given path is valid or not
         *
         *  @param path
         *      The path that should be checked for validity
         *
         *  @return True if the path is valid
         */
        boolean check(ArrayList<Integer> path);

    }


    /**
     * @brief Returns the unique hash key of the given path
     *
     * @param path
     *     The path
     *
     * @return The unique hash key of the given path
     */
    public static int pathHashKey (int[] path) {
        int key = 0;

        for (int vertex : path) {
            key ^= (int) (Math.pow(2, vertex));
        }

        return key;
    }

    /**
     * @brief Returns the unique hash key of the given path
     *
     * @param path
     *     The path
     *
     * @return The unique hash key of the given path
     *
     * @sa pathHashKey
     */
    public static int pathHashKey (ArrayList<Integer> path) {
        int numberOfVertices = path.size();

        int[] array = new int[numberOfVertices];
        for (int i = 0; i < numberOfVertices; i++) {
            array[i] = path.get(i);
        }

        return pathHashKey(array);
    }

    /**
     * @brief Returns the position of the maximum value inside an array
     *
     * @param array
     *     The array
     *
     * @return The position of the maximum value inside the given array
     */
    public static int indexOfMax (double[] array) {
        if (array.length == 0) {
            return - 1;
        }

        double maxValue = array[0];
        int maxValueIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxValue = array[i];
                maxValueIndex = i;
            }
        }

        return maxValueIndex;
    }

    /**
     * @brief Returns the maximum value of a given array
     *
     * @param array
     *     The array
     *
     * @return The maximum value of the given array
     */
    public static double maxValue (double[] array) {
        return (array[Utilities.indexOfMax(array)]);
    }

    /**
     * @brief Normalizes the values of a given array
     *        The values of the given array are mapped to [0, 100]. Each value is divided by the sum
     *        of the initial values and multiplied by 100. The initial values should be greater than
     *        zero.
     *
     * @param array
     *     The array to be normalized
     *
     * @return An array with the normalized values of the given array
     */
    public static double[] normalizeArray (double[] array) {
        int length = array.length;

        double sum = 0;
        for (double item : array) {
            sum += item;
        }

        double[] normalizedArray = new double[length];
        for (int i = 0; i < length; i++) {
            normalizedArray[i] = array[i] / sum * 100;
        }

        return normalizedArray;
    }

    /**
     * @brief Transforms an index to its corresponding upper triangular indices
     *        Let A = {
     *          {11, 12, 13, 14},
     *          {21, 22, 23, 24},
     *          {31, 32, 33, 34},
     *          {41, 42, 43, 44}
     *        }
     *        be an array and b = {12, 13, 14, 23, 24, 34} be the row-major representation of the
     *        upper triangular part of A, excluding the main diagonal. This method will translate
     *        indices on vector b, to indices on matrix A.
     *
     * @param numberOfRows
     *     The number of rows of the matrix
     * @param index
     *     The index on the row-major vector
     *
     * @return The corresponding row and column indices on the matrix
     *
     * @sa upperTriangularIndicesToVectorIndex
     */
    public static int[] vectorIndexToUpperTriangularIndices (int numberOfRows, int index) {
        int rowIndex = 0;
        int columnIndex;

        while (index >= numberOfRows - 1 - rowIndex) {
            index -= numberOfRows - 1 - rowIndex;
            rowIndex++;
        }
        columnIndex = rowIndex + 1 + index;

        return (new int[] {rowIndex, columnIndex});
    }

    /**
     * @brief Transforms upper triangular indices to their corresponding row-major index
     *        Let A = {
     *          {11, 12, 13, 14},
     *          {21, 22, 23, 24},
     *          {31, 32, 33, 34},
     *          {41, 42, 43, 44}
     *        }
     *        be an array and b = {12, 13, 14, 23, 24, 34} be the row-major representation of the
     *        upper triangular part of A, excluding the main diagonal. This method will translate
     *        indices on matrix A, to indices on vector b.
     *
     * @param numberOfRows
     *     The number of rows of the matrix
     * @param row
     *     The row index on the upper triangular matrix
     * @param column
     *     The column index on the upper triangular matrix
     *
     * @return The corresponding index on the row-major vector.
     *
     * @sa vectorIndexToUpperTriangularIndices
     */
    public static int upperTriangularIndicesToVectorIndex (int numberOfRows, int row, int column) {
        int index = 0;
        for (int i = 0; i < row; i++) {
            index += numberOfRows - 1 - i;
        }
        index += column;

        return index;
    }

    /**
     * @brief Returns true if all the values of an array are true
     *
     * @param array
     *     The array
     *
     * @return True if all the values of the given array are true
     */
    public static boolean areAllTrue (boolean[][] array) {
        for (boolean[] row : array) {
            for (boolean item : row) {
                if (! item) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * @brief Concatenates two arrays
     *        The The second array is appended to the first.
     *
     * @param array1
     *     The first array
     * @param array2
     *     The array to be appended
     *
     * @return The concatenated array
     */
    public static int[][] concatenateArrays (int[][] array1, int[][] array2) {
        int[][] array = new int[array1.length + array2.length][];

        for (int i = 0; i < array1.length; i++) {
            System.arraycopy(array1[i], 0, array[i], 0, array1[i].length);
        }

        for (int i = 0; i < array2.length; i++) {
            System.arraycopy(array2[i], 0, array[i], array1.length, array2[i].length);
        }

        return array;
    }

    /**
     * @brief Returns a copy of a given array with the specified rows removed
     *
     * @param array
     *     The array
     * @param rowsIndices
     *     The rows to be removed
     *
     * @return A copy of the given array with the specified rows removed
     */
    public static int[][] removeRows (int[][] array, Collection<Integer> rowsIndices) {
        int newLength = array.length - rowsIndices.size();
        int[][] newArray = new int[newLength][];

        ArrayList<Integer> listOfRows = new ArrayList<Integer>(rowsIndices);
        Collections.sort(listOfRows);

        int currentRowInNewArray = 0;
        int currentIndexInArray = 0;
        for (int i = 0; i < array.length; i++) {
            if (currentIndexInArray < listOfRows.size() &&
                i == listOfRows.get(currentIndexInArray)) {
                currentIndexInArray++;
                continue;
            }

            newArray[currentRowInNewArray] = array[i];
            currentRowInNewArray++;
        }

        return newArray;
    }

    /**
     * @brief Returns true if a row exists inside an array
     *
     * @param array
     *     The array
     * @param row
     *     The row
     * @param maintainSequence
     *     Flag to determine whether the order of the objects in the row should matter
     *
     * @return True of the row exists inside the array
     *
     * @todo Check hash equality between rows instead of checking each row element
     */
    public static boolean rowInArray (int[][] array, int[] row, boolean maintainSequence) {
        for (int[] arrayRow : array) {
            if (arrayRow.length == row.length) {

                int[] arrayRowClone = new int[arrayRow.length];
                System.arraycopy(arrayRow, 0, arrayRowClone, 0, arrayRow.length);

                int[] rowClone = new int[row.length];
                System.arraycopy(row, 0, rowClone, 0, row.length);

                if (! maintainSequence) {
                    Arrays.sort(arrayRowClone);
                    Arrays.sort(rowClone);
                }

                int counter = 0;
                for (int j = 0; j < rowClone.length; j++) {
                    if (arrayRowClone[j] == rowClone[j]) {
                        counter++;
                    }
                }

                if (counter == row.length) {
                    return true;
                }
            }
        }

        return false;
    }

}
