package miniJava.CodeGenerator;

import mJAM.Machine.Reg;

public class RunTimeAddress {
	public RunTimeAddress(Reg reg, Integer offset) {
		this.reg = reg;
		this.offset = offset;
	}
	public Reg reg;
	public Integer offset;
}
