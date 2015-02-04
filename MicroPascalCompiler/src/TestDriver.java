/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import micropascalcompiler.*;
import fsa.*;
/**
 *
 * @author arbiter34
 */
public class TestDriver {
    public static void main (String args[]) {
        System.out.println("Test Run");
        Scanner s = new Scanner("test.txt");
        s.run();
    }
}
