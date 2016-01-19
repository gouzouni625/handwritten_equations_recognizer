package org.hwer.engine.parsers.grammars;

import java.io.PrintStream;

import org.hwer.engine.parsers.symbols.Symbol;

/** @class Grammar
 *
 *  @brief Implements an abstract Grammar.
 *
 *  A Grammar should be able to parse two Symbol objects. This class is used as a general guide for
 *  for other Grammar classes.
 */
public abstract class Grammar{
  /**
   *  @brief Parses two Symbol objects.
   *
   *  @param symbol1 The first Symbol.
   *  @param symbol2 The second Symbol.
   */
  public abstract void parse(Symbol symbol1, Symbol symbol2);

  /**
   *  @brief Setter method for the quiet mode for this Grammar.
   *
   *  @param quiet The new value for quiet mode.
   */
  public void setQuiet(boolean quiet){
    quiet_ = quiet;
  }

  /**
   *  @brief Getter method for the quiet mode.
   *
   *  @return Returns true if quiet mode is on for this Grammar.
   */
  public boolean isQuiet(){
    return quiet_;
  }

  /**
   *  @brief Setter method for the output stream.
   *
   *  This is the stream where the log messages are send.
   *
   *  @param outputStream The new output stream.
   */
  public void setOutputStream(PrintStream outputStream){
    outputStream_ = outputStream;
  }

  /**
   *  @brief Getter method for the output stream.
   *
   *  This is the stream where the log messages are send.
   *
   *  @return Returns the output stream.
   */
  public PrintStream getOutputStream(){
    return outputStream_;
  }

  protected boolean quiet_ = true; //!< Quiet mode flag for this Grammar.

  protected PrintStream outputStream_ = System.out; //!< The stream for logs.

}
