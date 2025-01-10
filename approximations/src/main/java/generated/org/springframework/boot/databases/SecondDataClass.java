package generated.org.springframework.boot.databases;

import java.util.List;

public class SecondDataClass {

    private Integer id;

    // generated
    public Integer _getId() {
        return id;
    }

    private List<FirstDataClass> subclasses;

    private Integer subclasses_id;

    public SecondDataClass(Object[] row, ITable<FirstDataClass> subclassesCondition) {
        this.id = (Integer) row[0];
        this.subclasses_id = (Integer) row[1];

        this.subclasses = new ListWrapper<>(
                new FiltredTable<>(
                        subclassesCondition,
                        this::_subclassesFilter
                )
        );
    }

    public Boolean _subclassesFilter(FirstDataClass subclass) {
        return subclass._getId() == subclasses_id;
    }
}
