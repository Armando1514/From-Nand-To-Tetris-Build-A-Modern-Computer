import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VMTranslator {
static String fileName = null;

    public static void main(String [] args) throws IOException {
        Scanner reader = null;
        OutputStream writer = null;
        String lineTranslated = "";
        try {
            File file = new File(args[0]);
            File asmFile = null;
            if(file.isFile()) {
                reader = new Scanner(file);
                fileName = file.getName().split("\\.(?=[^\\.]+$)")[0];
                if (fileName.contains("Sys"))
                    lineTranslated = writeInitFunction();

                asmFile = new File(args[0].split("[.]")[0] + ".asm");
                asmFile.createNewFile();
                writer = new FileOutputStream(asmFile);
                lineTranslated = readerFile(reader, lineTranslated);
                if (lineTranslated != null)
                    writer.write(lineTranslated.getBytes(StandardCharsets.UTF_8), 0, lineTranslated.length());
            }
            if(file.isDirectory()) {
                String[] outputNames = file.getAbsolutePath().split("/");
                String outputName = outputNames[outputNames.length - 1] + ".asm";
                asmFile = new File(file.getAbsolutePath()+"/"+outputName);
                asmFile.createNewFile();
                File[] children = file.listFiles();
                for(File child : children) {
                    if (child.getName().contains("Sys"))
                        lineTranslated = writeInitFunction();
                }
                for (File child : children) {
                    fileName = child.getName();
                    if (child.getName().contains(".vm")) {
                        reader = new Scanner(child);
                        lineTranslated = lineTranslated + readerFile(reader, lineTranslated);
                    }
                }

                writer = new FileOutputStream(asmFile);
                if (lineTranslated != null)
                    writer.write(lineTranslated.getBytes(StandardCharsets.UTF_8), 0, lineTranslated.length());
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error during file reader initialization: " + e.getCause());
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Error during file creation: " + e.getCause());
            throw new RuntimeException(e);
        } finally {
            if(reader != null)
                reader.close();
            if(writer != null)
                writer.close();
        }
    }

    private static String readerFile(Scanner reader, String lineTranslated) throws IOException {
        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            int offset = data.indexOf("//");
            if(offset != -1){
                data = data.substring(0, offset);
            }
            if(!data.isEmpty()) {
                lineTranslated = lineTranslated + translateLineToAssembler(data);
            }
        }
        return lineTranslated;
    }

    private static String translateLineToAssembler(String data) {
        //Extract the number from the line
        Pattern p = Pattern.compile("\\d+");

        if(data.split(" ")[0].contains("return")) {
            return FunctionTranslator.translateReturnFunction();
        }
        
        // e.g. pop local 3 (there is space)
        // if-goto label (there is space)
        // label LOOP (there is space)
        // call function 1 (there is space)
        int i = data.indexOf(' ');
        if ( i != -1) {
            String firstWord = data.substring(0, i);
            Matcher m = null;
            String numericValue = null;
            
        switch (firstWord) {
            case "push":
                m = p.matcher(data);
                m.find();
                numericValue = m.group();
                return pushOperationTranslation(data, numericValue);
            case "pop":
                m = p.matcher(data);
                m.find();
                numericValue = m.group();
                return popOperationTranslation(data, numericValue);
            case "label":
                return BranchingTranslator.labelTranslation(data);
            case "goto":
                return BranchingTranslator.gotoTranslation(data);
            case "if-goto":
                return BranchingTranslator.ifgotoTranslation(data);
            case "call":
                return FunctionTranslator.callTranslation(data);
            case "function":
                return FunctionTranslator.functionTranslation(data);
        }
        } else {
            switch (data) {
                case "add":
                    return ArithmeticTranslator.addOperation();
                case "sub":
                    return ArithmeticTranslator.subOperation();
                case "and":
                    return ArithmeticTranslator.andOperation();
                case "or":
                    return ArithmeticTranslator.orOperation();
                case "neg":
                    return ArithmeticTranslator.negOperation();
                case "not":
                    return ArithmeticTranslator.notOperation();
                case "eq":
                    return ArithmeticTranslator.eqOperation();
                case "gt":
                    return ArithmeticTranslator.gtOperation();
                case "lt":
                    return ArithmeticTranslator.ltOperation();
            }
        }

        return "";
    }

    private static String writeInitFunction(){
        String output = "@256";
        output = output + "\n";
        output = output + "D=A";
        output = output + "\n";
        output = output + "@SP";
        output = output + "\n";
        output = output + "M=D";
        output = output + "\n";
        String data = "call Sys.init 0";
        output = output + FunctionTranslator.callTranslation(data);
        return output;
    }


    private static String popOperationTranslation(String data, String numericValue) {
        String output = "";
        data = data.split(" ")[1];

       if(data.contains("local")) {
            output = MemoryTranslator.popSegment(numericValue,"LCL");
        }
        else if(data.contains("argument")) {
           output = MemoryTranslator.popSegment(numericValue, "ARG");
        }
        else if (data.contains("this")) {
            output = MemoryTranslator.popSegment(numericValue, "THIS");
        }
        else if (data.contains("that")) {
            output = MemoryTranslator.popSegment(numericValue, "THAT");
        }
        else if (data.contains("temp")) {
            output = MemoryTranslator.popTemp(numericValue);
        }
        else if (data.contains("pointer")) {
            output = MemoryTranslator.popPointer(numericValue);
        }
        else if (data.contains("static")) {
            output = MemoryTranslator.popStatic(numericValue, fileName);
        }

        return output;
    }




    private static String pushOperationTranslation(String data, String numericValue){
        String output = "";
        data = data.split(" ")[1];

        if(data.contains("constant")) {
            output = MemoryTranslator.pushConstant(numericValue);
        }
        else if(data.contains("local")) {
            output = MemoryTranslator.pushSegment(numericValue,"LCL");
        }
        else if(data.contains("argument")) {
            output = MemoryTranslator.pushSegment(numericValue, "ARG");
        }
        else if (data.contains("this")) {
            output = MemoryTranslator.pushSegment(numericValue, "THIS");
        }
        else if (data.contains("that")) {
            output = MemoryTranslator.pushSegment(numericValue, "THAT");
        }
        else if (data.contains("temp")) {
            output = MemoryTranslator.pushTemp(numericValue);
        }
        else if (data.contains("pointer")) {
            output = MemoryTranslator.pushPointer(numericValue);
        }
        else if (data.contains("static")) {
            output = MemoryTranslator.pushStatic(numericValue, fileName);
        }

        return output;
    }



}
