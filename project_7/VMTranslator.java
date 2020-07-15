import java.io.IOException;

public class VMTranslator {
    public static void main(String args[])throws IOException {
        String filename=args[0];
        int i=filename.length()-1;
        while(filename.charAt(i)!='.'){
            i--;
        }
        filename=filename.substring(0,i);
        Parser parser=new Parser(args[0]);
        CodeWriter codeWriter=new CodeWriter(filename);
        String arg1="";
        int arg2=0;

        while (parser.hasMoreLines()){
            parser.advance();
            int commandType=parser.commandType();
            if (commandType==-1){
                break;
            }
            arg1=parser.arg1();
            if(commandType==2){
                codeWriter.writeArithmetic(arg1);
            }else {
                arg2=parser.arg2();
                codeWriter.writePushPop(commandType,arg1,arg2);
            }
        }
        codeWriter.close();
    }
}
