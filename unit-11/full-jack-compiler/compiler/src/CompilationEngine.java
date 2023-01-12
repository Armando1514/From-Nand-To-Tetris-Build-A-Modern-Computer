import java.io.IOException;
import java.util.Objects;


public class CompilationEngine {
    //////////////////////////////////////SEGMENTS//////////////////
    public static final String CONST = "constant";
    public static final String ARG = "argument";
    public static final String LOCAL = "local";
    public static final String THIS = "this";
    public static final String THAT = "that";
    public static final String POINTER = "pointer";
    public static final String TEMP = "temp";
    public static final String STATIC = "static";



    ///////////////////////////////////INSTANCE VARS//////////////////////////////
    private final JackTokenizer jackTokenizer;
    private SymbolTable symbolTable;
    private final VMWriter vmWriter;
    private int curLabelNUM;
    private String curSubroutineName;
    private String curSubroutineType;



    CompilationEngine(String inPath) throws IOException {
        this.jackTokenizer = new JackTokenizer(inPath);
        this.vmWriter = new VMWriter(inPath);
        this.curLabelNUM = 0;
        compileFile();
    }


    void compileFile() throws IOException {
        this.symbolTable = new SymbolTable();
        while(this.jackTokenizer.hasMoreTokens()){
            this.jackTokenizer.advance();
            compileClass();
        }
        this.vmWriter.close();
    }
    void createStartTag()  {
    }

    void createEndTag()  {
    }


    void advanceCompilation(){
        if (this.jackTokenizer.hasMoreTokens()){
            this.jackTokenizer.advance();
            return;
        }
        throw new RuntimeException("Wrong rule format!");
    }


    public void compileClass() throws IOException {
        //"class"
        advanceCompilation();
        //className
        advanceCompilation();
        //"{"
        advanceCompilation();
        while (jackTokenizer.isClassVar()){
            compileClassVarDec();
        }
        while(jackTokenizer.isSubroutineDec()){
            this.symbolTable.setCurScopeTable(SymbolTable.SUBROUTINE_SCOPE);
            compileSubroutine();
            this.symbolTable.setCurScopeTable(SymbolTable.ClASS_SCOPE);
        }
        //"}"
    }


    public void compileClassVarDec() throws IOException {
        this.symbolTable.setCurScopeTable(SymbolTable.ClASS_SCOPE);
        varDecHelper();
    }


    public void wrapVarType() {
        if (this.jackTokenizer.isVarType() || Objects.equals(jackTokenizer.curToken, "void") ||
                this.jackTokenizer.tokenType()== JackTokenizer.IDENTIFIER){
            return;}
        throw new RuntimeException("Wrong rule format!");

    }

    public void varDecHelper() throws IOException {

        //var|static|field
        String varKind = this.jackTokenizer.keyWord();
        advanceCompilation();
        //int|boolean|char|className
        wrapVarType();
        String varType = this.jackTokenizer.curToken;
        advanceCompilation();
        //varName
        String varName = this.jackTokenizer.identifier();
        this.symbolTable.define(varName, varType, varKind);
        advanceCompilation();
        //(",", varName)*;
        while (!Objects.equals(jackTokenizer.curToken ,";") ){
            //if there is another symbol, including "," throw error

            advanceCompilation();
            //varName
            varName = this.jackTokenizer.identifier();
            this.symbolTable.define(varName, varType, varKind);
            advanceCompilation();
        }
        //";"
        safelyAddSign(';');
        advanceCompilation();
    }

    public void compileSubroutine() throws IOException {
        /////FINE////////////////////////////////////
        while(this.jackTokenizer.isSubroutineDec()){
            ///MANAGING SYMBOL TABLE
            this.symbolTable.startSubroutine();
            //constructor|function|method
            curSubroutineType = jackTokenizer.keyWord();
            if (Objects.equals(curSubroutineType,"method")){
                symbolTable.define("this",vmWriter.getOutFileName(),"arg");
            }
            advanceCompilation();
            //(void|varType)
            wrapVarType();
            advanceCompilation();
            //subroutineName
            curSubroutineName = jackTokenizer.identifier();
            advanceCompilation();
            //"("
            if (!Objects.equals(jackTokenizer.curToken ,"(") ){
                throw new RuntimeException();
            }
            advanceCompilation();
            //parameter list
            if (Objects.equals(jackTokenizer.curToken ,")")){
                createStartTag();
                createEndTag();
            } else if (jackTokenizer.isVarType() || jackTokenizer.tokenType() == JackTokenizer.IDENTIFIER){ ///////HERERERERRERERRERERER
                compileParameterList();
            }
            else{
                throw new RuntimeException();
            }
            //")"
            advanceCompilation();
            //subroutine body
            compileSubroutineBody();
        }

    }



    public void compileParameterList() throws IOException {
        //////fine//////////////////////////////////////
        //varType
        wrapVarType();
        String varType = this.jackTokenizer.curToken;
        advanceCompilation();
        //varName
        String varName = jackTokenizer.identifier();
        this.symbolTable.define(varName,varType,"arg");
        advanceCompilation();
        while(!Objects.equals(jackTokenizer.curToken ,")")){
            //","
            advanceCompilation();
            //varName
            varType = this.jackTokenizer.curToken;
            wrapVarType();
            advanceCompilation();
            //varName
            varName = jackTokenizer.identifier();
            this.symbolTable.define(varName,varType,"arg");
            advanceCompilation();
        }
    }

    public void compileSubroutineBody() throws IOException {
        //"{"
        advanceCompilation();
        //varDec*
        while (Objects.equals(jackTokenizer.curToken, "var")){
            compileVarDec();
        }
        subroutineToVM();
        //statements
        compileStatements();
        //"}"
        advanceCompilation();
    }


    public void compileVarDec() throws IOException {
        this.symbolTable.setCurScopeTable(SymbolTable.SUBROUTINE_SCOPE);
        varDecHelper();
    }

    public void compileStatements() throws IOException {
        while ( !(Objects.equals(jackTokenizer.curToken, ";") || Objects.equals( jackTokenizer.curToken,"}"))){
            String keyWord = jackTokenizer.keyWord();
            switch (keyWord){
                case JackTokenizer.S_IF: { compileIf(); break;}
                case JackTokenizer.S_LET: { compileLet(); break;}
                case JackTokenizer.S_WHILE: {compileWhile(); break;}
                case JackTokenizer.S_DO : {compileDo(); break;}
                case JackTokenizer.S_RETURN: {compileReturn(); break;}
                default:{ throw new RuntimeException("error");}
            }
        }

    }

    public void compileLet() throws IOException {
        //let
        advanceCompilation();
        //varName
        String varName = jackTokenizer.identifier();
        advanceCompilation();
        boolean isArray = false;
        if (Objects.equals(jackTokenizer.curToken,"[")){
            isArray = true;
            addArrIndex(varName);
        }
        //"="
        advanceCompilation();
        //expression
        compileExpression();
        //";"
        advanceCompilation();
        letToVM(varName, isArray);


    }

    private void addArrIndex(String varName) throws IOException {
        //"["
        ////push a
        variableToVM(varName);
        advanceCompilation();
        //expression
        ////push i
        compileExpression();
        //"]"
        advanceCompilation();
        ////add
        symbolToVM("+");
    }

    public void compileIf() throws IOException {

        String L1 = "LABEL" + curLabelNUM++;
        String L2 = "LABEL" + curLabelNUM++;
        //if
        advanceCompilation();
        compileCondition(L1);
        ////goto Label 2
        vmWriter.writeGoto(L2);
        ////label L1
        vmWriter.writeLabel(L1);
        if (Objects.equals(jackTokenizer.curToken, "else")){
            //else
            advanceCompilation();
            //{statements}
            compileToDo();
        }
        ////label L2
        vmWriter.writeLabel(L2);


    }

    private void compileCondition(String label) throws IOException {
        //"("
        advanceCompilation();
        //expression
        compileExpression();
        //")"
        advanceCompilation();
        /////neg
        vmWriter.writeArithmetic("~");
        ////if-goto L1
        vmWriter.writeIf(label);

        //{statements}
        compileToDo();
    }

    private void compileToDo() throws IOException {
        //"{"
        advanceCompilation();
        //statements
        compileStatements();
        //"}"
        advanceCompilation();
    }

    public void compileWhile() throws IOException {
        String L1 = "LABEL"+curLabelNUM++;
        String L2 = "LABEL"+curLabelNUM++;
        //while
        advanceCompilation();
        vmWriter.writeLabel(L1);
        //"("expression")""{"statements"}"
        compileCondition(L2);
        vmWriter.writeGoto(L1);
        vmWriter.writeLabel(L2);
        curLabelNUM++;

    }

    public void compileDo() throws IOException {
        //do
        advanceCompilation();
        //subroutine|className|varName
        String classVarName = jackTokenizer.identifier();
        advanceCompilation();
        if (Objects.equals(jackTokenizer.curToken ,".") || Objects.equals(jackTokenizer.curToken ,"(") ){
            subroutineHelper(classVarName);
        }
        else {
            throw new RuntimeException();
        }
        //")"
        advanceCompilation();
        //";"
        advanceCompilation();

        //pop the return value
        this.vmWriter.writePop(TEMP, 0);


    }

    public void compileReturn() throws IOException {
        advanceCompilation();
        if (jackTokenizer.tokenType() != JackTokenizer.SYMBOL){
            compileExpression();
        }
        else {
            vmWriter.writePush(CONST, 0);
        }
        //";"
        vmWriter.writeReturn();
        advanceCompilation();
    }

    public void compileExpression() throws IOException {
        compileTerm();
        while(!(Objects.equals(jackTokenizer.curToken ,";")  || Objects.equals(jackTokenizer.curToken ,")") ||
                Objects.equals(jackTokenizer.curToken ,"]")  ||Objects.equals(jackTokenizer.curToken ,",") )){
            if (!this.jackTokenizer.isOp()){
                throw new RuntimeException("Type Error");
            }
            String symbol = jackTokenizer.symbol();
            advanceCompilation();
            compileTerm();
            symbolToVM(symbol);
        }
    }

    private void subroutineHelper(String classVarName) throws IOException {
        String subroutineName ="";
        ///subroutineName(expressionList)
        if (Objects.equals(jackTokenizer.curToken, "(")) {
            addThisToArgsVM();
        }
        ///(className|varName).subroutineName(expressionList)
        int nArgs = 0 ;
        boolean dot = false;
        if (Objects.equals(jackTokenizer.curToken ,".") ) {
            dot = true;
            advanceCompilation();
            //subroutineName
            subroutineName = jackTokenizer.identifier();
            //push varName|className
            variableToVM(classVarName);
            advanceCompilation();
            //"("
        }
        advanceCompilation();
        //expressionList
        if (Objects.equals(jackTokenizer.curToken ,")") ) {
            createStartTag();
            createEndTag();
        } else {
            nArgs = compileExpressionList();
        }
        ///subroutineName(expressionList)
        if (Objects.equals(subroutineName, "") && !Objects.equals(this.curSubroutineType, "function") && !dot){
            vmWriter.writeCall(vmWriter.getOutFileName() +"."+classVarName, nArgs+1);
            return;
        }
        ///call (className|varName).subroutineName(expressionList)
        callToVM(classVarName, subroutineName, nArgs);


    }

    public void compileTerm() throws IOException {
        int tokenType = this.jackTokenizer.tokenType();
        switch (tokenType) {
            //integerConstant
            case JackTokenizer.INT_CONST: {
                integerToVM(jackTokenizer.intVal());
                advanceCompilation();
                break;
            }
            //keywordConstant
            case JackTokenizer.KEYWORD: {
                keywordToVM(jackTokenizer.keyWord());
                advanceCompilation();
                break;
            }
            //stringConstant
            case JackTokenizer.STRING_CONST: {
                stringToVM(jackTokenizer.stringVal());
                advanceCompilation();
                break;
            }
            //varName|varName[expression]|subroutineCall
            case JackTokenizer.IDENTIFIER : {
                //subroutineName|varName|className|
                String varClasName = jackTokenizer.identifier();
                advanceCompilation();
                if (jackTokenizer.tokenType() != JackTokenizer.SYMBOL) {
                    throw new RuntimeException();
                }
                ////////////////////////varName[expression]////////////////////////////////////
                if (Objects.equals(jackTokenizer.curToken, "[")) {
                    addArrIndex(varClasName);
                    vmWriter.writePop(POINTER,1);
                    vmWriter.writePush(THAT, 0);
                    ///////////(className|varName).subroutineName(expressionList) subroutineName(expressionList)
                } else if (Objects.equals(jackTokenizer.curToken, ".") || Objects.equals(jackTokenizer.curToken, "(")) {
                    subroutineHelper(varClasName);
                    advanceCompilation();
                }
                else {
                    variableToVM(varClasName);
                }
                break;
            }
            case JackTokenizer.SYMBOL: {
                //(expression)
                if (Objects.equals(jackTokenizer.symbol(), "(")) {
                    //"("
                    safelyAddSignAdvance('(');
                    //expression
                    compileExpression();
                    //")"
                    safelyAddSignAdvance(')');
                } else if (jackTokenizer.isUnaryOp()) {
                    //unaryOp
                    String symbol  = jackTokenizer.symbol();
                    advanceCompilation();
                    compileTerm();
                    if (symbol.equals("-")) {
                        vmWriter.writeArithmetic("_");
                    } else {
                        vmWriter.writeArithmetic(symbol);
                    }
                }
                break;
            }
        }


    }

    private void safelyAddSignAdvance(Character symbol) {
        safelyAddSign(symbol);
        advanceCompilation();
    }


    public int compileExpressionList() throws IOException {
        //////FINE
        int nArgs = 1;
        compileExpression();
        while(!Objects.equals(jackTokenizer.curToken ,")") ){
            //","
            safelyAddSign(',');
            advanceCompilation();
            //expression
            compileExpression();
            nArgs ++;
        }
        return nArgs;
    }

    void safelyAddSign(char sign) {
        if (!Objects.equals(jackTokenizer.curToken ,Character.toString(sign)) ){
            throw new RuntimeException();
        }

    }


    ////////////////////////////////////////////VM TRANSLATION FUNCTIONS///////////////////////////////
    void constructorToVM() throws IOException {
        vmWriter.writeFunction(curSubroutineName,
                symbolTable.VarCount(JackTokenizer.S_VAR));
        vmWriter.writePush(CONST, symbolTable.VarCount(JackTokenizer.S_FIELD ));
        vmWriter.writeCall("Memory.alloc", 1);
        vmWriter.writePop(POINTER, 0);
    }

    void methodToVM() throws IOException {
        vmWriter.writeFunction(curSubroutineName,
                symbolTable.VarCount(JackTokenizer.S_VAR));

        vmWriter.writePush(ARG, 0);
        vmWriter.writePop(POINTER, 0);

    }

    void functionToVM() throws IOException {
        vmWriter.writeFunction(curSubroutineName, symbolTable.VarCount(JackTokenizer.S_VAR));
    }

    void letToVM(String varName, boolean isArray) throws IOException {
        if (isArray){
            arrayToVM();
            return;
        }
        Kind kindOfVar = this.symbolTable.kindOf(varName);
        String kindOfVarStr = "";
        if (kindOfVar == Kind.NONE){
            symbolTable.setCurScopeTable(SymbolTable.ClASS_SCOPE);
            kindOfVar = symbolTable.kindOf(varName);
            if (kindOfVar == Kind.NONE){
                this.symbolTable.setCurScopeTable(SymbolTable.SUBROUTINE_SCOPE);

                return;
            }
            if (kindOfVar == Kind.STATIC){
                kindOfVarStr = STATIC;
            }
            if (kindOfVar == Kind.FIELD){
                kindOfVarStr = THIS;
            }
            vmWriter.writePop(kindOfVarStr,symbolTable.IndexOf(varName));
            symbolTable.setCurScopeTable(SymbolTable.SUBROUTINE_SCOPE);
        }
        else{
            if (kindOfVar == Kind.ARG){
                kindOfVarStr = ARG;
            }
            if (kindOfVar == Kind.VAR){
                kindOfVarStr = LOCAL;
            }
            vmWriter.writePop(kindOfVarStr,symbolTable.IndexOf(varName));
        }
    }

    void arrayToVM() throws IOException {
        this.vmWriter.writePop(TEMP, 0);
        this.vmWriter.writePop(POINTER, 1);
        this.vmWriter.writePush(TEMP, 0);
        this.vmWriter.writePop(THAT, 0);

    }

    void callToVM(String classVarName, String subroutineName, int nArgs) throws IOException {
        //call
        String name =""; int n = nArgs;
        if (symbolTable.kindOf(classVarName) == Kind.NONE){ ///the variable is not the class one
            symbolTable.setCurScopeTable(SymbolTable.ClASS_SCOPE);
            //classVarName is the name of some class
            if (symbolTable.kindOf(classVarName) == Kind.NONE){
                name = classVarName;
            }
            else {
                name = symbolTable.TypeOf(classVarName);
                n = n+1;
            }
            symbolTable.setCurScopeTable(SymbolTable.SUBROUTINE_SCOPE);
        }
        else {
            name = symbolTable.TypeOf(classVarName);
            n=n+1;
        }
        vmWriter.writeCall(name+"."+subroutineName,n);
    }

    void subroutineToVM() throws IOException{
        if (Objects.equals(curSubroutineType, "constructor")){
            constructorToVM();
        }
        if (Objects.equals(curSubroutineType, "method")){
            methodToVM();
        }
        if (Objects.equals(curSubroutineType, "function")){
            functionToVM();
        }

    }

    void variableToVM(String varName) throws IOException{
        Kind kindOfVar = this.symbolTable.kindOf(varName);
        String kindOfVarStr = "";
        if (kindOfVar == Kind.NONE){
            this.symbolTable.setCurScopeTable(SymbolTable.ClASS_SCOPE);
            kindOfVar = this.symbolTable.kindOf(varName);
            if (kindOfVar == Kind.NONE){
                this.symbolTable.setCurScopeTable(SymbolTable.SUBROUTINE_SCOPE);
                return;
            }
            if (kindOfVar == Kind.STATIC){
                kindOfVarStr = STATIC;
            }
            if (kindOfVar == Kind.FIELD){
                kindOfVarStr = THIS;
            }
            this.vmWriter.writePush(kindOfVarStr,symbolTable.IndexOf(varName));
            this.symbolTable.setCurScopeTable(SymbolTable.SUBROUTINE_SCOPE);
        }
        else{
            if (kindOfVar == Kind.ARG){
                kindOfVarStr = ARG;
            }
            if (kindOfVar == Kind.VAR){
                kindOfVarStr = LOCAL;
            }
            this.vmWriter.writePush(kindOfVarStr,symbolTable.IndexOf(varName));
        }
    }

    void symbolToVM(String symbol) throws IOException{
        switch (symbol){
            case "*":{
                vmWriter.writeCall("Math.multiply", 2);
                break;
            }
            case "/":{
                vmWriter.writeCall("Math.divide", 2);
                break;
            }
            case "&gt;":{
                vmWriter.writeArithmetic(">");
                break;
            }
            case "&lt;":{
                vmWriter.writeArithmetic("<");
                break;
            }
            case "&amp;":{
                vmWriter.writeArithmetic("&");
                break;
            }
            default:
                vmWriter.writeArithmetic(symbol);
                break;
        }
    }

    void integerToVM(int intConst) throws IOException {
        vmWriter.writePush(CONST, intConst);
    }

    void stringToVM(String strConst) throws IOException{
        vmWriter.writePush(CONST, strConst.length());
        vmWriter.writeCall("String.new", 1);
        for (int i=0; i<strConst.length(); i++){
            vmWriter.writePush(CONST, strConst.charAt(i));
            vmWriter.writeCall("String.appendChar", 2);
        }
    }

    void keywordToVM(String keywordConst) throws IOException{
        switch (keywordConst){
            case "true":{
                vmWriter.writePush(CONST, 1);
                vmWriter.writeArithmetic("_");
                break;
            }
            case "false":
            case "null": {
                vmWriter.writePush(CONST, 0);
                break;
            }
            case "this":{
                vmWriter.writePush(POINTER, 0);
                break;
            }
        }
    }

    private void addThisToArgsVM() throws IOException {
        ////if it is s constructor, then this is in pointer 0
        if (Objects.equals(this.curSubroutineType , "constructor")){
            this.vmWriter.writePush(POINTER, 0);
            ///if it is a method, then this is the first parameter got
        } else if (Objects.equals(this.curSubroutineType, "method")) {
            this.vmWriter.writePush(ARG, 0);
        }
    }

}
