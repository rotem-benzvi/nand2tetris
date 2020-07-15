import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Hashtable;
import javax.imageio.IIOException;



public class CompilationEngine {
    JackTokenizer tokenaizer;
    String class_name;
    SymbolTable st;
    VMWriter out;
    Hashtable<Integer,String> tokentypeSet;
    Hashtable<Integer,String> keywordSet;
    Hashtable<String,Integer> segmentSet;
    Hashtable<String,Integer> opSet;
    boolean is_constructor;
    boolean is_method;
    int index;
    int param_number;
    int local_naumer;
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

    public static final int CONST=27;
    public static final int ARG=28;
    public static final int LOCAL=29;
    //public static final int STATIC=30;
    //public static final int THIS=31;
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


    public CompilationEngine(JackTokenizer in, VMWriter out,SymbolTable st) throws IOException{
        this.st=st;
        this.tokenaizer = in;
        this.out = out;
        this.tokentypeSet = new Hashtable <>();
        this.is_constructor=false;
        this.is_method = false;
        tokentypeSet.put(KEYWORD,"keyword");
        tokentypeSet.put(SYMBOL,"symbol");
        tokentypeSet.put(IDENTIFIER,"identifier");
        tokentypeSet.put(INT_CONST,"integerConstant");
        tokentypeSet.put(STRING_CONST,"stringConstant");
        this.param_number=0;
        this.local_naumer=0;
        this.keywordSet = new Hashtable<>();
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
        keywordSet.put(DO,"do");
        keywordSet.put(IF,"if");
        keywordSet.put(ELSE,"else");
        keywordSet.put(WHILE,"while");
        keywordSet.put(RETURN,"return");
        keywordSet.put(LET,"let");

        this.segmentSet=new Hashtable <>();
        segmentSet.put("ARG",ARG);
        segmentSet.put("LOCAL",LOCAL);
        segmentSet.put("STATIC",STATIC);
        segmentSet.put("FIELD",THIS);

        this.opSet=new Hashtable <>();
        opSet.put("+",ADD);
        opSet.put("-",SUB);
        opSet.put("=",EQ);
        opSet.put("~",NEG);
        opSet.put(">",GT);
        opSet.put("<",LT);
        opSet.put("&",AND);
        opSet.put("|",OR);
        this.index = 1;
    }

    private void process(String str) throws IOException {
        if (tokenaizer.getCurrentToken().equals(str)) {
            //printXMLToken();
        }
        else {
            System.out.println(tokenaizer.getCurrentToken());
            System.out.println(str);
            System.out.println("syntax error");
        }
        if(tokenaizer.hasMoreTokens()) {
            tokenaizer.advance();
        }
    }

    private void printXMLToken() throws IOException {
        String line="";
        int curTokenType;
        curTokenType=tokenaizer.tokenType();
        line="<"+tokentypeSet.get(curTokenType)+"> ";
        if(curTokenType==KEYWORD){
            line+=keywordSet.get(tokenaizer.keyWord());
        }if(curTokenType==SYMBOL){
            line+=tokenaizer.symbol();
        }if(curTokenType==IDENTIFIER){
            String id_token=tokenaizer.identifier();
            if(st.IndexOf(id_token)>=0){
                line+="name: "+id_token+ " type: "+st.TypeOf(id_token)+" kind: "+st.KindOf(id_token)+" count: "+st.IndexOf(id_token);
            }
            else{line+=id_token;}
        }if(curTokenType==INT_CONST){
            line+=tokenaizer.intVal();

        }if(curTokenType==STRING_CONST){
            line+=tokenaizer.stringVal();
        }
        line+=" </"+tokentypeSet.get(curTokenType)+">";
    }


    public void CompileClass()throws IOException {
        process("class");
        this.class_name=tokenaizer.identifier();
        process(this.class_name);
        process("{");
        while(tokenaizer.getCurrentToken().equals("static")||tokenaizer.getCurrentToken().equals("field")){
            this.CompileClassVarDec();
        }
        while (tokenaizer.getCurrentToken().equals("constructor")||tokenaizer.getCurrentToken().equals("function")||tokenaizer.getCurrentToken().equals("method")){
            this.CompileSubroutineDec();
        }
        process("}");
    }

    public void CompileClassVarDec() throws IOException{
        String temp = keywordSet.get(tokenaizer.keyWord());
        String kind = temp.toUpperCase();
        process(temp);
        String type;
        if(tokenaizer.tokenType()==KEYWORD){
            temp = keywordSet.get(tokenaizer.keyWord());
            type=temp;
            process(temp);
        }else{
            temp=tokenaizer.identifier();
            type=temp;
            process(temp);
        }
        while(!tokenaizer.getCurrentToken().equals(";")) {
            if(tokenaizer.getCurrentToken().equals(",")){
                process(",");
            }
            else {
                String name=tokenaizer.identifier();
                st.define(name,type,kind);
                process(name);
            }
        }
        process(";");
    }

    public void CompileSubroutineDec()throws IOException {
        String fun_name;
        st.startSubruotine();
        this.is_constructor=false;
        boolean is_method = false;
        //('constructor'|'function'|'method')
        String temp = keywordSet.get(tokenaizer.keyWord());
        process(temp);
        if(temp.equals("method")){
            st.define("this",class_name,"ARG");
            this.is_method = true;

        }
        if(temp.equals("constructor")){
            this.is_constructor=true;
        }
        //('void|type')
        Boolean is_void=false;
        if(tokenaizer.tokenType()==KEYWORD){
            temp = keywordSet.get(tokenaizer.keyWord());
            if(temp.equals("void")){
                is_void=true;
            }
            process(temp);
        }else{
            temp=tokenaizer.identifier();
            process(tokenaizer.identifier());
        }
        fun_name=tokenaizer.identifier();
        process(tokenaizer.identifier());
        process("(");
        param_number=0;
        this.CompileParameterList();
        process(")");
        this.CompileSubroutineBody(fun_name);
//        if(is_constructor){
//            out.writePush(POINTER,0);
//        }
        if(is_void){
            out.writePush(CONST,0);
        }
        out.writeReturn();
    }

    public void CompileParameterList()throws IOException {
        boolean secparam=false;
        while (!tokenaizer.getCurrentToken().equals(")")){
            //param_number++;
            if(secparam){
                process(",");
            }
            String type;
            if(tokenaizer.tokenType()==KEYWORD){
                type=keywordSet.get(tokenaizer.keyWord());
                process(type);
            }else{
                type=tokenaizer.identifier();
                process(type);
            }

            String name=tokenaizer.identifier();
            st.define(name,type,"ARG");
            process(name);
            if(!secparam){
                secparam=true;
            }
        }
    }

    public void CompileSubroutineBody(String fun_name)throws IOException {
        process("{");
        //this.index = 0;
        local_naumer=0;
        while(tokenaizer.getCurrentToken().equals("var")){
            this.CompileVarDec();
        }
        out.writeFunction(class_name+"."+fun_name,local_naumer);
        if(is_method){
            out.writePush(ARG, 0);
            out.writePop(POINTER, 0);
        }
        this.is_method = false;
        if(is_constructor){
            out.writePush(CONST,st.varCount("FIELD"));
            out.writeCall("Memory.alloc",1);
            out.writePop(POINTER,0);
        }

        this.CompileStatements();
        process("}");
    }

    public void CompileVarDec() throws IOException{
        local_naumer++;
        process("var");
        String type;
        if(tokenaizer.tokenType()==KEYWORD){
            type=keywordSet.get(tokenaizer.keyWord());
            process(keywordSet.get(tokenaizer.keyWord()));
        }else{
            type=tokenaizer.identifier();
            process(tokenaizer.identifier());
        }
        String name=tokenaizer.identifier();
        st.define(name,type,"LOCAL");
        process(tokenaizer.identifier());
        while(!tokenaizer.getCurrentToken().equals(";")) {
            local_naumer++;
            process(",");
            name=tokenaizer.identifier();
            st.define(name,type,"LOCAL");
            process(tokenaizer.identifier());
        }
        process(";");
    }

    public void CompileStatements()throws IOException {
        //out.write("statements");
        String statement_dec;
        if(tokenaizer.tokenType()==KEYWORD) {
            statement_dec = keywordSet.get(tokenaizer.keyWord());
            while (statement_dec.equals("let") || statement_dec.equals("if") || statement_dec.equals("while") || statement_dec.equals("do") || statement_dec.equals("return")) {
                switch (statement_dec) {
                    case "let":
                        this.CompileLet();
                        break;
                    case "if":
                        this.CompileIf();
                        break;
                    case "while":
                        this.CompileWhile();
                        break;
                    case "do":
                        this.CompileDo();
                        break;
                    case "return":
                        this.CompileReturn();
                        break;
                }
                if(tokenaizer.tokenType()==KEYWORD) {
                    statement_dec = keywordSet.get(tokenaizer.keyWord());
                }else{
                    break;
                }
            }
        }
    }

    public void CompileLet() throws IOException{
        process("let");
        String vareName = tokenaizer.identifier();
        process(vareName);
        boolean ifArr = false;
        if(tokenaizer.getCurrentToken().equals("[")){
            ifArr = true;
            process("[");
            out.writePush(segmentSet.get(st.KindOf(vareName)),st.IndexOf(vareName));
            //this compile calc the offset i [i]
            this.CompileExpression();
            process("]");
            //prepare base+offset
            out.writeArithmetic(VMWriter.ADD);
        }
        process("=");

        //here we prepare the expression
        CompileExpression();

        //if its arr then arr[i] = expression
        if (ifArr) {
            //pop expression to temp
            out.writePop(TEMP,0);
            //pop base+offset to THAT
            out.writePop(POINTER,1);
            //push expression into stack
            out.writePush(TEMP,0);
            //store expression in arr[i]
            out.writePop(THAT,0);
        }
        else {
            out.writePop(segmentSet.get(st.KindOf(vareName)),st.IndexOf(vareName));
        }
        process(";");
    }

    public void CompileIf() throws IOException{
        //out.write("if-statement");
        process("if");
        process("(");
        this.CompileExpression();
        process(")");
        process("{");

        out.writeArithmetic(NOT);
        String L1 = "label" + Integer.toString(index);
        //String L1 = "IF_TRUE" + Integer.toString(index);
        out.writeIf(L1);
        index++;
        this.CompileStatements();
        //String L2 = "IF_FALSE" + Integer.toString(index);
        String L2 = "label" + Integer.toString(index);
        out.writeGoto(L2);
        index++;
        out.writeLabel(L1);
        process("}");
        if(tokenaizer.tokenType()==KEYWORD){
            if(keywordSet.get(tokenaizer.keyWord()).equals("else")){
                process("else");
                process("{");
                //out.writeLabel(L1);
                this.CompileStatements();
                process("}");
//                out.writeLabel(L2);
            }
        }
        out.writeLabel(L2);
    }

    public void CompileWhile() throws IOException{
        //out.write("while-statement");
        process("while");
        process("(");
        //String L1 = "WHILE_TRUE" + Integer.toString(index);
        String L1 = "label" + Integer.toString(index);
        index++;
        out.writeLabel(L1);
        this.CompileExpression();
        process(")");
        out.writeArithmetic(NOT);
        //String L2 = "WHILE_FALSE" + Integer.toString(index);
        String L2 = "label" + Integer.toString(index);
        index++;
        out.writeIf(L2);
        process("{");
        this.CompileStatements();
        process("}");
        out.writeGoto(L1);
        out.writeLabel(L2);
    }

    public void CompileDo() throws IOException {
        process("do");
        this.CompileExpression();
        out.writePop(TEMP,0);
        process(";");
    }

    public void CompileReturn() throws IOException {
        process("return");
        if (!tokenaizer.getCurrentToken().equals(";")){
            this.CompileExpression();
        }
        process(";");
    }
    public void CompileExpression()throws IOException {
        this.CompileTerm();
        while (!tokenaizer.getCurrentToken().equals(",") && !tokenaizer.getCurrentToken().equals(";") && !tokenaizer.getCurrentToken().equals(")") && !tokenaizer.getCurrentToken().equals("]")){
            String op=tokenaizer.getCurrentToken();
            if(op.equals("+")){
                process("+");
            }
            else if(op.equals("-")){
                process("-");
            }
            else if(op.equals("*")){
                process("*");
            }
            else if(op.equals("/")){
                process("/");
            }
            else if(op.equals("&")){
                process("&");
            }
            else if(op.equals("|")){
                process("|");
            }
            else if(op.equals("<")){
                process("<");
            }
            else if(op.equals(">")){
                process(">");
            }
            else if(op.equals("=")){
                process("=");
            }
            this.CompileTerm();
            if(opSet.containsKey(op)){
                //if(op.equals( "|")){System.out.println(op);}
                out.writeArithmetic(opSet.get(op));
            }else{
                if(op.equals("*")){
                    out.writeCall("Math.multiply", 2);
                }else{
                    out.writeCall("Math.divide",2);
                }
            }
        }
    }
    public void CompileTerm() throws IOException {
        if (tokenaizer.tokenType() == SYMBOL) {
            if (tokenaizer.symbol() == '~') {
                process("~");
                this.CompileTerm();
                out.writeArithmetic(VMWriter.NOT);

            } else if (tokenaizer.symbol() == '-') {
                process("-");
                this.CompileTerm();
                out.writeArithmetic(NEG);
            }
        }
        if (tokenaizer.tokenType() == SYMBOL) {
            if (tokenaizer.symbol() == '(') {
                process("(");
                this.CompileExpression();
                process(")");
            }
        }
        else if (tokenaizer.tokenType() == INT_CONST) {
            out.writePush(CONST,tokenaizer.intVal());
            process("" + tokenaizer.intVal());
        }

        else if (tokenaizer.tokenType() == STRING_CONST) {
            String str = tokenaizer.stringVal();
            process(str);
            out.writePush(CONST, str.length());
            out.writeCall("String.new",1);
            for (int i=0; i<str.length(); i++) {
                out.writePush(CONST, (int)str.charAt(i));
                out.writeCall("String.appendChar", 2);
            }
        }

        else if (tokenaizer.tokenType() == KEYWORD) {
            String key = keywordSet.get(tokenaizer.keyWord());
            switch (key){
                case "true":
                    out.writePush(CONST,1);
                    out.writeArithmetic(NEG);
                    process(key);
                    break;
                case "null":
                    out.writePush(CONST,0);
                    process(key);
                    break;
                case "false":
                    out.writePush(CONST,0);
                    process(key);
                    break;
                case "this":
                    out.writePush(POINTER,0);
                    process(key);
                    break;
            }
        }
        else if (tokenaizer.tokenType() == IDENTIFIER) {
            String variable=tokenaizer.identifier();
            //base
            boolean method = false;
            boolean is_object = false;
            if(st.IndexOf(variable)>=0) {
                out.writePush(segmentSet.get(st.KindOf(variable)), st.IndexOf(variable));
                if (st.TypeOf(variable).equals(class_name)) {
                    method = true;
                }
                if(st.KindOf(variable).equals("LOCAL") || st.KindOf(variable).equals("FIELD") ) {
                    is_object = true;
                }
                process(variable);
            }
            else {
                process(variable);
                if (tokenaizer.getCurrentToken().equals("(")){
                    this.param_number = 1;
                    out.writePush(POINTER, 0);
                    process("(");
                    this.CompileExpressionList();
                    process(")");
                    out.writeCall(class_name + "." + variable , param_number);
                }
            }
            if (tokenaizer.getCurrentToken().equals("[")) {
                process("[");
                //offset
                this.CompileExpression();
                process("]");
                //prepare base + offset
                out.writeArithmetic(ADD);
                //pop into THAT pointer
                out.writePop(POINTER, 1);
                //push *(base+offset) back to stack
                out.writePush(THAT, 0);
            }

            else if (tokenaizer.getCurrentToken().equals(".")) {
                process(".");
                if(method || is_object) {
                    this.param_number = 1;
                }else{
                    this.param_number=0;
                }
                String name = tokenaizer.identifier();
                process(name);
                process("(");
                this.CompileExpressionList();
                process(")");
                if(method) {
                    out.writeCall(class_name + "." + name, param_number);
                }
                else if (st.IndexOf(variable)>=0){
                    out.writeCall(st.TypeOf(variable)+ "." + name, param_number);
                }
                else{
                    out.writeCall(variable + "." + name, param_number);
                }
            }
            else if (tokenaizer.getCurrentToken().equals("(")) {
                process("(");
                if(is_object) {
                    this.param_number = 1;
                }else{
                    this.param_number=0;
                }
                this.CompileExpressionList();
                process(")");
                out.writeCall(class_name + "." + variable , param_number);
            }
        }
    }

    public void CompileExpressionList() throws IOException{
        boolean moreThenOneex=false;
        while(!tokenaizer.getCurrentToken().equals(")")){
            param_number++;
            if(moreThenOneex){
                process(",");
            }
            this.CompileExpression();
            moreThenOneex=true;
        }
    }
}
