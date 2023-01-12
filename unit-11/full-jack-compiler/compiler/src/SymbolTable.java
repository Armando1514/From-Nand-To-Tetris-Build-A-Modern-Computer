import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SymbolTable {
    /////////////////////////////////////////////////////////
    public static final int ClASS_SCOPE = 0;
    public static final int SUBROUTINE_SCOPE = 1;
    //////////////////////////////////////////////////////////
    private final Map<String, Symbol> classLVLTable = new HashMap<>();
    private final Map<String, Symbol> subRoutineLVLTable = new HashMap<>();
    private int curScopeTable = ClASS_SCOPE;
    private int curFieldNum= 0;
    private int curStaticNum = 0;
    private int curArgNum = 0;
    private int curVarNum = 0;

    public SymbolTable(){}

    void startSubroutine(){
        subRoutineLVLTable.clear();
        this.curArgNum = 0;
        this.curVarNum = 0;
        setCurScopeTable(SUBROUTINE_SCOPE);
    }

    void setCurScopeTable(int n){
        this.curScopeTable = n;
    }

    void define(String name, String type, String kind){
        Symbol symbol;
        if (Objects.equals(kind, JackTokenizer.S_STATIC)){
            symbol = new Symbol(name, type, Kind.STATIC, curStaticNum++);
            classLVLTable.put(name, symbol);
        }
        if (Objects.equals(kind, JackTokenizer.S_FIELD)){
            symbol = new Symbol(name, type, Kind.FIELD, curFieldNum++);
            classLVLTable.put(name, symbol);
        }
        if (Objects.equals(kind, JackTokenizer.S_VAR)){
            symbol = new Symbol(name, type, Kind.VAR, curVarNum++);
            subRoutineLVLTable.put(name, symbol);
        }
        if (Objects.equals(kind, JackTokenizer.S_ARG)){
            symbol = new Symbol(name, type, Kind.ARG, curArgNum++);
            subRoutineLVLTable.put(name, symbol);
        }


    }

    int VarCount(String kind){
         switch (kind) {
            case JackTokenizer.S_FIELD: {
                return curFieldNum;
            }
            case JackTokenizer.S_STATIC: {
                return curStaticNum;
            }
            case JackTokenizer.S_ARG: {
                return curArgNum;
            }
            case JackTokenizer.S_VAR: {
                return curVarNum;
            }
            default : {
                return 0;
            }
        }
    }

    Kind kindOf(String name){
        Map<String, Symbol> scopeTable = subRoutineLVLTable;
        if (this.curScopeTable == ClASS_SCOPE){
            scopeTable = classLVLTable;
        }
        return scopeTable.get(name) == null ? Kind.NONE : scopeTable.get(name).getKind();

    }

    String TypeOf(String name){
        Map<String, Symbol> scopeTable = subRoutineLVLTable;
        if (this.curScopeTable == ClASS_SCOPE){
            scopeTable = classLVLTable;
        }
        return scopeTable.get(name).getType();
    }

    int IndexOf(String name){
        Map<String, Symbol> scopeTable = subRoutineLVLTable;
        if (this.curScopeTable == ClASS_SCOPE){
            scopeTable = classLVLTable;
        }
        return scopeTable.get(name).getNum();
    }



}
