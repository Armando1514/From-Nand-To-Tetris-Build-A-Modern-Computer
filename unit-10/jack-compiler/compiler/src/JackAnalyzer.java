import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class JackAnalyzer {

    public static void translateToXMLFolder(String path) throws IOException {
        File file = new File(path);
        if (file.exists()){
            if (file.getName().endsWith(".jack")){
                translateToXMLFile(path);
                return;
            }
            File[] files = file.listFiles();
            assert files != null;
            if (files.length == 0 ){
                throw new RuntimeException("No files to translate to XML");
            }
            for (File f: files){
                if (f.getPath().endsWith(".jack")){
                    translateToXMLFile(f.getPath());
                }
            }
            return;
        }
        throw new RuntimeException("File doesn't exist");
    }

    public static void translateToXMLFile(String path) throws IOException {
        String outPass = path.replace(".jack", ".xml");
        new CompilationEngine(path, outPass);
    }




        public static void main(String[] args) throws IOException {
        if (args.length !=1){
            throw new RuntimeException("Not enough arguments!");
        }
        translateToXMLFolder(args[0]);
    }


}
