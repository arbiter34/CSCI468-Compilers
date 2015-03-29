/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micropascalcompiler.labelmaker;

/**
 *
 * @author arbiter34@gmail.com
 */
public class LabelMaker {
    private static int currentLabelNumber = 1;
    
    public static String getCurrentLabel() {
        return "L" + currentLabelNumber;
    }
    
    public static String getNextLabel() {
        return "L" + ++currentLabelNumber;
    }
}
