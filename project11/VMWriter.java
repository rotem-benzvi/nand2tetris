import java.io.IOException;
import java.io.RandomAccessFile;

public class VMWriter {
    RandomAccessFile out;
    public static final int CONST=27;
    public static final int ARG=28;
    public static final int LOCAL=29;
    public static final int STATIC=15;
    public static final int THIS=26;
    public static final int THAT=32;
    public static final int POINTER=33;
    public static final int TEMP=34;
    public static final int ADD=35;
    public static final int SUB=36;
    public static final int NEG=37;
    public static final int EQ=38;
    public static final int GT=39;
    public static final int LT=40;
    public static final int AND=41;
    public static final int OR=42;
    public static final int NOT=43;

    public VMWriter (RandomAccessFile out) throws IOException {
        this.out = out;
    }

    public void writePush(int segment, int index) throws IOException{
        switch (segment) {
            case CONST:
                out.writeBytes("push constant " + Integer.toString(index) +"\n");
                break;
            case ARG:
                out.writeBytes("push argument " + Integer.toString(index) +"\n");
                break;
            case LOCAL:
                out.writeBytes("push local " + Integer.toString(index) +"\n");
                break;
            case STATIC:
                out.writeBytes("push static " + Integer.toString(index) +"\n");
                break;
            case THIS:
                out.writeBytes("push this " + Integer.toString(index) +"\n");
                break;
            case THAT:
                out.writeBytes("push that " + Integer.toString(index) +"\n");
                break;
            case POINTER:
                out.writeBytes("push pointer " + Integer.toString(index) +"\n");
                break;
            case TEMP:
                out.writeBytes("push temp " + Integer.toString(index) +"\n");
                break;
        }
    }

    public void writePop(int segment, int index) throws IOException{
        switch (segment) {
            case CONST:
                out.writeBytes("pop constant " + Integer.toString(index) +"\n");
                break;
            case ARG:
                out.writeBytes("pop argument " + Integer.toString(index) +"\n");
                break;
            case LOCAL:
                out.writeBytes("pop local " + Integer.toString(index) +"\n");
                break;
            case STATIC:
                out.writeBytes("pop static " + Integer.toString(index) +"\n");
                break;
            case THIS:
                out.writeBytes("pop this " + Integer.toString(index) +"\n");
                break;
            case THAT:
                out.writeBytes("pop that " + Integer.toString(index) +"\n");
                break;
            case POINTER:
                out.writeBytes("pop pointer " + Integer.toString(index) +"\n");
                break;
            case TEMP:
                out.writeBytes("pop temp " + Integer.toString(index) +"\n");
                break;
        }
    }

    public void writeArithmetic(int command) throws IOException{
        switch (command) {
            case ADD:
                out.writeBytes("add\n");
                break;
            case SUB:
                out.writeBytes("sub\n");
                break;
            case NEG:
                out.writeBytes("neg\n");
                break;
            case EQ:
                out.writeBytes("eq\n");
                break;
            case GT:
                out.writeBytes("gt\n");
                break;
            case LT:
                out.writeBytes("lt\n");
                break;
            case AND:
                out.writeBytes("and\n");
                break;
            case OR:
                out.writeBytes("or\n");
                break;
            case NOT:
                out.writeBytes("not\n");
                break;
        }
    }
    public void writeLabel(String label) throws IOException{
        out.writeBytes("label " + label + "\n");
    }
    public void writeGoto(String label) throws IOException{
        out.writeBytes("goto " + label + "\n");

    }
    public void writeIf(String label) throws IOException{
        out.writeBytes("if-goto " + label + "\n");
    }
    public void writeCall(String name, int nArgs) throws IOException{
        out.writeBytes("call " + name + " " + Integer.toString(nArgs) + "\n");
    }
    public void writeFunction(String name, int nLocals) throws IOException{
        out.writeBytes("function " + name + " " + Integer.toString(nLocals) + "\n");
    }
    public void writeReturn() throws IOException{
        out.writeBytes("return\n");
    }
}
