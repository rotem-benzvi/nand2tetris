import java.util.HashMap;



public class Code {
    HashMap <String, String> comp_map;
    HashMap <String, String> dest_map;
    HashMap <String, String> jump_map;

    public Code(){
        //init comp look up table
        comp_map=new HashMap <>();
        comp_map.put("0","0101010");
        comp_map.put("1","0111111");
        comp_map.put("-1","0111010");
        comp_map.put("D","0001100");
        comp_map.put("A","0110000");
        comp_map.put("M","1110000");
        comp_map.put("!D","0001101");
        comp_map.put("!A","0110001");
        comp_map.put("!M","1110001");
        comp_map.put("-D","0001111");
        comp_map.put("-A","0110011");
        comp_map.put("-M","1110011");
        comp_map.put("D+1","0011111");
        comp_map.put("A+1","0110111");
        comp_map.put("M+1","1110111");
        comp_map.put("D-1","0001110");
        comp_map.put("A-1","0110010");
        comp_map.put("M-1","1110010");
        comp_map.put("D+A","0000010");
        comp_map.put("D+M","1000010");
        comp_map.put("D-A","0010011");
        comp_map.put("D-M","1010011");
        comp_map.put("A-D","0000111");
        comp_map.put("M-D","1000111");
        comp_map.put("D&A","0000000");
        comp_map.put("D&M","1000000");
        comp_map.put("D|A","0010101");
        comp_map.put("D|M","1010101");
        //init dest look up table
        this.dest_map=new HashMap <>();
        dest_map.put("null","000");
        dest_map.put("M","001");
        dest_map.put("D","010");
        dest_map.put("DM","011");
        dest_map.put("MD","011");
        dest_map.put("A","100");
        dest_map.put("AM","101");
        dest_map.put("AD","110");
        dest_map.put("ADM","111");
        //init jump look up table
        this.jump_map=new HashMap <>();
        jump_map.put("null","000");
        jump_map.put("JGT","001");
        jump_map.put("JEQ","010");
        jump_map.put("JGE","011");
        jump_map.put("JLT","100");
        jump_map.put("JNE","101");
        jump_map.put("JLE","110");
        jump_map.put("JMP","111");

    }

    public String comp(String comp_ins){
        return comp_map.get(comp_ins);
    }
    public String dest(String dest_ins){
        return dest_map.get(dest_ins);
    }
    public String jump(String jump_ins){
        return jump_map.get(jump_ins);
    }

}
