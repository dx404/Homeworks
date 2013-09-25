/**
 * Example illustrating components of mJAM package
 * @author prins
 * @version COMP 520 V2.2
 */
package mJAM;
import mJAM.Machine.Op;
import mJAM.Machine.Reg;
import mJAM.Machine.Prim;

// test class to construct and run an mJAM program
public class Test2
{
	public static void main(String[] args){
		
		Machine.initCodeGen();
		System.out.println("Duo --Generating test program object code from MySelf");
		
		Machine.emit(Op.JUMP, Reg.CB, 8);
		Machine.emit(Op.LOADL, 10001);
		Machine.emit(Op.LOADL, 10002);
		Machine.emit(Op.LOADL, 10003);
		Machine.emit(Op.LOAD, Reg.LB, -2);
		Machine.emit(Op.HALT, 4, 0, 0);
		Machine.emit(Prim.add);
		Machine.emit(Op.RETURN, 1, 0, 0);
		
		for (int i = 0; i < 10; i++){
			Machine.emit(Op.LOADL, i + 100);
		}
		Machine.emit(Op.LOADL, -1); //why loadL static link
		Machine.emit(Op.HALT,4,0,0);
		Machine.emit(Op.LOADA, Reg.CB, 1);
		Machine.emit(Op.HALT,4,0,0);
		Machine.emit(Op.CALLI);
		Machine.emit(Op.LOADL, 65537);
		Machine.emit(Op.LOADL,5);
		Machine.emit(Prim.newarr);
		Machine.emit(Op.LOADL,3);
		Machine.emit(Prim.newarr);
		Machine.emit(Op.LOAD, Reg.SB, 12);
		Machine.emit(Op.LOADL, 2); //index
		Machine.emit(Op.LOADL, -12345);
		Machine.emit(Prim.arrayupd);
		Machine.emit(Op.LOAD, Reg.SB, 12);
		Machine.emit(Op.LOADL, 2);
		Machine.emit(Prim.arrayref);
		Machine.emit(Op.LOADA, Reg.HT, 6);
		Machine.emit(Op.HALT,4,0,0);
		Machine.emit(Op.STOREI);
		
		Machine.emit(Op.LOADA, Reg.SB, 2);
		Machine.emit(Op.LOADL, 5);
		Machine.emit(Prim.newobj);
		Machine.emit(Op.LOADL, 27599);
		Machine.emit(Prim.putint);
		//Machine.emit(Prim.putint);
		Machine.emit(Op.HALT,4,0,0);
		Machine.emit(Op.HALT,0,0,0);       // halt

		/* write code as an object file */
		String objectCodeFileName = "test2.mJAM";
		ObjectFile objF = new ObjectFile(objectCodeFileName);
		System.out.print("Writing object code file " + objectCodeFileName + " ... ");
		if (objF.write()) {
			System.out.println("FAILED!");
			return;
		}
		else
			System.out.println("SUCCEEDED");	
		
		/* create asm file using disassembler */
		System.out.print("Writing assembly file ... ");
		Disassembler d = new Disassembler(objectCodeFileName);
		if (d.disassemble()) {
			System.out.println("FAILED!");
			return;
		}
		else
			System.out.println("SUCCEEDED");
		
		/* run code */
		System.out.println("Running code ... ");
		Interpreter.interpret(objectCodeFileName);

		System.out.println("*** mJAM execution completed");
	}
}
