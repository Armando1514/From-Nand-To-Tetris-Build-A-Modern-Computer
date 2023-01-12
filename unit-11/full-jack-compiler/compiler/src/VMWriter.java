import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VMWriter {
    private final BufferedWriter outfile;

    private final String outFileName;

    /////////////////////////////////////////////////////
    public static final String ADD = "add";
    public static final String SUB = "sub";
    public static final String NEG = "neg";
    public static final String EQ = "eq";
    public static final String GT = "gt";
    public static final String LT = "lt";
    public static final String AND = "and";
    public static final String OR = "or";
    public static final String NOT = "not";
    public static final String SHIFT_LEFT = "shiftleft";
    public static final String SHIFT_RIGHT = "shiftright";


    ///////////////////////////////////////////////////

    public VMWriter(String outFileName) throws IOException {
        this.outfile = generateOutfile(outFileName);
        this.outFileName = generateName(outFileName);
    }

    private BufferedWriter generateOutfile(String outFileName) throws IOException {
        String newOutFileName = outFileName.replace(".jack", ".vm");
        return new BufferedWriter(new FileWriter(newOutFileName));
    }
    private String generateName(String outFileName) {
        File file = new File(outFileName);
        return file.getName().replace(".jack", "");
    }

    String getOutFileName(){
        return this.outFileName;
    }


    public void writePush(String seg, int idx) throws IOException {
        this.outfile.write("push "+ seg + " " + idx + "\n");
    }

    public void writePop(String seg, int idx) throws IOException {
        this.outfile.write("pop "+ seg + " " + idx + "\n");
    }

    public void writeArithmetic(String command) throws IOException {
        String cmdStr = "";
        switch (command){
            case "+": {cmdStr=ADD; break;}
            case "-": {cmdStr=SUB; break;}
            case "~":{ cmdStr=NOT;break;}
            case "_":{  cmdStr=NEG;break;}
            case ">":{  cmdStr=GT;break;}
            case "<":{  cmdStr=LT;break;}
            case "=":{  cmdStr=EQ;break;}
            case "|":{  cmdStr=OR;break;}
            case "&":{ cmdStr=AND;break;}
            case "^":{ cmdStr=SHIFT_LEFT;break; }
            case "#":{ cmdStr=SHIFT_RIGHT;break;}
        }
        this.outfile.write(cmdStr+"\n");
    }

    public void writeLabel(String label) throws IOException {
        this.outfile.write("label "+ label + "\n");
    }

    public void writeGoto(String label) throws IOException {
        this.outfile.write("goto "+ label + "\n");
    }

    public void writeIf(String label) throws IOException{
        this.outfile.write("if-goto " + label + "\n");
    }

    public void writeCall(String name, int nArgs) throws IOException{
        this.outfile.write("call "+ name + " " + nArgs+"\n");
    }

    public void writeFunction(String name, int nVars) throws IOException{
        this.outfile.write("function " + this.outFileName + "." + name +" " + nVars + "\n");
    }

    public void writeReturn() throws IOException {
        this.outfile.write("return\n");
    }

    public void close() throws IOException {
        this.outfile.flush();
        this.outfile.close();
    }

}
