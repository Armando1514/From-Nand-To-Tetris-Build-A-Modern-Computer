public class MemoryTranslator {

    public static String popStatic(String numericValue, String fileName) {
        String output = "@SP";
        output = output + "\n";
        output = output + "M=M-1";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "@"+fileName+"."+numericValue;
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";

        return output;
    }

    public static String popPointer(String numericValue) {
        String output = "@SP";
        output = output + "\n";
        output = output + "A=M-1";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";

        if(numericValue.equalsIgnoreCase("0")){
            output = output + "@THIS";

        }
        else {
            output = output + "@THAT";
        }

        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "D=M-1";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        return output;
    }
    public static String pushPointer(String numericValue) {
        String output = "";
        if(numericValue.equalsIgnoreCase("0")){
            output = output + "@THIS";

        }
        else {
            output = output + "@THAT";
        }

        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        output = output + "D=A+1";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        return output;
    }
    public static String popTemp(String numericValue) {

        String output = "@5";
        output = output + "\n";
        output = output + "D=A";
        output = output + "\n";
        output = output + "@"+numericValue;
        output = output + "\n";
        output = output + "D=D+A";
        output = output + "\n";
        output = output + "@R13";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "A=A-1";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "@R13";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "A=A-1";
        output = output + "\n";
        output = output + "D=A";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";

        return output;
    }

    public static String popSegment(String numericValue, String baseAddressName) {
        String output = "@"+baseAddressName;
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "@"+numericValue;
        output = output + "\n";
        output = output + "D=D+A";
        output = output + "\n";
        output = output + "@R13";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "A=A-1";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "@R13";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "A=A-1";
        output = output + "\n";
        output = output + "D=A";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";

        return output;
    }

    public static String pushStatic(String numericValue, String fileName) {
        String output = "@"+ fileName + "." + numericValue;
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        output = output + "D=A+1";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";

        return output;
    }


    public static String pushTemp(String numericValue) {
        String output = "@5";
        output = output + "\n";
        output = output + "D=A";
        output = output + "\n";
        output = output + "@"+numericValue;
        output = output + "\n";
        output = output + "A=D+A";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        output = output + "D=A+1";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";

        return output;
    }

    public static String pushSegment(String numericValue, String baseAddressName) {
        String output =  "@"+baseAddressName;
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "@"+numericValue;
        output = output + "\n";
        output = output + "A=D+A";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        output = output + "D=A+1";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";

        return output;
    }


    public static String pushConstant(String numericValue) {
        String output = "@" + numericValue;
        output = output + "\n";
        output = output + "D=A";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        output = output + "D=A+1";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";

        return output;
    }
}
