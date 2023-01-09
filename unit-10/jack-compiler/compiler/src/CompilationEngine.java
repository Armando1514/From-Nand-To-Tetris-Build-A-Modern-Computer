import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;


public class CompilationEngine {
    //////////////////////////////////////TAGS///////////////////////
    private final String TAG_CLASSVAR_DEC = "classVarDec";
    private final String TAG_VAR_DEC = "varDec";
    private final String TAG_TOKENS = "tokens";
    private final String TAG_CLASS = "class";
    private final String TAG_SUBROUTINE_DEC = "subroutineDec";
    private final String TAG_PARAMETERLIST = "parameterList";
    private final String TAG_SUBROUTINEBODY = "subroutineBody";
    private final String TAG_STATEMENTS = "statements";
    private final String TAG_LET = "letStatement";
    private final String TAG_IF = "ifStatement";
    private final String TAG_WHILE = "whileStatement";
    private final String TAG_DO = "doStatement";
    private final String TAG_RETURN = "returnStatement";
    private final String TAG_EXPRESSION = "expression";
    private final String TAG_TERM = "term";
    private final String TAG_EXPRESSIONLIST = "expressionList";

    /////////////////////////////////////////////////////////////////
    private final JackTokenizer jackTokenizer;
    private final BufferedWriter outFile;

    CompilationEngine(String inPath, String outPath) throws IOException {
        this.outFile = new BufferedWriter(new FileWriter(outPath));
        this.jackTokenizer = new JackTokenizer(inPath);
        compileFile();
    }


    void compileFile() throws IOException {
        while(this.jackTokenizer.hasMoreTokens()){
            this.jackTokenizer.advance();
            compileClass();
        }
        this.outFile.flush();
        this.outFile.close();
    }
    void createStartTag(String tag) throws IOException {
        this.outFile.write("<"+tag+">\n");
    }

    void createEndTag(String tag) throws IOException {
        this.outFile.write("</"+tag+">\n");
    }

    void wrapIdentifier(String token) throws IOException {
        this.outFile.write("<identifier> "+token+" </identifier>\n");
    }


    void wrapSymbol(String token) throws IOException {
        this.outFile.write("<symbol> "+token+" </symbol>\n");
    }
    void wrapKeyword(String token) throws IOException {
        this.outFile.write("<keyword> "+token+" </keyword>\n");
    }

    void wrapInteger(String token) throws IOException {
        this.outFile.write("<integerConstant> "+token+" </integerConstant>\n");
    }

    void wrapString(String token) throws IOException {

        this.outFile.write("<stringConstant> "+token+" </stringConstant>\n");
    }

    void advanceCompilation(){
        if (this.jackTokenizer.hasMoreTokens()){
            this.jackTokenizer.advance();
            return;
        }
        throw new RuntimeException("Wrong rule format!");
    }



    public void compileClass() throws IOException {
        createStartTag(TAG_CLASS);
        //"class"
        wrapKeyword(jackTokenizer.keyWord());
        advanceCompilation();
        //className
        wrapIdentifier(jackTokenizer.identifier());
        advanceCompilation();
        //"{"
        wrapSymbol(jackTokenizer.symbol());
        advanceCompilation();
        while (jackTokenizer.isClassVar()){
            compileClassVarDec();
        }
        while(jackTokenizer.isSubroutineDec()){
            compileSubroutine();
        }
        //"}"
        wrapSymbol(jackTokenizer.symbol());
        createEndTag(TAG_CLASS);
    }



    public void compileClassVarDec() throws IOException {
        createStartTag(TAG_CLASSVAR_DEC);
        varDecHelper();
        createEndTag(TAG_CLASSVAR_DEC);
    }



    public void wrapVarType() throws IOException {
        if (this.jackTokenizer.isVarType() || Objects.equals(jackTokenizer.curToken, "void")){
            wrapKeyword(this.jackTokenizer.keyWord());
        } else if (this.jackTokenizer.tokenType()== JackTokenizer.IDENTIFIER) {
            wrapIdentifier(this.jackTokenizer.identifier());
        } else {
            throw new RuntimeException("Wrong rule format!");
        }
    }

    public void varDecHelper() throws IOException {
        //var|static|field
        wrapKeyword(this.jackTokenizer.keyWord());
        advanceCompilation();
        //int|boolean|char|className
        wrapVarType();
        advanceCompilation();
        //varName
        wrapIdentifier(this.jackTokenizer.identifier());
        advanceCompilation();
        //(",", varName)*;
        while (!Objects.equals(jackTokenizer.curToken ,";") ){
            //if there is another symbol, including "," throw error

            wrapSymbol(jackTokenizer.symbol());
            advanceCompilation();
            //varName
            wrapIdentifier(this.jackTokenizer.identifier());
            advanceCompilation();
        }
        //";"
        safelyAddSign(';');
        advanceCompilation();
    }

    public void compileSubroutine() throws IOException {
        /////FINE
        while(this.jackTokenizer.isSubroutineDec()){
            createStartTag(TAG_SUBROUTINE_DEC);
            //constructor|function|method
            wrapKeyword(jackTokenizer.keyWord());
            advanceCompilation();
            //(void|varType)
            wrapVarType();
            advanceCompilation();
            //subroutineName
            wrapIdentifier(jackTokenizer.identifier());
            advanceCompilation();
            //"("
            if (Objects.equals(jackTokenizer.curToken ,"(") ){
                wrapSymbol(jackTokenizer.symbol());
            }
            else{
                throw new RuntimeException();
            }
            advanceCompilation();
            //parameter list
            if (Objects.equals(jackTokenizer.curToken ,")")){
                createStartTag(TAG_PARAMETERLIST);
                createEndTag(TAG_PARAMETERLIST);
            } else if (jackTokenizer.isVarType() || jackTokenizer.tokenType() == JackTokenizer.IDENTIFIER){
                compileParameterList();
            }
            else{
                throw new RuntimeException();
            }
            //")"
            wrapSymbol(jackTokenizer.symbol());
            advanceCompilation();

            compileSubroutineBody();
            createEndTag(TAG_SUBROUTINE_DEC);
        }

    }




    public void compileParameterList() throws IOException {
        createStartTag(TAG_PARAMETERLIST);
        //varType
        wrapVarType();
        advanceCompilation();
        //varName
        wrapIdentifier(jackTokenizer.identifier());
        advanceCompilation();
        while(!Objects.equals(jackTokenizer.curToken ,")")){
            //","
            wrapSymbol(jackTokenizer.symbol());
            advanceCompilation();
            //varName
            wrapVarType();
            advanceCompilation();
            //varName
            wrapIdentifier(jackTokenizer.identifier());
            advanceCompilation();
        }
        createEndTag(TAG_PARAMETERLIST);
    }

    public void compileSubroutineBody() throws IOException {
        createStartTag(TAG_SUBROUTINEBODY);
        //"{"
        wrapSymbol(jackTokenizer.symbol());
        advanceCompilation();
        //varDec*
        while (Objects.equals(jackTokenizer.curToken, "var")){
            compileVarDec();
        }
        //statements
        compileStatements();
        //"}"
        wrapSymbol(jackTokenizer.symbol());
        createEndTag(TAG_SUBROUTINEBODY);
        advanceCompilation();
    }


    public void compileVarDec() throws IOException {
        createStartTag(TAG_VAR_DEC);
        varDecHelper();
        createEndTag(TAG_VAR_DEC);
    }

    public void compileStatements() throws IOException {
        createStartTag(TAG_STATEMENTS);
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
        createEndTag(TAG_STATEMENTS);

    }

    public void compileLet() throws IOException {
        createStartTag(TAG_LET);
        //let
        wrapKeyword(jackTokenizer.keyWord());
        advanceCompilation();
        //varName
        wrapIdentifier(jackTokenizer.identifier());
        advanceCompilation();

        if (Objects.equals(jackTokenizer.curToken,"[")){
            //"["
            wrapSymbol(jackTokenizer.symbol());
            advanceCompilation();
            //expression
            compileExpression();
            //"]"
            wrapSymbol(jackTokenizer.symbol());
            advanceCompilation();
        }
        ////////CAN BE ERRRRORS!!!!!!!!!!!!!!!!
        //"="
        wrapSymbol(jackTokenizer.symbol());
        advanceCompilation();
        //expression
        compileExpression();
        //";"
        wrapSymbol(jackTokenizer.symbol());
        advanceCompilation();
        createEndTag(TAG_LET);
    }

    public void compileIf() throws IOException {
        createStartTag(TAG_IF);
        //if
        wrapKeyword(jackTokenizer.keyWord());
        advanceCompilation();
        compileCondition();
        if (Objects.equals(jackTokenizer.curToken, "else")){
            //else
            wrapKeyword(jackTokenizer.keyWord());
            advanceCompilation();
            //{statements}
            compileToDo();
        }
        createEndTag(TAG_IF);

    }

    private void compileCondition() throws IOException {
        //"("
        wrapSymbol(jackTokenizer.symbol());
        advanceCompilation();
        //expression
        compileExpression();
        //")"
        wrapSymbol(jackTokenizer.symbol());
        advanceCompilation();
        //{statements}
        compileToDo();

    }

    private void compileToDo() throws IOException {
        //"{"
        wrapSymbol(jackTokenizer.symbol());
        advanceCompilation();
        //statements
        compileStatements();
        //"}"
        wrapSymbol(jackTokenizer.symbol());
        advanceCompilation();
    }

    public void compileWhile() throws IOException {
        createStartTag(TAG_WHILE);
        //while
        wrapKeyword(jackTokenizer.keyWord());
        advanceCompilation();
        //"("expression")""{"statements"}"
        compileCondition();
        createEndTag(TAG_WHILE);

    }

    public void compileDo() throws IOException {
        createStartTag(TAG_DO);
        //do
        wrapKeyword(jackTokenizer.keyWord());
        advanceCompilation();
        //subroutine|className|varName
        wrapIdentifier(jackTokenizer.identifier());
        advanceCompilation();
        if (Objects.equals(jackTokenizer.curToken ,".") || Objects.equals(jackTokenizer.curToken ,"(") ){
            subroutineHelper();
        }
        else {
            throw new RuntimeException();
        }
        //")"
//        this.outFile.write("///////////////////////////////////" + jackTokenizer.symbol());
        wrapSymbol(jackTokenizer.symbol());
        advanceCompilation();
        //";"
        wrapSymbol(jackTokenizer.symbol());
        createEndTag(TAG_DO);
        advanceCompilation();


    }

    public void compileReturn() throws IOException {
        createStartTag(TAG_RETURN);
        wrapKeyword(jackTokenizer.keyWord());
        advanceCompilation();
        if (jackTokenizer.tokenType() != JackTokenizer.SYMBOL){
            compileExpression();
        }
        //";"
        wrapSymbol(jackTokenizer.symbol());
        advanceCompilation();
        createEndTag(TAG_RETURN);
    }

    public void compileExpression() throws IOException {
        createStartTag(TAG_EXPRESSION);
        compileTerm();
        while(!(Objects.equals(jackTokenizer.curToken ,";")  || Objects.equals(jackTokenizer.curToken ,")") ||
                Objects.equals(jackTokenizer.curToken ,"]")  ||Objects.equals(jackTokenizer.curToken ,",") )){
            if (!this.jackTokenizer.isOp()){
                throw new RuntimeException("Type Error");
            }
            wrapSymbol(jackTokenizer.symbol());
            advanceCompilation();
            compileTerm();
        }
        createEndTag(TAG_EXPRESSION);
    }

    private void subroutineHelper() throws IOException {
        wrapSymbol(jackTokenizer.symbol());
        //this.outFile.write("START START START\n");
        if (Objects.equals(jackTokenizer.curToken ,".") ) {
            advanceCompilation();
            //subroutineName
            wrapIdentifier(jackTokenizer.identifier());
            advanceCompilation();
            //"("
            wrapSymbol(jackTokenizer.symbol());
        }
        advanceCompilation();
        //expressionList
        if (Objects.equals(jackTokenizer.curToken ,")") ) {
            createStartTag(TAG_EXPRESSIONLIST);
            createEndTag(TAG_EXPRESSIONLIST);
        } else {
            compileExpressionList();
        }


        //this.outFile.write("END END END END\n");


    }
    public void compileTerm() throws IOException {
        createStartTag(TAG_TERM);
        int tokenType = this.jackTokenizer.tokenType();
        switch (tokenType){
            //integerConstant
            case JackTokenizer.INT_CONST : {
                wrapInteger(jackTokenizer.curToken);
                advanceCompilation();
                break;
            }
            //keywordConstant
            case JackTokenizer.KEYWORD :{
                wrapKeyword(jackTokenizer.keyWord());
                advanceCompilation();
                break;
            }
            //stringConstant
            case JackTokenizer.STRING_CONST: {
                wrapString(jackTokenizer.stringVal());
                advanceCompilation();
                break;
            }
            //varName|varName[expression]|subroutineCall
            case JackTokenizer.IDENTIFIER :{
                //subroutineName|varName|className|
                wrapIdentifier(jackTokenizer.identifier());
                advanceCompilation();
                if (jackTokenizer.tokenType() != JackTokenizer.SYMBOL){
                    throw new RuntimeException();
                }
                ////////////////////////varName[expression]////////////////////////////////////
                if (Objects.equals(jackTokenizer.curToken ,"[") ){
                    //"["
                    wrapSymbol(jackTokenizer.symbol());
                    advanceCompilation();
                    //expression
                    compileExpression();
                    //"]"
                    wrapSymbol(jackTokenizer.symbol());
                    advanceCompilation();

                ///////////(className|varName).subroutineName(expressionList) subroutineName(expressionList)
                } else if (Objects.equals(jackTokenizer.curToken ,".")  || Objects.equals(jackTokenizer.curToken ,"(") ) {
                    subroutineHelper();
                    wrapSymbol(jackTokenizer.symbol());
                    advanceCompilation();
                }
                
                break;
            }
            case JackTokenizer.SYMBOL :{
                if (Objects.equals(jackTokenizer.symbol(), "(")){
                    //"("
                    safelyAddSign('(');
                    advanceCompilation();
                    //expression
                    compileExpression();
                    //")"
                    safelyAddSign(')');
                    advanceCompilation();
                } else if (jackTokenizer.isUnaryOp() ) {
                    //unaryOp   ????????????
                    wrapSymbol(jackTokenizer.symbol());
                    advanceCompilation();
                    compileTerm();
                }
                break;
            }
        }
        createEndTag(TAG_TERM);


    }

    public void compileExpressionList() throws IOException {
        //////FINE
        createStartTag(TAG_EXPRESSIONLIST);
        compileExpression();
        while(!Objects.equals(jackTokenizer.curToken ,")") ){
            //","
            safelyAddSign(',');
            advanceCompilation();
            //expression
            compileExpression();
        }
        createEndTag(TAG_EXPRESSIONLIST);
    }

    void safelyAddSign(char sign) throws IOException {
        if (Objects.equals(jackTokenizer.curToken ,Character.toString(sign)) ){
            wrapSymbol(jackTokenizer.symbol());
        }
        else {
            throw new RuntimeException();
        }

    }
}
