public class BranchingTranslator {

    static String gotoTranslation(String data) {
        String output = "@"+data.split(" ")[1];
        output = output + "\n";
        output = output + "0;JMP";
        output = output + "\n";
        return output;
    }

    static String ifgotoTranslation(String data) {
        String gotoToLocation = "@"+data.split(" ")[1];
        String output = "@SP";
        output = output + "\n";
        output = output + "M=M-1";
        output = output + "\n";
        output = output + "A=M";
        output = output + "\n";
        output = output + "D=M";
        output = output + "\n";
        output = output + gotoToLocation;
        output = output + "\n";
        output = output + "D;JNE";
        output = output + "\n";
        return output;
    }

    static String labelTranslation(String data) {
        String output = "("+data.split(" ")[1] +")";
        output = output + "\n";
        return output;
    }
}
