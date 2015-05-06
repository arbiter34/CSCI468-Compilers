/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micropascalcompiler.symboltable;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author arbiter34
 */
public class SymbolTable {
	private LinkedHashMap<String, SymbolTableRecord> symbolTable;

	private int nestingLevel;

	private String label;

	private String scopeName;

	private long tableSize;

	private int parameterCount;

	private int variableCount;

	public SymbolTable(String scopeName, String label, int nestingLevel) {
		symbolTable = new LinkedHashMap<>();
		this.nestingLevel = nestingLevel;
		this.label = label;
		this.scopeName = scopeName;
		this.tableSize = 0;
		this.variableCount = 0;
		this.parameterCount = 0;
	}

    public LinkedHashMap<String, SymbolTableRecord> getSymbolTable() {
        return symbolTable;
    }

	public void print() {
		System.out.println("\n\nScope Name    Nesting Level    Label");
		System.out.println(this.scopeName + "           " + this.nestingLevel
				+ "            " + this.label);
		System.out
				.println("\nLexeme   Type    Kind    Mode    Size    Parameters    Offset");
		for (String key : symbolTable.keySet()) {
			SymbolTableRecord r = symbolTable.get(key);
			int numParams = r.getParameters() == null ? 0 : r.getParameters()
					.size();
			System.out.println(r.getLexeme() + "\t" + r.getType() + "\t"
					+ r.getKind() + "\t" + r.getMode() + "\t" + r.getSize()
					+ "\t" + numParams + "\t" + r.getOffset());
		}
		System.out.print("\n\n");
	}

	public void insert(SymbolTableRecord rec) {

		rec.setOffset(this.tableSize);
		long recDataSize;
		if (rec.getKind() == RecordKind.VARIABLE) {
			recDataSize = DataSize.size[rec.getType().ordinal()];
			if (rec.getMode() == null) {
				this.variableCount++;
			} else {
				this.parameterCount++;
			}
		} else if (rec.getKind() == RecordKind.FUNCTION
				|| rec.getKind() == RecordKind.PROCEDURE) {
			this.variableCount++;
			recDataSize = DataSize.size[4];
		} else if (rec.getKind() == RecordKind.REG_OR_RA) {
			recDataSize = 1;
		} else {
			recDataSize = 0;
		}
		this.tableSize += recDataSize;
		symbolTable.put(rec.getLexeme(), rec);
	}

	public String getScopeName() {
		return scopeName;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName.toLowerCase();
	}

	public boolean exists(String key) {
		return this.symbolTable.containsKey(key.toLowerCase());
	}

	public int getNestingLevel() {
		return nestingLevel;
	}

	public String getLabel() {
		return label;
	}

	public SymbolTableRecord getRecord(String key) {
		return this.symbolTable.get(key.toLowerCase());
	}

	public RecordKind getRecordKind(String key) {
		return this.symbolTable.get(key.toLowerCase()).getKind();
	}

	public RecordType getRecordType(String key) {
		return this.symbolTable.get(key.toLowerCase()).getType();
	}

	public String getRecordLexeme(String key) {
		return this.symbolTable.get(key.toLowerCase()).getLexeme();
	}

	public RecordMode getRecordMode(String key) {
		return this.symbolTable.get(key.toLowerCase()).getMode();
	}

	public int getRecordSize(String key) {
		return this.symbolTable.get(key.toLowerCase()).getSize();
	}

	public ArrayList<RecordParameter> getRecordParameters(String key) {
		return this.symbolTable.get(key.toLowerCase()).getParameters();
	}

	public long getRecordOffset(String key) {
		return this.symbolTable.get(key.toLowerCase()).getOffset();
	}

	public int getVariableCount() {
		return this.variableCount;
	}

	public int getParameterCount() {
		return this.parameterCount;
	}

	public int getSize() {
		int size = 0;
		for (String key : symbolTable.keySet()) {
			size++;
		}
		return size;
	}
}
