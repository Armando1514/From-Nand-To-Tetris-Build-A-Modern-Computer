import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JackTokenizer {
    /////////////////////////////KEYWORDS///////////////
    final static String S_CLASS = "class";

    final static String S_ARG = "arg";
    final static String S_METHOD = "method";
    final static String S_FUNCTION = "function";
    final static String S_CONSTRUCTOR= "constructor";
    final static String S_INT = "int";
    final static String S_BOOLEAN = "boolean";
    final static String S_CHAR = "char";
    final static String S_VOID = "void";
    final static String S_VAR  = "var";
    final static String S_STATIC = "static";
    final static String S_FIELD  = "field";
    final static String S_LET  = "let";
    final static String S_DO  = "do";
    final static String S_IF   = "if";
    final static String S_ELSE  = "else";
    final static String S_WHILE = "while";
    final static String S_RETURN = "return";
    final static String S_TRUE = "true";
    final static String S_FALSE = "false";
    final static String S_NULL = "null";
    final static String S_THIS  = "this";


    final static int KEYWORD = 1;
    final static int SYMBOL = 2;
    final static int IDENTIFIER = 3;
    final static int INT_CONST = 4;
    final static int STRING_CONST = 5;


    final String[] keywords = new String[]{S_CLASS, S_BOOLEAN, S_CHAR, S_CONSTRUCTOR,
            S_DO, S_ELSE, S_FALSE, S_FIELD, S_FUNCTION, S_IF, S_INT, S_LET, S_METHOD,
            S_VOID, S_VAR, S_STATIC, S_WHILE, S_RETURN, S_TRUE, S_THIS, S_NULL};
    final String[] symbols = new String[]{"{", "}",
            "(",")", "[" ,"]", ".",",", ";", "+", "-", "*", "/", "&","|","<",">","=","~", "\"",
            "^", "#"};
    List<String> tokens;
    int curTokenNum;
    String curToken;

    public JackTokenizer(String pathToFile) throws FileNotFoundException {
        this.tokens = new ArrayList<>();
        readFile(new File(pathToFile));
        this.curTokenNum = -1;
        this.curToken = "";
    }



    private String getCurToken(){
        return this.curToken;
    }



    void readFile(File inFile) throws FileNotFoundException {
        Scanner scanner = new Scanner(inFile);
        while (scanner.hasNext()) {

            String line = filterComments(scanner);
            ArrayList<String> strInLine = helpME(line);
            for (String str: strInLine) {
                if (str.length() >=2){
                    if (str.charAt(0) == '"' && str.charAt(str.length()-1) =='"'){
                        this.tokens.add(str);
                        continue;
                    }
                }
                readFileHelper(str);
            }
        }
    }

    private void readFileHelper(String str) {
        String[] lineTokens = str.split("\\s+");
        if (lineTokens.length >= 1) {
            for (String lineToken : lineTokens) {
                if (lineToken.isEmpty()) {
                    continue;
                }
                findTokens(lineToken);
            }
        }
    }


    ArrayList<String> findSubsWithStrs(String line){
        ArrayList<String> subs = new ArrayList<>();
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(line);
        while (m.find()) {
            subs.add(m.group(1));
        }
        return subs;
    }


    ArrayList<String> helpME(String line){
        ArrayList<String> subs = new ArrayList();
        ArrayList<String> subStrs = findSubsWithStrs(line);

        if (subStrs.size() == 0){
            subs.add(line);
            return subs;
        }
        String curLine = line;
        for(String s: subStrs){
            int ixStart = curLine.indexOf(s);
            int ixEnd = ixStart + s.length()+1;
            subs.add(line.substring(0, ixStart-1));
            subs.add('"'+s+'"');
            curLine = curLine.substring(ixEnd);
        }
        subs.add(curLine);
        return subs;
    }



    private boolean containsIn(String token, String[] vars){
        for(String var: vars){
            if (Objects.equals(token , var)){
                return true;
            }
        }
        return false;
    }

    private boolean isSymbol(String token){
        return containsIn(token, symbols);
    }

    private boolean isKeyword(String token){
        return containsIn(token, keywords);
    }

    private boolean isNumeric(String token){
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (token == null) {
            return false;
        }
        return pattern.matcher(token).matches();
    }

    private boolean isString(String token){
        return token.startsWith("\"") && token.endsWith("\"");
    }

    private boolean isIllegalIdentifier(String token){
        return Character.isDigit(token.charAt(0));
    }

    public boolean isUnaryOp(){

        return Objects.equals(this.curToken, "-") || Objects.equals(this.curToken, "~") ||
                Objects.equals(this.curToken, "^") || Objects.equals(this.curToken, "#");
    }

    public boolean isClassVar(){
        return Objects.equals(this.curToken, "field") || Objects.equals(this.curToken, "static");
    }

    public boolean isSubroutineDec(){
        return Objects.equals(this.curToken, "constructor") || Objects.equals(this.curToken, "function") ||
                Objects.equals(this.curToken, "method") ;
    }

    public boolean isVarType(){
        return Objects.equals(this.curToken, "int") || Objects.equals(this.curToken, "char") ||
                Objects.equals(this.curToken, "boolean") ;
    }


    public boolean isOp(){
        return Objects.equals(this.curToken, "+") || Objects.equals(this.curToken, "-")||
                Objects.equals(this.curToken, "*")|| Objects.equals(this.curToken, "/")||
                Objects.equals(this.curToken, "&") ||
                Objects.equals(this.curToken, "|")|| Objects.equals(this.curToken, "<")||
                Objects.equals(this.curToken, ">")|| Objects.equals(this.curToken, "=") ||
                Objects.equals(this.curToken, "^") || Objects.equals(this.curToken, "#");
    }

    public void findTokens(String token){
        if (token == null || token.isEmpty()) {
            return;
        }
        if (token.length() == 1) {
            tokens.add(token);
            return;
        }
        String[] letters = token.split("");
        boolean flag = true;
        for (int i=0; i<letters.length; i++){
            if (isSymbol(letters[i])){
                flag = false;
                String toAdd = joinStrings(0, i, letters);
                if (!toAdd.isEmpty()){ this.tokens.add(toAdd);}
                this.tokens.add(letters[i]);
                String toContinue= joinStrings(i+1, letters.length, letters);
                if (i+1< letters.length){
                    findTokens(toContinue);
                }
                break;
            }
        }
        if (flag){this.tokens.add(token);}


    }

    private String joinStrings(int start, int end, String[] strings){
        StringBuilder toAdd= new StringBuilder();
        for (int j=start; j<end; j++){
            toAdd.append(strings[j]);
        }
        return toAdd.toString();
    }
    private String filterComments(Scanner scanner){
        String lineFiltered = scanner.nextLine().
                replaceAll("(?sm)(^(?:\\s*)?((?:/\\*(?:\\*)?).*?(?<=\\*/))|(?://).*?(?<=$))", "");
        lineFiltered = lineFiltered.replaceAll("[\\s\\t]+"," ");
        if (lineFiltered.startsWith("/**") ||lineFiltered.startsWith(" /**")){
            lineFiltered = "";
            while (scanner.hasNext() ){
                String str = scanner.nextLine();
                if (str.endsWith("*/")){
                    if (scanner.hasNext()){
                        lineFiltered = scanner.nextLine().
                                replaceAll("(?sm)(^(?:\\s*)?((?:/\\*(?:\\*)?).*?(?<=\\*/))|(?://).*?(?<=$))", "");
                    }
                    break;
                }
            }
        }
        return lineFiltered;
    }

    boolean hasMoreTokens(){
        return this.curTokenNum  < tokens.size()-1;
    }

    void advance(){
        this.curTokenNum++;
        this.curToken = this.tokens.get(this.curTokenNum);

    }


    int tokenType(){
        String curToken = this.tokens.get(this.curTokenNum);
        if (isSymbol(curToken)){
            return SYMBOL;
        }
        if (isKeyword(curToken)){
            return KEYWORD;
        }
        if (isNumeric(curToken)){
            return INT_CONST;
        }
        if (isString(curToken)){
            return STRING_CONST;
        }
        if (isIllegalIdentifier(curToken)){
            throw new RuntimeException();
        }
        return IDENTIFIER;

    }


    String keyWord(){
        if (tokenType() != KEYWORD){
            throw new RuntimeException();
        }
        return getCurToken();
    }


    public String symbol() {
        if (tokenType() != SYMBOL) {
            throw new RuntimeException("only when type of token is 'SYMBOL' can symbol()");
        }
        String token = curToken;
        switch (curToken) {
            case ">":
                token = "&gt;";
                break;
            case "<":
                token = "&lt;";
                break;
            case "&":
                token = "&amp;";
                break;
        }
        return token;
    }




    String identifier(){
        if (tokenType() != IDENTIFIER){
            throw new RuntimeException();
        }
        return getCurToken();
    }


    String stringVal(){
        if (tokenType() != STRING_CONST){
            throw new RuntimeException();
        }
        return getCurToken().replaceAll("\"","");
    }
}
