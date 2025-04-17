package generated.org.springframework.boot.databases.saveupddel;

import java.util.HashSet;

public class SaveUpdDelCtx {

    public boolean allowRecursiveUpdate;
    public HashSet<Object> context;

    public SaveUpdDelCtx() {
        this.context = new HashSet<>();
        this.allowRecursiveUpdate = true;
    }

    public SaveUpdDelCtx(boolean allowRecursiveUpdate) {
        this.context = new HashSet<>();
        this.allowRecursiveUpdate = allowRecursiveUpdate;
    }

    public SaveUpdDelCtx(HashSet<Object> context, boolean allowRecursiveUpdate) {
        this.context = context;
        this.allowRecursiveUpdate = allowRecursiveUpdate;
    }

    public void setAllowRecursiveUpdate(boolean allowRecursiveUpdate) {
        this.allowRecursiveUpdate = allowRecursiveUpdate;
    }

    public boolean getAllowRecursiveUpdate() {
        return allowRecursiveUpdate;
    }

    public void add(Object o) {
        context.add(o);
    }

    public boolean contains(Object o) {
        return context.contains(o);
    }
}
