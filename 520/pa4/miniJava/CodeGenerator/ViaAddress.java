package miniJava.CodeGenerator;

import mJAM.Machine.Reg;

public class ViaAddress extends RunTimeEntity{
	public ViaAddress(RunTimeAddress address, Integer size) {
		super (address, size);
	}
	
	public ViaAddress(Reg reg, Integer offset, Integer size){
		super (new RunTimeAddress(reg, offset), size);
	}
}
