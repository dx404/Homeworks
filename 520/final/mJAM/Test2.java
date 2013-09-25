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

		Machine.emit(Op.LOADL, 1);
		Machine.emit(Op.JUMPIF, 1, Reg.CB, 4);
		Machine.emit(Op.LOADL, 0);
		Machine.emit(Prim.or);
		Machine.emit(Op.LOADL, 1);
		
		Machine.emit(Op.HALT,4,0,0);       // halt
		
		Machine.emit(Op.HALT,4,0,0);       // halt
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
