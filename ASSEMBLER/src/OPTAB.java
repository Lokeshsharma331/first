public class OPTAB {
    public String mnemonic;
    public String cls;
    public String opcode;
    public int length;
    OPTAB(String mnemonic,String cls,String opcode,int length){
        this.cls=cls;
        this.mnemonic=mnemonic;
        this.length=length;
        this.opcode=opcode;
    }
}
