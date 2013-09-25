package miniJava.CodeGenerator;

public class PatchLine {
	public int line;
	public int classIndex;
	public int info;

	public PatchLine(int line, int classIndex, int info){
		this.line = line;
		this.classIndex = classIndex;
		this.info = info;
	}
}
