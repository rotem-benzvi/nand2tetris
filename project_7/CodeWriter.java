import javax.imageio.IIOException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class CodeWriter {
    public static final int C_PUSH = 0;
    public static final int C_POP = 1;
    RandomAccessFile rf;
    String filename;
    public static int label = 0;


    public CodeWriter(String filename) throws IOException {
        this.rf = new RandomAccessFile(filename + ".asm", "rw");
        int index = filename.lastIndexOf('/') + 1;
        if (index == -1) {
            this.filename = filename;
        }
        else {
            this.filename = filename.substring(index);
        }

    }

    public void writeArithmetic(String command) throws IOException {
        if (command.equals("add")) {
            //SP--
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M-1\n");
            //D=*SP
            rf.writeBytes("A=M\n");
            rf.writeBytes("D=M\n");
            //SP--
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M-1\n");
            //*SP=*SP+D
            rf.writeBytes("A=M\n");
            rf.writeBytes("D=M+D\n");
            rf.writeBytes("@SP\n");
            rf.writeBytes("A=M\n");
            rf.writeBytes("M=D\n");
            //SP++
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M+1\n");
            return;
        }
        if (command.equals("sub")) {
            //SP--
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M-1\n");
            //D=*SP
            rf.writeBytes("A=M\n");
            rf.writeBytes("D=M\n");
            //SP--
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M-1\n");
            //*SP=*SP-D
            rf.writeBytes("A=M\n");
            rf.writeBytes("D=M-D\n");
            rf.writeBytes("@SP\n");
            rf.writeBytes("A=M\n");
            rf.writeBytes("M=D\n");
            //SP++
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M+1\n");
            return;
        }
        if (command.equals("neg")) {
            //SP--
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M-1\n");
            //D=*SP
            rf.writeBytes("A=M\n");
            rf.writeBytes("D=M\n");
            //*SP=!D
            rf.writeBytes("@SP\n");
            rf.writeBytes("A=M\n");
            rf.writeBytes("M=-D\n");
            //SP++
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M+1\n");
            return;
        }
        if (command.equals("eq")) {
            //SP--
            this.writeArithmetic("sub");
            //SP--
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M-1\n");
            //D=*SP
            rf.writeBytes("A=M\n");
            rf.writeBytes("D=M\n");
            //set *sp to 1
            rf.writeBytes("@SP\n");
            rf.writeBytes("A=M\n");
            rf.writeBytes("M=-1\n");
            //check if D==0, jump to the lable if it does
            rf.writeBytes("@lt."+label+"\n");
            rf.writeBytes("D;JEQ\n");
            //D!=0-> *SP=false
            rf.writeBytes("@SP\n");
            rf.writeBytes("A=M\n");
            rf.writeBytes("M=0\n");
            rf.writeBytes("(lt."+label+")\n");
            label++;
            //SP++
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M+1\n");
        }
        
        if (command.equals("gt")) {
            this.writeArithmetic("sub");
            //SP--
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M-1\n");
            //D=*SP
            rf.writeBytes("A=M\n");
            rf.writeBytes("D=M\n");
            //set *sp to 1
            rf.writeBytes("@SP\n");
            rf.writeBytes("A=M\n");
            rf.writeBytes("M=-1\n");
            //check if D>0, jump to the lable if it does
            rf.writeBytes("@gt."+label+"\n");
            rf.writeBytes("D;JGT\n");
            //D!=0-> *SP=false
            rf.writeBytes("@SP\n");
            rf.writeBytes("A=M\n");
            rf.writeBytes("M=0\n");
            rf.writeBytes("(gt."+label+")\n");
            label++;
//            //SP++
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M+1\n");
        }
        if (command.equals("lt")) {
            this.writeArithmetic("sub");
            //SP--
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M-1\n");
            //D=*SP
            rf.writeBytes("A=M\n");
            rf.writeBytes("D=M\n");
            //set *sp to 1
            rf.writeBytes("@SP\n");
            rf.writeBytes("A=M\n");
            rf.writeBytes("M=-1\n");
            //check if D<0, jump to the lable if it does
            rf.writeBytes("@lt."+label+"\n");
            rf.writeBytes("D;JLT\n");
            //D!=0-> *SP=false
            rf.writeBytes("@SP\n");
            rf.writeBytes("A=M\n");
            rf.writeBytes("M=0\n");
            rf.writeBytes("(lt."+label+")\n");
            label++;
            //SP++
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M+1\n");
        }
        if (command.equals("and")) {
            //SP--
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M-1\n");
            //D=*SP
            rf.writeBytes("A=M\n");
            rf.writeBytes("D=M\n");
            //SP--
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M-1\n");
            //*SP=*SP&D
            rf.writeBytes("A=M\n");
            rf.writeBytes("D=M&D\n");
            rf.writeBytes("@SP\n");
            rf.writeBytes("A=M\n");
            rf.writeBytes("M=D\n");
            //SP++
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M+1\n");
            return;
        }
        if (command.equals("or")) {
            //SP--
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M-1\n");
            //D=*SP
            rf.writeBytes("A=M\n");
            rf.writeBytes("D=M\n");
            //SP--
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M-1\n");
            //*SP=*SP|D
            rf.writeBytes("A=M\n");
            rf.writeBytes("D=M|D\n");
            rf.writeBytes("@SP\n");
            rf.writeBytes("A=M\n");
            rf.writeBytes("M=D\n");
            //SP++
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M+1\n");
            return;
        }
        if (command.equals("not")) {
            //SP--
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M-1\n");
            //D=*SP
            rf.writeBytes("A=M\n");
            rf.writeBytes("D=!M\n");
            //*SP=!D
            rf.writeBytes("@SP\n");
            rf.writeBytes("A=M\n");
            rf.writeBytes("M=D\n");
            //SP++
            rf.writeBytes("@SP\n");
            rf.writeBytes("M=M+1\n");
            return;
        }
    }

    public void writePushPop ( int command, String segment,int index) throws IOException{
        if (command == C_PUSH) {
            if (segment.equals("local")) {
                //addr = LCL + i
                rf.writeBytes("@" + Integer.toString(index) + "\n");
                rf.writeBytes("D=A\n");
                rf.writeBytes("@LCL\n");
                rf.writeBytes("D=M+D\n");
                //D = *addr
                rf.writeBytes("A=D\n");
                rf.writeBytes("D=M\n");
                // *SP = *addr
                rf.writeBytes("@SP\n");
                rf.writeBytes("A=M\n");
                rf.writeBytes("M=D\n");
                //SP++
                rf.writeBytes("@SP\n");
                rf.writeBytes("M=M+1\n");
            }
            if (segment.equals("argument")) {
                //addr = ARG + i
                rf.writeBytes("@" + Integer.toString(index) + "\n");
                rf.writeBytes("D=A\n");
                rf.writeBytes("@ARG\n");
                rf.writeBytes("D=M+D\n");
                //D = *addr
                rf.writeBytes("A=D\n");
                rf.writeBytes("D=M\n");
                // *SP = *addr
                rf.writeBytes("@SP\n");
                rf.writeBytes("A=M\n");
                rf.writeBytes("M=D\n");
                //SP++
                rf.writeBytes("@SP\n");
                rf.writeBytes("M=M+1\n");
            }
            if (segment.equals("this")) {
                //addr = THIS + i
                rf.writeBytes("@" + Integer.toString(index) + "\n");
                rf.writeBytes("D=A\n");
                rf.writeBytes("@THIS\n");
                rf.writeBytes("D=M+D\n");
                //D = *addr
                rf.writeBytes("A=D\n");
                rf.writeBytes("D=M\n");
                // *SP = *addr
                rf.writeBytes("@SP\n");
                rf.writeBytes("A=M\n");
                rf.writeBytes("M=D\n");
                //SP++
                rf.writeBytes("@SP\n");
                rf.writeBytes("M=M+1\n");
            }
            if (segment.equals("that")) {
                //addr = THAT + i
                rf.writeBytes("@" + Integer.toString(index) + "\n");
                rf.writeBytes("D=A\n");
                rf.writeBytes("@THAT\n");
                rf.writeBytes("D=M+D\n");
                //D = *addr
                rf.writeBytes("A=D\n");
                rf.writeBytes("D=M\n");
                // *SP = *addr
                rf.writeBytes("@SP\n");
                rf.writeBytes("A=M\n");
                rf.writeBytes("M=D\n");
                //SP++
                rf.writeBytes("@SP\n");
                rf.writeBytes("M=M+1\n");
            }
            if (segment.equals("constant")) {
                rf.writeBytes("@" + Integer.toString(index) + "\n");
                rf.writeBytes("D=A\n");
                //D=constant
                rf.writeBytes("@SP\n");
                rf.writeBytes("A=M\n");
                rf.writeBytes("M=D\n");
                //SP++
                rf.writeBytes("@SP\n");
                rf.writeBytes("M=M+1\n");
            }
            if (segment.equals("static")) {
                //addr = filename.i
                rf.writeBytes("@" +filename+"."+Integer.toString(index)+ "\n");
                //D = *addr
                rf.writeBytes("D=M\n");
                // *SP = *addr
                rf.writeBytes("@SP\n");
                rf.writeBytes("A=M\n");
                rf.writeBytes("M=D\n");
                //SP++
                rf.writeBytes("@SP\n");
                rf.writeBytes("M=M+1\n");
            }
            if (segment.equals("temp")) {
                //addr = 5 + i
                rf.writeBytes("@" + Integer.toString(index) + "\n");
                rf.writeBytes("D=A\n");
                rf.writeBytes("@5\n");
                rf.writeBytes("D=D+A\n");
                //D = *addr
                rf.writeBytes("A=D\n");
                rf.writeBytes("D=M\n");
                // *SP = *addr
                rf.writeBytes("@SP\n");
                rf.writeBytes("A=M\n");
                rf.writeBytes("M=D\n");
                //SP++
                rf.writeBytes("@SP\n");
                rf.writeBytes("M=M+1\n");
            }
            if(segment.equals("pointer")){
                if(index==0){
                    //D=This
                    rf.writeBytes("@THIS\n");
                }if(index==1){
                    rf.writeBytes("@THAT\n");
                }
                //D=THIS/THAT
                rf.writeBytes("D=M\n");
                // *SP =THIS/THAT
                rf.writeBytes("@SP\n");
                rf.writeBytes("A=M\n");
                rf.writeBytes("M=D\n");
                //SP++
                rf.writeBytes("@SP\n");
                rf.writeBytes("M=M+1\n");
            }
        }

        if (command == C_POP) {
            if (segment.equals("local")) {
                //addr = LCL + i
                rf.writeBytes("@"+ Integer.toString(index) + "\n");
                rf.writeBytes("D=A\n");
                rf.writeBytes("@LCL\n");
                rf.writeBytes("D=M+D\n");
                //store addr in R13
                rf.writeBytes("@13\n");
                rf.writeBytes("M=D\n");
                //SP--
                rf.writeBytes("@SP\n");
                rf.writeBytes("M=M-1\n");
                //D = *SP
                rf.writeBytes("@SP\n");
                rf.writeBytes("A=M\n");
                rf.writeBytes("D=M\n");
                // *addr = *SP
                rf.writeBytes("@13\n");
                rf.writeBytes("A=M\n");
                rf.writeBytes("M=D\n");
            }
            else if (segment.equals("argument")) {
                //addr = ARG + i
                rf.writeBytes("@"+ Integer.toString(index) + "\n");
                rf.writeBytes("D=A\n");
                rf.writeBytes("@ARG\n");
                rf.writeBytes("D=M+D\n");
                //store addr in R13
                rf.writeBytes("@13\n");
                rf.writeBytes("M=D\n");
                //SP--
                rf.writeBytes("@SP\n");
                rf.writeBytes("M=M-1\n");
                //D = *SP
                rf.writeBytes("@SP\n");
                rf.writeBytes("A=M\n");
                rf.writeBytes("D=M\n");
                // *addr = *SP
                rf.writeBytes("@13\n");
                rf.writeBytes("A=M\n");
                rf.writeBytes("M=D\n");
            }
            else if (segment.equals("this")) {
                //addr = THIS + i
                rf.writeBytes("@"+ Integer.toString(index) + "\n");
                rf.writeBytes("D=A\n");
                rf.writeBytes("@THIS\n");
                rf.writeBytes("D=M+D\n");
                //store addr in R13
                rf.writeBytes("@13\n");
                rf.writeBytes("M=D\n");
                //SP--
                rf.writeBytes("@SP\n");
                rf.writeBytes("M=M-1\n");
                //D = *SP
                rf.writeBytes("@SP\n");
                rf.writeBytes("A=M\n");
                rf.writeBytes("D=M\n");
                // *addr = *SP
                rf.writeBytes("@13\n");
                rf.writeBytes("A=M\n");
                rf.writeBytes("M=D\n");
            }
            else if (segment.equals("that")) {
                //addr = THAT + i
                rf.writeBytes("@"+ Integer.toString(index) + "\n");
                rf.writeBytes("D=A\n");
                rf.writeBytes("@THAT\n");
                rf.writeBytes("D=M+D\n");
                //store addr in R13
                rf.writeBytes("@13\n");
                rf.writeBytes("M=D\n");
                //SP--
                rf.writeBytes("@SP\n");
                rf.writeBytes("M=M-1\n");
                //D = *SP
                rf.writeBytes("@SP\n");
                rf.writeBytes("A=M\n");
                rf.writeBytes("D=M\n");
                // *addr = *SP
                rf.writeBytes("@13\n");
                rf.writeBytes("A=M\n");
                rf.writeBytes("M=D\n");
            }
            else if (segment.equals("static")) {
                //SP--
                rf.writeBytes("@SP\n");
                rf.writeBytes("M=M-1\n");
                //D = *SP
                rf.writeBytes("@SP\n");
                rf.writeBytes("A=M\n");
                rf.writeBytes("D=M\n");
                //*static+i = *SP
                rf.writeBytes("@" + filename +"."+ Integer.toString(index) + "\n");
                rf.writeBytes("M=D\n");
            }
            else if (segment.equals("temp")) {
                //addr = 5 + i
                rf.writeBytes("@"+ Integer.toString(index) + "\n");
                rf.writeBytes("D=A\n");
                rf.writeBytes("D=D+1\n");
                rf.writeBytes("D=D+1\n");
                rf.writeBytes("D=D+1\n");
                rf.writeBytes("D=D+1\n");
                rf.writeBytes("D=D+1\n");
                //store addr in R13
                rf.writeBytes("@13\n");
                rf.writeBytes("M=D\n");
                //SP--
                rf.writeBytes("@SP\n");
                rf.writeBytes("M=M-1\n");
                //D = *SP
                rf.writeBytes("@SP\n");
                rf.writeBytes("A=M\n");
                rf.writeBytes("D=M\n");
                // *addr = *SP
                rf.writeBytes("@13\n");
                rf.writeBytes("A=M\n");
                rf.writeBytes("M=D\n");
            }
            else if (segment.equals("pointer")) {
                //SP--
                rf.writeBytes("@SP\n");
                rf.writeBytes("M=M-1\n");
                //D = *SP
                rf.writeBytes("@SP\n");
                rf.writeBytes("A=M\n");
                rf.writeBytes("D=M\n");
                //*THIS//*THAT = *SP
                if (index == 0) {
                    //*THIS = *SP
                    rf.writeBytes("@THIS\n");
                    //rf.writeBytes("A=M\n");
                    rf.writeBytes("M=D\n");
                }
                else {
                    //*THAT = *SP
                    rf.writeBytes("@THAT\n");
                    //rf.writeBytes("A=M\n");
                    rf.writeBytes("M=D\n");
                }
            }
        }
    }
    public void close()throws IOException{
        this.rf.close();
    }

}

