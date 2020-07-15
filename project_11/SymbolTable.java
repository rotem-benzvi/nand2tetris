import java.sql.Array;
import java.util.Hashtable;

public class SymbolTable {
    Hashtable<String, String> class_lavel_type;
    Hashtable<String, String> class_lavel_kind;
    Hashtable<String, Integer> class_lavel_count;
    Hashtable<String, String> sub_lavel_type;
    Hashtable<String, String> sub_lavel_kind;
    Hashtable<String, Integer> sub_lavel_count;
    int STATIC;
    int FIELD;
    int ARG;
    int VAR;

    public SymbolTable(){
        this.class_lavel_count=new Hashtable <>();
        this.class_lavel_type = new Hashtable <>();
        this.class_lavel_kind = new Hashtable <>();
        this.sub_lavel_count = new Hashtable <>();
        this.sub_lavel_type = new Hashtable <>();
        this.sub_lavel_kind = new Hashtable <>();
        this.STATIC = -1;
        this.FIELD = -1;
        this.ARG = -1;
        this.VAR = -1;
    }

    public void startSubruotine(){
        ARG=-1;
        VAR=-1;
        sub_lavel_kind.clear();
        sub_lavel_type.clear();
        sub_lavel_count.clear();
    }

    public void define(String name,String type, String kind){
        if(kind.equals("STATIC")||kind.equals("FIELD")){
            if(!class_lavel_kind.containsKey(name)) {
                class_lavel_type.put(name, type);
                class_lavel_kind.put(name, kind);
                int varCount = (kind.equals("STATIC")) ? (STATIC = STATIC + 1) : (FIELD = FIELD + 1);
                class_lavel_count.put(name, varCount);
            }
        }else {
            if(!sub_lavel_kind.contains(name)) {
                sub_lavel_type.put(name, type);
                sub_lavel_kind.put(name, kind);
                int varCount = (kind.equals("ARG")) ? (ARG = ARG + 1) : (VAR = VAR + 1);
                sub_lavel_count.put(name, varCount);
            }
        }
    }
    public int varCount(String kind){
        int var_num=0;
        switch (kind) {
            case "STATIC":
                var_num= STATIC+1;
                break;
            case "FIELD":
                var_num=FIELD+1;
                break;
            case "ARG":
                var_num= ARG+1;
                break;
            case "LOCAL":
                var_num= VAR+1;
                break;
        }
        return var_num;
    }

    public String KindOf(String name){
        if(sub_lavel_kind.containsKey(name)){
            return sub_lavel_kind.get(name);
        }if (class_lavel_kind.containsKey(name)){
            return class_lavel_kind.get(name);
        }
        return "NONE";
    }
    public String TypeOf(String name){
        if(sub_lavel_type.containsKey(name)){
            return sub_lavel_type.get(name);
        }if(class_lavel_type.containsKey(name)){
            return class_lavel_type.get(name);
        }
        return null;
    }
    public int IndexOf(String name){
        if (sub_lavel_count.containsKey(name)){
            return sub_lavel_count.get(name);
        }if(class_lavel_count.containsKey(name)){
            return class_lavel_count.get(name);
        }
        return -1;
    }
}
