public class ArithmeticTranslator {
    private static int boolCount = 0;

    public static String addOperation() {
        String output = "@SP";
        output = output + "\n";
        output = output + "A=M-1";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "A=A-1";
        output = output + "\n";
        output = output + "M=D+M";
        output = output + "\n";
        output = output + "D=A+1";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        return output;
    }
    public static String subOperation() {
        String output = "@SP";
        output = output + "\n";
        output = output + "A=M-1";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "A=A-1";
        output = output + "\n";
        output = output + "M=M-D";
        output = output + "\n";
        output = output + "D=A+1";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        return output;
    }

    public static String andOperation() {
        String output = "@SP";
        output = output + "\n";
        output = output + "A=M-1";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "A=A-1";
        output = output + "\n";
        output = output + "M=M&D";
        output = output + "\n";
        output = output + "D=A+1";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        return output;
    }

    public static String orOperation() {
        String output = "@SP";
        output = output + "\n";
        output = output + "A=M-1";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "A=A-1";
        output = output + "\n";
        output = output + "M=M|D";
        output = output + "\n";
        output = output + "D=A+1";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        return output;
    }

    public static String negOperation() {
        String output = "@SP";
        output = output + "\n";
        output = output + "A=M-1";
        output = output + "\n";
        output = output + "M=-M";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "A=M-1";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        return output;
    }

    public static String notOperation() {
        String output = "@SP";
        output = output + "\n";
        output = output + "A=M-1";
        output = output + "\n";
        output = output + "M=!M";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "A=M-1";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        return output;
    }

    public static String eqOperation() {
        String output = "@SP";
        output = output + "\n";
        output = output + "A=M-1";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "A=A-1";
        output = output + "\n";
        output = output + "D=M-D";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=M-1";
        output = output + "\n";
        output = output + "M=M-1";
        output = output + "\n";
        output = output + "@BOOLTRUE."+boolCount;
        output = output + "\n";
        output = output + "D;JEQ";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "M=0";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=M+1";
        output = output + "\n";
        output = output + "@ENDBOOL."+boolCount;
        output = output + "\n";
        output = output + "0;JMP";
        output = output + "\n";
        output = output + "(BOOLTRUE."+boolCount+")";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "M=-1";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=M+1";
        output = output + "\n";
        output = output + "(ENDBOOL."+boolCount+")";
        output = output + "\n";


        boolCount ++;
        return output;
    }
    public static String gtOperation() {

        String output = "@SP";
        output = output + "\n";
        output = output + "A=M-1";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "A=A-1";
        output = output + "\n";
        output = output + "D=M-D";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=M-1";
        output = output + "\n";
        output = output + "M=M-1";
        output = output + "\n";
        output = output + "@BOOLTRUE."+boolCount;
        output = output + "\n";
        output = output + "D;JGT";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "M=0";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=M+1";
        output = output + "\n";
        output = output + "@ENDBOOL."+boolCount;
        output = output + "\n";
        output = output + "0;JMP";
        output = output + "\n";
        output = output + "(BOOLTRUE."+boolCount+")";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "M=-1";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=M+1";
        output = output + "\n";
        output = output + "(ENDBOOL."+boolCount+")";
        output = output + "\n";


        boolCount ++;
        return output;
    }
    public static String ltOperation() {

        String output = "@SP";
        output = output + "\n";
        output = output + "A=M-1";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "A=A-1";
        output = output + "\n";
        output = output + "D=M-D";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=M-1";
        output = output + "\n";
        output = output + "M=M-1";
        output = output + "\n";
        output = output + "@BOOLTRUE."+boolCount;
        output = output + "\n";
        output = output + "D;JLT";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "M=0";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=M+1";
        output = output + "\n";
        output = output + "@ENDBOOL."+boolCount;
        output = output + "\n";
        output = output + "0;JMP";
        output = output + "\n";
        output = output + "(BOOLTRUE."+boolCount+")";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "M=-1";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=M+1";
        output = output + "\n";
        output = output + "(ENDBOOL."+boolCount+")";
        output = output + "\n";


        boolCount ++;
        return output;
    }
}
