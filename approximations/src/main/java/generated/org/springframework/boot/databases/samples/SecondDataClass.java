package generated.org.springframework.boot.databases.samples;

import generated.org.springframework.boot.databases.FiltredTable;
import generated.org.springframework.boot.databases.ITable;
import generated.org.springframework.boot.databases.saveupddel.SaveUpdDelCtx;
import generated.org.springframework.boot.databases.wrappers.ListWrapper;

import java.util.List;

public class SecondDataClass {

    private Integer id;

    // generated
    public Integer _getId() {
        return id;
    }

    private List<FirstDataClass> subclasses;

    private Integer subclasses_id;
    private Integer oneToMany_id;

    public Integer _getOneToMany_id() {
        return oneToMany_id;
    }

    public static SecondDataClass foo(Object[] row) {
        return new SecondDataClass(row, null);
    }

    public SecondDataClass(Object[] row, ITable<FirstDataClass> subclassesCondition) {
        this.id = (Integer) row[0];
        this.subclasses_id = (Integer) row[1];
        this.oneToMany_id = (Integer) row[2];

        this.subclasses = new ListWrapper<>(
                new FiltredTable<>(
                        subclassesCondition,
                        this::_subclassesFilter,
                        new Object[0]
                )
        );
    }

    public Boolean _subclassesFilter(FirstDataClass subclass, Object[] methodArgs) {
        return subclass._getId() == subclasses_id;
    }

    public static void _save(SecondDataClass t, SaveUpdDelCtx ctx) {

    }

    public static void _delete(SecondDataClass t, SaveUpdDelCtx ctx) {

    }
}
