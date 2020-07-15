import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.TreeSet;

public class VMTranslator {
    public static void main(String args[])throws IOException {
        CodeWriter codeWriter;

        File root = new File(args[0]);
        String filename = root.getPath();
        TreeSet<String> all_files = new TreeSet <>();

        if(root.isDirectory()){
            codeWriter = new CodeWriter(filename,true);
            codeWriter.writeBoot();
            File[] sub_file=root.listFiles();
            for (File file : sub_file) {
                int curIndex = file.getName().lastIndexOf(".");
                String extention = file.getName().substring(curIndex+1);
                if(extention.equals("vm")){
                    all_files.add(file.getPath());
                }
            }
        }
        
        else{
            int index = filename.lastIndexOf('.');
            filename=filename.substring(0,index);
            codeWriter = new CodeWriter(filename,false);
            all_files.add(args[0]);
        }

        //This loop iterate all files in directory or one file and write the result into filname.asm
        for (String file_name:all_files) {
            Parser parser=new Parser(file_name);
            String arg1="";
            int arg2=0;

            int indexStart = file_name.lastIndexOf(File.separatorChar) + 1;
            if (indexStart != -1) {
                file_name = file_name.substring(indexStart);
            }
            while (parser.hasMoreLines()){
                parser.advance();
                int commandType=parser.commandType();
                //only return (8) has no arg1
                if (commandType != 8) {
                    arg1=parser.arg1();
                    if(commandType==0 || commandType==1){
                        arg2=parser.arg2();
                        codeWriter.writePushPop(commandType, arg1, arg2,file_name);
                    }
                    else if(commandType==2){
                        codeWriter.writeArithmetic(arg1);
                    }
                    else if(commandType==3) {
                        codeWriter.writeLabel(arg1);
                    }
                    else if(commandType==4) {
                        codeWriter.writeGoto(arg1);
                    }
                    else if(commandType==5) {
                        codeWriter.writeIf(arg1);
                    }
                    else if(commandType==6) {
                        arg2=parser.arg2();
                        codeWriter.writeFunction(arg1,arg2);
                    }
                    else if(commandType==7) {
                        arg2=parser.arg2();
                        codeWriter.writeCall(arg1,arg2);
                    }
                    else{
                        break;
                    }
                }
                else {
                    codeWriter.writeReturn();
                }
            }
        }
        codeWriter.close();
    }
}
