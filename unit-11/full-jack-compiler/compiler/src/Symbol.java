import java.util.Objects;

public class Symbol {
    private String name;
    private String type;
    private Kind kind;
    private int num;

    public Symbol(String name, String type, Kind kind, int num){
        this.name = name;
        this.type = type;
        this.kind = kind;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
