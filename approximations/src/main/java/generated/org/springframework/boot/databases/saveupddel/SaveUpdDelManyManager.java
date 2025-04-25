package generated.org.springframework.boot.databases.saveupddel;

import generated.org.springframework.boot.databases.basetables.BaseTableManager;
import generated.org.springframework.boot.databases.basetables.NoIdTableManager;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;

// T - type of objects to save
// V1 - type of id field of parent class
// V2 - type of id field of objects class
public class SaveUpdDelManyManager<T, V1, V2> {

    public SaveUpdDelCtx context;

    public BaseTableManager<V2> manager;

    public BiConsumer<T, SaveUpdDelCtx> saveUpd; // nullable
    public BiConsumer<T, SaveUpdDelCtx> delete; // nullable

    public V1 parentId; // nullable
    public Function<T, V2> getTId; // nullable

    // decides view of row in relation table: is [V1; V2] or [V2; V1]
    // 1 means [V2; V1], 0 means [V1; V2]
    public int shouldShuffle; // nullable

    public SaveUpdDelManyManager(
            SaveUpdDelCtx context,
            BaseTableManager<V2> manager,
            BiConsumer<T, SaveUpdDelCtx> saveUpd, // nullable
            BiConsumer<T, SaveUpdDelCtx> delete, // nullable

            V1 parentId, // nullable
            Function<T, V2> getTId, // nullable
            int shouldShuffle
    ) {
        this.context = context;
        this.manager = manager;
        this.saveUpd = saveUpd;
        this.delete = delete;

        this.parentId = parentId;
        this.getTId = getTId;
        this.shouldShuffle = shouldShuffle;
    }

    public V2 getId(T t) { return getTId.apply(t); }

    public void setShouldShuffle(int shouldShuffle) {
        this.shouldShuffle = shouldShuffle;
    }

    public void setAllowRecursiveUpdate(boolean allowRecursiveUpdate) {
        context.setAllowRecursiveUpdate(allowRecursiveUpdate);
    }

    public void saveUpdWithoutRelationTable(Collection<T> objects, int rel_pos) {
        for (T t : objects) {
            saveUpd.accept(t, context);
            manager.changeSingleFieldByIdEnsure(getTId.apply(t), rel_pos, parentId);
        }
    }

    public void delWithoutRelationTable(Collection<T> objects) {
        for (T t : objects) delete.accept(t, context);
    }

    public void saveUpd(Collection<T> objects, NoIdTableManager relationTable) {
        for (T t : objects) {
            saveUpd.accept(t, context);

            // saved relation in additional table
            Object[] relRow = new Object[2];
            relRow[1 - shouldShuffle] = getTId.apply(t);
            relRow[shouldShuffle] = parentId;
            relationTable.save(relRow);
        }
    }

    public void delete(Collection<T> objects, NoIdTableManager relationTable) {
        for (T t : objects) {
            delete.accept(t, context);

            Object[] relRow = new Object[2];
            relRow[1 - shouldShuffle] = getTId.apply(t);
            relRow[shouldShuffle] = parentId;
            relationTable.delete(relRow);
        }
    }
}
