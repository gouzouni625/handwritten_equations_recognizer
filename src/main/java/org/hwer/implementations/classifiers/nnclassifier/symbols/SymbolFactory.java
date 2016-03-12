package org.hwer.implementations.classifiers.nnclassifier.symbols;

import org.hwer.engine.parsers.symbols.Symbol;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * @class SymbolFactory
 * @brief A class used to create Symbol objects.
 * <p>
 * This class also contains all the needed variables and methods to make the transition of data between the equation
 * partitioner and the parser.
 */
public class SymbolFactory {

    // Symbol Factory is a Singleton. Make sure it cannot be instantiated.
    private SymbolFactory(){}


    public static SymbolFactory getInstance(){
        return instance_;
    }

    public void registerSymbol(Class<?> clazz){
        symbolClasses_.add(clazz);
    }

    /**
     * @param traceGroup The TraceGroup of the Symbol to be created.
     * @param label      The label of the Symbol to be created.
     * @return Returns the created Symbol.
     * @brief Creates a Symbol object for a given label.
     * <p>
     * This label is the label used by the equation partitioner and the neural network, thus an integer.
     */
    public Symbol createByLabel (TraceGroup traceGroup, int label) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> clazz = symbolClasses_.get(label);

        return (Symbol) clazz.getConstructor(TraceGroup.class).newInstance(traceGroup);
    }

    ArrayList<Class<?>> symbolClasses_ = new ArrayList<Class<?>>();

    private static SymbolFactory instance_ = new SymbolFactory();

}
