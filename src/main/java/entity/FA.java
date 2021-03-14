package entity;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author TYX
 * @name FA
 * @description
 * @time
 **/
public class FA<T> {
    protected Set<T> terminal;
    protected Graph statusGraph;

//    public Set<T> getTerminal() {
//        return terminal;
//    }

    public Graph getStatusGraph() {
        return statusGraph;
    }

    public void show(){};
}
