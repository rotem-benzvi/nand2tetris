import javax.imageio.IIOException;
import java.io.*;

public class Parser {
    RandomAccessFile raf;
    String current_instruction="";
    long length;

    public Parser(String fileName) throws IOException {
        this.raf = new RandomAccessFile(fileName, "r");
        this.length = this.raf.length();
        this.current_instruction = "";
    }

    /**
     * checks if the given file has more kines to read
     * @return true is it has
     * @throws IOException
     */
    public boolean hasMoreLines() throws IOException {
        return (this.raf.getFilePointer() != length);
    }

    /**
     * return string of the current insrtuction
     * @return
     */
    public String getCurrent_instruction() {
        return this.current_instruction;
    }

    /**
     * set the random access pointer to the begining of the file
     * @throws IOException
     */
    public void rest() throws IOException {
        raf.seek(0);
        this.current_instruction = "";
    }


    /**
     * advance the file to the next asm instruction (skip spaces)
     * set the current_instruction field to be that instruction.
     * if reaeach end of file set the current_instruction to null.
     *
     * @throws IOException
     */
    public void advance() throws IOException {
        this.current_instruction="";
        boolean read = true;
        int c = ' ';
        //skip comments and white spaces
        while (read) {
            c = raf.read();
            if (c == '/') {
                raf.readLine();
            } else if (c == -1 || !Character.isWhitespace(c)) {
                read = false;
            }
        }
        //set the current_instruction field.
        if (c != -1) {
            this.current_instruction = "" + (char) c;
            current_instruction += raf.readLine();
        }
    }

    /**
     * return the type of the current_instruction
     *
     * @return 0 for A_instruction;  1 for C_instruction ;2 for L_instruction
     */
    public int instructionType() {
        if (current_instruction!="") {

            if (this.current_instruction.charAt(0) == '@') {
                return 0;
            }
            if (this.current_instruction.charAt(0) == '(') {
                return 2;
            }

            return 1;
        }
        return -1;
    }

    /**
     * parse symbol from A and L instructions
     *
     * @return string which represents the symbol
     */
    public String symbol() {
        String symbol = "";
        char c = this.current_instruction.charAt(0);
        int i = 1;
        if (c == '(') {
            while ((c = this.current_instruction.charAt(i)) != ')') {
                symbol += c;
                i++;
            }
        } else {
            while (i <= (this.current_instruction.length() - 1) && !Character.isWhitespace(c = this.current_instruction.charAt(i))) {
                symbol += c;
                i++;
            }
        }
        return symbol;
    }

    /**
     * extract the comp part from a given C_instruction
     * @return String which represents the comp part of a given instruction
     */
    public String comp() {
        int instruction_length = this.current_instruction.length();
        int even_sign = 0;
        int semiColon_sign = instruction_length;
        int i = 0;
        while (i < instruction_length) {
            if (this.current_instruction.charAt(i) == '=') {
                even_sign = i+1;
            }
            if (this.current_instruction.charAt(i) == ';'|| Character.isWhitespace(this.current_instruction.charAt(i))||this.current_instruction.charAt(i) == '/') {
                semiColon_sign = i;
                break;

            }
            i++;
        }
        return this.current_instruction.substring(even_sign, semiColon_sign);
    }

    /**
     * extract the dest part from a given C_instruction
     * @return String which represents the dest part of a given instruction
     */
    public String dest(){
        int even_sign=-1;
        int instruction_length = this.current_instruction.length();
        int i=0;
        while(i<instruction_length){
            if (this.current_instruction.charAt(i) == '=') {
                even_sign = i;
                break;
            }
            i++;
        }
        if(even_sign==-1){
            return "null";
        }
        return this.current_instruction.substring(0,even_sign);
    }

    /**
     * extract the jump part from a given C_instruction
     * @return String which represents the jump part of a given instruction
     */
    public String jump(){
        int semicolon_sign=-1;
        int instruction_length = this.current_instruction.length();
        int end=instruction_length;
        int i=0;
        while(i<instruction_length){
            if (this.current_instruction.charAt(i) == ';') {
                semicolon_sign = i;

            }
            if (Character.isWhitespace(this.current_instruction.charAt(i))||this.current_instruction.charAt(i) == '/') {
                end = i;
                break;
            }
            i++;
        }
        if(semicolon_sign==-1){
            return "null";
        }
        return this.current_instruction.substring(semicolon_sign+1,end);
    }
}

