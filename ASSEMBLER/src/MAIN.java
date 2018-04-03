import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MAIN {
    private static Map<String,OPTAB> optable=new HashMap<>();
    private static ArrayList<SYMBOLTAB> symtable=new ArrayList<>();
    private static ArrayList<LITTAB> littable=new ArrayList<>();
    private static Map<String,String> register_table=new HashMap<>();
    private static int pooltab[];
    private static int curr_pool_index=0;
    private static int loc_ctr=0;
    private static boolean AD=false;
    public static void main(String[] args) throws IOException {
        FileReader fr=new FileReader("/home/lokesh/IdeaProjects/ASSEMBLER/src/OPCODES");
        BufferedReader br=new BufferedReader(fr);
        String curr_line,tokens[],operand_type="    ";
        int this_entry=0,pool_no=0;
        register_table.put("AREG","0");
        register_table.put("BREG","1");
        register_table.put("CREG","2");
        pooltab=new int[5];
        pooltab[pool_no]=curr_pool_index;
        while ((curr_line=br.readLine())!=null){
            tokens=curr_line.split(" ");
            optable.put(tokens[0],new OPTAB(tokens[0],tokens[1],tokens[2],Integer.parseInt(tokens[3])));
        }
        fr=new FileReader("/home/lokesh/IdeaProjects/ASSEMBLER/src/source_code");
        br=new BufferedReader(fr);
        while((curr_line=br.readLine())!=null){
            tokens=curr_line.split(" ");
            AD=false;

            if(tokens[0].equalsIgnoreCase("START")||tokens[0].equalsIgnoreCase("ORIGIN")){
                AD=true;
                try {
                    loc_ctr=Integer.parseInt(tokens[1]);
                }catch (Exception e){

                    loc_ctr=symtable.get(getaddress(tokens[1])).address+Integer.parseInt(tokens[3]);
                }

                intermediate_code("   ","AD", optable.get(tokens[0]).opcode, " ", "C",String.valueOf(loc_ctr));


            }
            if(tokens[0].equalsIgnoreCase("END")||tokens[0].equalsIgnoreCase("LTORG")){
                intermediate_code("","AD", optable.get(tokens[0]).opcode, " ", "","");
                for (int i=curr_pool_index;i<littable.size();i++) {
                    intermediate_code(String.valueOf(loc_ctr),"", "    ", "    ", "",littable.get(i).Literal);
                    littable.get(i).Address = loc_ctr++;
                }
                if(tokens[0].equalsIgnoreCase("LTORG")) {
                    curr_pool_index = littable.size();
                    pooltab[++pool_no] = curr_pool_index;
                }
                AD=true;

            }
            if(tokens.length>1&&tokens[1].equalsIgnoreCase("EQU")){
                symtable.add(new SYMBOLTAB(tokens[0],symtable.get(getaddress(tokens[2])).address,1));
                intermediate_code("","AD", optable.get(tokens[1]).opcode, " ", "","");
                AD=true;
            }


            if(optable.get(tokens[0])!=null && !AD){           //  IS STATEMENTS WITHOUT LABEL
                if(tokens[tokens.length-1].contains("=")){
                    littable.add(new LITTAB(tokens[3],loc_ctr));
                    operand_type="L";
                    this_entry=littable.size()-1;
                }
                else if(optable.get(tokens.length-1)==null){
                    operand_type="S";
                    if(getaddress(tokens[tokens.length-1])==9999) {
                        symtable.add(new SYMBOLTAB(tokens[tokens.length - 1], loc_ctr, 1));
                        this_entry=symtable.size()-1;
                    }
                    else {
                        this_entry=getaddress(tokens[tokens.length-1]);
                    }


                }
                intermediate_code(String.valueOf(loc_ctr),"IS",optable.get(tokens[0]).opcode,register_table.get(tokens[1]),operand_type,String.valueOf(this_entry));
                loc_ctr++;


            }
            if(optable.get(tokens[0])==null && !AD){        //label and DL statements


                if(tokens[tokens.length-1].contains("=")){      //CHECK FOR LITERALS
                    littable.add(new LITTAB(tokens[4],loc_ctr));
                    operand_type="L";
                    this_entry=littable.size()-1;
                    intermediate_code(String.valueOf(loc_ctr),"IS",optable.get(tokens[1]).opcode,register_table.get(tokens[2]),operand_type,String.valueOf(this_entry));
                }
                if(tokens[1].equalsIgnoreCase("DS")){    //DS STATEMENT
                    operand_type="C";
                    this_entry=Integer.parseInt(tokens[2]);
                    intermediate_code(String.valueOf(loc_ctr),"DL",optable.get(tokens[1]).opcode," ",operand_type,String.valueOf(this_entry));
                    if(getaddress(tokens[0])==9999) {
                        symtable.add(new SYMBOLTAB(tokens[0], loc_ctr, 1));
                        loc_ctr += Integer.parseInt(tokens[2]);
                    }
                    else{


                        symtable.set(getaddress(tokens[0]),new SYMBOLTAB(tokens[0], loc_ctr, 1));
                        loc_ctr += Integer.parseInt(tokens[2]);
                    }

                }else if(tokens[1].equalsIgnoreCase("DC")){   //DC STATEMENTS
                    operand_type="C";
                    this_entry=Integer.parseInt(tokens[2]);
                    intermediate_code(String.valueOf(loc_ctr),"DL",optable.get(tokens[1]).opcode," ",operand_type,String.valueOf(this_entry));
                    if(getaddress(tokens[0])==9999) {
                        symtable.add( new SYMBOLTAB(tokens[0], loc_ctr++, 1));
                    }
                    else{
                        symtable.set(getaddress(tokens[0]),new SYMBOLTAB(tokens[0], loc_ctr++, 1));
                    }

                } else{      //label for IS statement , add label to symtable and check operands is symbol present or not

                    if(optable.get(tokens.length-1)==null&&!tokens[tokens.length-1].contains("=")) {
                        operand_type="S";
                        if(getaddress(tokens[tokens.length-1])==9999) {
                            symtable.add(new SYMBOLTAB(tokens[tokens.length - 1], loc_ctr, 1));
                            this_entry=symtable.size()-1;
                        }
                        else {
                            this_entry=getaddress(tokens[tokens.length-1]);

                        }
                        intermediate_code(String.valueOf(loc_ctr),"IS",optable.get(tokens[1]).opcode,register_table.get(tokens[2]),operand_type,String.valueOf(this_entry));
                        symtable.add( new SYMBOLTAB(tokens[0], loc_ctr++, 1));

                    }
                }


            }

        }
        file_out_literal();
        file_out_symbol();
        pass2();
    }
    private static void pass2() throws IOException {
        FileReader fr=new FileReader("/home/lokesh/IdeaProjects/ASSEMBLER/src/IC");
        BufferedReader br=new BufferedReader(fr);
        String curr_line,tokens[];
        int i=0;
        while((curr_line=br.readLine())!=null) {

            tokens=curr_line.split("\\s+");
            i++;

            if(Pattern.matches(".*IS.*",tokens[1])){
                String table_type=tokens[3].split(",")[0].split("[(]")[1];
                int address;
                if(table_type=="S"){
                    address=symtable.get(Integer.parseInt(tokens[3].split(",")[1].split("[)]")[0])).address;
                }
                else {
                    address=littable.get(Integer.parseInt(tokens[3].split(",")[1].split("[)]")[0])).Address;
                }


                System.out.println(tokens[0]+"  "+tokens[1].split(",")[1].split("[)]")[0]+"   "+tokens[2]+"   "+address+"\n");
            }
            else if(Pattern.matches(".*DL.*",tokens[1])){
                System.out.println(tokens[0]);
            }
            else if(Pattern.matches("=.*",tokens[1])){
                System.out.println(tokens[0]+"  "+"00"+"   "+"0"+"   "+tokens[1]+"\n");
            }
        }

    }
    public static void intermediate_code(String loc_ctr,String cls,String opcode,String Register,String operand_type ,String index) throws IOException {
        FileWriter fw= new FileWriter("/home/lokesh/IdeaProjects/ASSEMBLER/src/IC",true);
        BufferedWriter bw=new BufferedWriter(fw);
        if(cls.equals("")){
            bw.append(loc_ctr+   "                   "  + index + "\n");
        }
        else if(operand_type.equals("")){
            bw.append(loc_ctr + "       " + "(" + cls + "," + opcode + ")"+"\n");
        }

        else{
            bw.append(loc_ctr + "    " + "(" + cls + "," + opcode + ")" + "  " + Register + "    " + "(" + operand_type + "," + index + ")" + "\n");
        }



        bw.close();
        fw.close();

    }
    private static void file_out_literal() throws IOException {
        FileWriter fw= new FileWriter("/home/lokesh/IdeaProjects/ASSEMBLER/src/literal_table");
        BufferedWriter bw=new BufferedWriter(fw);
        for (LITTAB l:littable){
            bw.append(l.Literal+"   "+l.Address+"\n");
        }
        bw.close();
        fw.close();


    }
    private static void file_out_symbol() throws IOException {
        FileWriter fw= new FileWriter("/home/lokesh/IdeaProjects/ASSEMBLER/src/symbol_table");
        BufferedWriter bw=new BufferedWriter(fw);
        for (SYMBOLTAB l:symtable){
            bw.append(l.symbol+"   "+l.address+"    "+l.length+"\n");
        }
        bw.close();
        fw.close();


    }
    public static int getaddress(String sym){
        int i=0;
        for (SYMBOLTAB symbol:symtable){

            if(symbol.symbol.equals(sym)){
                return i;
            }
            i++;
        }
        return 9999;
    }
}
