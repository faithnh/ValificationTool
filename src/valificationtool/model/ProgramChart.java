package valificationtool.model;

import java.util.ArrayList;

public class ProgramChart {
	private int x;
	private int y;
	private ArrayList<ProgramChart> next_branch;
	private String program;
	public ProgramChart(){
		this(0,0);
	}
	public ProgramChart(int x, int y){
		this.x = x;
		this.y = y;
		next_branch = new ArrayList<ProgramChart>();
		program = "";
	}
}
