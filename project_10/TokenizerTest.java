import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Hashtable;

public class TokenizerTest {
    static int KEYWORD=1;
    static int SYMBOL=2;
    static int IDENTIFIER=3;
    static int INT_CONST=4;
    static int STRING_CONST=5;


    static int CLASS=6;
    static int METHOD=7;
    static int FUNCTION=8;
    static int CONSTRUCTOR=9;
    static int INT=10;
    static int BOOLEAN=11;
    static int CHAR=12;
    static int VOID=13;
    static int VAR=14;
    static int STATIC=15;
    static int FIELD=16;
    static int LET=17;
    static int DO=18;
    static int IF=19;
    static int ELSE=20;
    static int WHILE=21;
    static int RETURN=22;
    static int TRUE=23;
    static int FALSE=24;
    static int NULL=25;
    static int THIS=26;

    public static void main(String args[])throws IOException {

        Hashtable<Integer,String> tokentypeSet=new Hashtable <>();
        tokentypeSet.put(KEYWORD,"keyword");
        tokentypeSet.put(SYMBOL,"symbol");
        tokentypeSet.put(IDENTIFIER,"identifier");
        tokentypeSet.put(INT_CONST,"integerConstant");
        tokentypeSet.put(STRING_CONST,"stringConstant");

        Hashtable<Integer,String> keywordSet=new Hashtable<>();
        keywordSet.put(CLASS,"class");
        keywordSet.put(CONSTRUCTOR,"constructor");
        keywordSet.put(FUNCTION,"function");
        keywordSet.put(METHOD,"method");
        keywordSet.put(FIELD,"field");
        keywordSet.put(STATIC,"static");
        keywordSet.put(VAR,"var");
        keywordSet.put(CLASS,"class");
        keywordSet.put(INT,"int");
        keywordSet.put(CHAR,"char");
        keywordSet.put(BOOLEAN,"boolean");
        keywordSet.put(VOID,"void");
        keywordSet.put(TRUE,"true");
        keywordSet.put(FALSE,"false");
        keywordSet.put(NULL,"null");
        keywordSet.put(THIS,"this");
        keywordSet.put(THIS,"let");
        keywordSet.put(DO,"do");
        keywordSet.put(IF,"if");
        keywordSet.put(ELSE,"else");
        keywordSet.put(WHILE,"while");
        keywordSet.put(RETURN,"return");
        keywordSet.put(LET,"let");
        File file = new File("test.jack");
        JackTokenizer tknzr=new JackTokenizer(file);
        RandomAccessFile rf = new RandomAccessFile("outPut.xml","rw");
        String line="";
        int curTokenType;
        rf.writeBytes("<tokenes>\n");
        while (tknzr.hasMoreTokens()){
            tknzr.advance();
            curTokenType=tknzr.tokenType();
            line="<"+tokentypeSet.get(curTokenType)+"> ";
            if(curTokenType==KEYWORD){
                line+=keywordSet.get(tknzr.keyWord());
            }if(curTokenType==SYMBOL){
               line+=tknzr.symbol();
            }if(curTokenType==IDENTIFIER){
                line+=tknzr.identifier();
            }if(curTokenType==INT_CONST){
                line+=tknzr.intVal();
            }if(curTokenType==STRING_CONST){
                line+=tknzr.stringVal();
            }
           line+=" </"+tokentypeSet.get(curTokenType)+">";
            rf.writeBytes(line+"\n");
        }
        rf.writeBytes("</tokenes>\n");
        }
    }


