package miniJava.CodeGenerator;

import mJAM.Machine.Reg;

public class ViaValue extends RunTimeEntity{
	public ViaValue(RunTimeAddress addr, Integer size) {
		super(addr, size);
	}
	public ViaValue(RunTimeAddress addr, Integer size, Integer value) {
		super(addr, size);
		this.value = value;
	}
	
	public ViaValue(Reg reg, Integer offset, Integer size, Integer value){
		super (new RunTimeAddress(reg, offset), size);
		this.value = value;
	}

	Integer value;
}
