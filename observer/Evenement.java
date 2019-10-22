package observer;

public class Evenement {
    private Object source;

    public Evenement(Object source){
        this.source = source;
    }

    public Object getSource() {
        return source;
    }
}
