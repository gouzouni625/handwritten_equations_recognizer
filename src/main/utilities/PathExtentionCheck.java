package main.utilities;

import java.util.ArrayList;

/** @interface PathExtentionCheck
 *
 *  @brief Interface that can be implmented to create checks for findUniquePaths method of Utilities class.
 */
public interface PathExtentionCheck{
  /**
   *  @brief The check that will be performed to decide whether the given path is valid or not.
   *
   *  @param list The path that should be checked for validity.
   *
   *  @return Returns true if the path is valid.
   */
  public boolean check(ArrayList<Integer> list);

}
