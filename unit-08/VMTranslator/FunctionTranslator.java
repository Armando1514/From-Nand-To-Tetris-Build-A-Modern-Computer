public class FunctionTranslator {
    static int returnCounter = 0;

    static String translateReturnFunction() {
        //endFrame = LCL
        String output = "@LCL";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "@FRAME";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";

        // gets the return address
        output = output + "@5";
        output = output + "\n";
        output = output + "A=D-A";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "@RET";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";

        // reposition the return value for the caller
        output = output + "@ARG";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "@0";
        output = output + "\n";
        output = output + "D=D+A";
        output = output + "\n";
        output = output + "@R13";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "AM=M-1";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "@R13";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";

        // reposition SP of the caller
        output = output + "@ARG";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=D+1";
        output = output + "\n";

        output = output +restoreValueTranslation("THAT");
        output = output +restoreValueTranslation("THIS");
        output = output +restoreValueTranslation("ARG");
        output = output +restoreValueTranslation("LCL");

        // goto RETURNADDRESS
        output = output + "@RET";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "0;JMP";
        output = output + "\n";

        return output;
    }

    private static String restoreValueTranslation(String target) {

        String output = "@FRAME";
        output = output + "\n";
        output = output + "AM=M-1";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "@"+target;
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";

        return output;

    }

    static String functionTranslation(String data) {

        String functionName = data.split(" ")[1];
        // number of local variables
        int nVars = Integer.parseInt(data.split(" ")[2]);
        String output = "("+functionName+")";
        output = output + "\n";
        for(int i = 0; i < nVars; i ++){
            output = output + "@0";
            output = output + "\n";
            output = output + "D=A";
            output = output + "\n";
            output = output + "@SP";
            output = output + "\n";
            output = output + "A=M";
            output = output + "\n";
            output = output + "M=D";
            output = output + "\n";
            output = output + "@SP";
            output = output + "\n";
            output = output + "M=M+1";
            output = output + "\n";
        }
        return output;
    }

    static String callTranslation(String data) {

        String functionName = data.split(" ")[1];
        String arguments = data.split(" ")[2];
        // push returnAddress
        String returnAddress = "RETURN_"+ returnCounter++;
        String output = "@"+returnAddress;
        output = output + "\n";
        output = output + "D=A";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=M+1";
        output = output + "\n";

        // push local, arguments, this and that
        output = output + writePushBaseAddress("LCL");
        output = output + writePushBaseAddress("ARG");
        output = output + writePushBaseAddress("THIS");
        output = output + writePushBaseAddress("THAT");

        // reposition ARG (SP - 5 - Nargs)
        output = output + "@SP";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "@"+arguments;
        output = output + "\n";
        output = output + "D=D-A";
        output = output + "\n";
        output = output + "@5";
        output = output + "\n";
        output = output + "D=D-A";
        output = output + "\n";
        output = output + "@ARG";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";

        //LCL = SP
        output = output + "@SP";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "@LCL";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";

        // TRANSFER CONTROL TO THE CALLED FUNCTION
        output = output + "@"+functionName;
        output = output + "\n";
        output = output + "0;JMP";
        output = output + "\n";

        // DEFINE RETURN ADDRESS AT THE END OF THE FRAME
        output = output + "("+returnAddress+")";
        output = output + "\n";

        return output;
    }

    private static String writePushBaseAddress(String seg){
        String output = "@"+seg;
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=M+1";
        output = output + "\n";
        return output;
    }
}
