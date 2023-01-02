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
        try {
            File file = new File(args[0]);
            reader = new Scanner(file);
            fileName = file.getName().split("\\.(?=[^\\.]+$)")[0];
            File asmFile = new File(args[0].split("[.]")[0] + ".asm");
            asmFile.createNewFile();
            writer = new FileOutputStream(asmFile);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String lineTranslated = translateLineToAssembler(data);
                if(lineTranslated != null)
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

    private static String translateLineToAssembler(String data) {
        //Extract the number from the line
        Pattern p = Pattern.compile("\\d+");

        // e.g. pop local 3 (there is space)
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

        return null;
    }

    private static String popOperationTranslation(String data, String numericValue) {
        String output = "";

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
