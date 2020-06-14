package smartContracts.analysis;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;

public class DeadCodeChecker extends MethodVisitor {

	String owner;
	MethodVisitor next;
	
	public DeadCodeChecker(String owner, int access, String name, String desc, MethodVisitor methodVisitor) {
		
		super(Opcodes.ASM8, new MethodNode(access, name, desc, null, null));
	
		this.owner = owner;
		this.next = methodVisitor;
		
	}
	
	@Override
	public void visitEnd() {
		
		MethodNode mn = (MethodNode) mv;
		
		Analyzer<BasicValue> a = new Analyzer<BasicValue>(new BasicInterpreter());
		
		try {
			
			a.analyze(owner, mn);
			Frame<BasicValue>[] frames = a.getFrames();
			AbstractInsnNode[] insns = mn.instructions.toArray();
		
			int deadCodeIntructions = 0;
			
			for (int i = 0; i < frames.length; ++i) {
				
				if (frames[i] == null && !(insns[i] instanceof LabelNode)) {
				
					deadCodeIntructions++;
					
				}
			
			}
			
			if(deadCodeIntructions >= ( frames.length/2 ) ) {
				
				throw new Exception("Two Many Dead Code Lines in the Contract!!!");
				
			}
		
		}
		catch (AnalyzerException ignored) {
			
			
			
		}
		catch (Exception ignored) {
		
			
			
		}
		
		mn.accept(next);
		
	}
	
}