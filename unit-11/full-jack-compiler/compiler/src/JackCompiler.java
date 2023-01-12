import java.io.File;
import java.io.IOException;

public class JackCompiler {


    public static void translateToVMFolder(String path) throws IOException {
        File file = new File(path);
        if (file.exists()){
            if (file.getName().endsWith(".jack")){
                translateToVMFileFile(path);
                return;
            }
            File[] files = file.listFiles();
            assert files != null;
            if (files.length == 0 ){
                throw new RuntimeException("No files to translate to VM");
            }
            for (File f: files){
                if (f.getPath().endsWith(".jack")){
                    translateToVMFileFile(f.getPath());
                }
            }
            return;
        }
        throw new RuntimeException("File doesn't exist");
    }

    public static void translateToVMFileFile(String path) throws IOException {
        new CompilationEngine(path);
    }




    public static void main(String[] args) throws IOException {
        if (args.length !=1){
            throw new RuntimeException("Not enough arguments!");
        }
        translateToVMFolder(args[0]);
    }

}
