import javax.imageio.IIOException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class HackAssembler {
    public static int A_INSTRUCTION = 0;
    public static int C_INSTRUCTION = 1;
    public static int L_INSTRUCTION = 2;

    public static void main(String[] args) throws IOException {
        //initialize all the objects withe the given file name;
        String filename=args[0];
        int dot=-1;
        for (int i = 0; i <filename.length() ; i++) {
            if(filename.charAt(i)=='.'){
                dot=i;
                break;
            }
        }
        String new_file_name=filename.substring(0,dot)+'.'+"hack";
        Parser parser = new Parser(filename);
        Code coder = new Code();
        SymbolTable table = new SymbolTable();

        firstun(parser,table);
        secRun(parser,coder,table,new_file_name);

        }


        

    public static void firstun(Parser parser,SymbolTable table) throws IOException{
        parser.rest();
        int instruction_number =0;
        while (parser.hasMoreLines()) {
            parser.advance();
            if (parser.instructionType() == L_INSTRUCTION) {
                String symbol = parser.symbol();
                if (!table.contains(symbol)) {
                    table.addentry(symbol, instruction_number);
                }
            }
            if(parser.instructionType()!=L_INSTRUCTION){
                instruction_number++;
            }
        }
    }

    public static void secRun(Parser parser,Code coder,SymbolTable table,String new_file_name)throws IOException{
        RandomAccessFile rf = new RandomAccessFile(new_file_name, "rw");
        rf.seek(0);
        parser.rest();
        int memory_location = 16;
        String instruction = "";

        while (parser.hasMoreLines()) {
            parser.advance();
            if (parser.instructionType() == C_INSTRUCTION) {
                instruction = "111" + coder.comp(parser.comp()) + coder.dest(parser.dest()) + coder.jump(parser.jump()) + "\n";
                rf.writeBytes(instruction);
            }if (parser.instructionType() == A_INSTRUCTION) {
                String symbol = parser.symbol();
                if (symbol.charAt(0) < '0' || symbol.charAt(0) > '9') {
                    if (!table.contains(symbol)) {
                        table.addentry(symbol, memory_location);
                        memory_location++;
                    }

                    instruction = dec_To_16Bin(table.getAddress(symbol)) + "\n";
                    rf.writeBytes(instruction);
                } else {
                    instruction = dec_To_16Bin(Integer.parseInt(symbol)) + "\n";
                    rf.writeBytes(instruction);
                }
            }
        }
        rf.close();
    }


    public static String dec_To_16Bin(int num){
        String ret=Integer.toBinaryString(num);
        while(ret.length()<16){
            ret="0"+ret;
        }
        return ret;
    }
}