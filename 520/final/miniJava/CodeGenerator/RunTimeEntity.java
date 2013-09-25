package miniJava.CodeGenerator;

import mJAM.Machine.Reg;

public class RunTimeEntity{
	public RunTimeAddress address;  // Register, offset
	public Integer size;
	
	public Integer index; //basically for static ordering
	
	public Integer value = 0; //here is primarily for expression
	
	public Integer VarDeclCounter = 0;

	public void setRTE(Reg reg, Integer offset, Integer size){
		this.address.reg = reg; 
		this.address.offset = offset;
		this.size = size;
	}
	
	public RunTimeEntity(RunTimeAddress address, Integer size, Integer index){ 
		//size may always be one
		this.address = address;
		this.size = size;
		this.index = index;
		this.value = 0;
	}
	
	public RunTimeEntity(Reg reg, Integer offset, Integer size, Integer index){
		this.address = new RunTimeAddress(reg, offset);
		this.size = size;
		this.index = index;
		this.value = 0;
	}
	
	public RunTimeEntity(Reg reg, Integer offset){
		this.address = new RunTimeAddress(reg, offset);
		this.size = 1;
		this.index = -1;
		this.value = 0;
	}
	
	public RunTimeEntity(Integer index){
		this.address = null;
		this.index = index;
		this.value = 0;
	}
	
	public void setRTE(Reg reg, Integer offset, Integer size, Integer index){
		this.address.reg = reg; 
		this.address.offset = offset;
		this.size = size;
		this.index = index;
	}
	
	
	public Integer setRTEvalue(Integer value){
		return this.value = value;
	}
	
	public void setVarDeclCounter(Integer n){
		VarDeclCounter = n;
	}
	
	public int getEnd(){
		return address.offset + size;
	}
	
}
