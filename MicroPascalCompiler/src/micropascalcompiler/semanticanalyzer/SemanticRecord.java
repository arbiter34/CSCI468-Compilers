/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micropascalcompiler.semanticanalyzer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author arbiter34
 */
public class SemanticRecord {
    
    private SRType type;
    
    private ArrayList<String> data;
    
    public SemanticRecord(SRType type, String... data) {
        this.type = type;
        this.data = new ArrayList<>();
        this.data.addAll(Arrays.asList(data));
    }
}
