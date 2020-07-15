import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class JackCompiler {

    public static void main(String args[]) throws IOException {
        File file = new File(args[0]);
        if (!file.isDirectory()) {
            String filename=file.getPath();
            int index= filename.lastIndexOf('.');
            filename=filename.substring(0,index);
            JackTokenizer jackTokenizer = new JackTokenizer(file);
            RandomAccessFile rf = new RandomAccessFile(filename+".vm", "rw");
            VMWriter out = new VMWriter(rf);
            SymbolTable st = new SymbolTable();
            CompilationEngine compilationEngine = new CompilationEngine(jackTokenizer, out,st);
            if (jackTokenizer.hasMoreTokens()) {
                jackTokenizer.advance();
                compilationEngine.CompileClass();
            }
            rf.close();
        }

        else {
            SymbolTable st;
            for (File curfile:file.listFiles()) {
                String filename=curfile.getPath();
                int index=filename.lastIndexOf('.');
                String extention=filename.substring(index+1);
                if(extention.equals("jack")){
                    filename=filename.substring(0,index);
                    JackTokenizer jackTokenizer = new JackTokenizer(curfile);
                    RandomAccessFile rf = new RandomAccessFile(filename+".vm", "rw");
                    VMWriter out = new VMWriter(rf);
                    st = new SymbolTable();
                    CompilationEngine compilationEngine = new CompilationEngine(jackTokenizer, out,st);
                    if (jackTokenizer.hasMoreTokens()) {
                        jackTokenizer.advance();
                        compilationEngine.CompileClass();

                    }
                    rf.close();
                }
            }
        }
    }
}