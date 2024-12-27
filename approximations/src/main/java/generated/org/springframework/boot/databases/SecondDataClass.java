package generated.org.springframework.boot.databases;

import java.util.List;

public class SecondDataClass {

    private Integer id;

    // genered
    public static Integer _getId(Object[] row) {
        return (Integer) row[0];
    }

    private List<FirstDataClass> subclasses;

    private Integer subclasses_id;

    public SecondDataClass(Object[] row, ASpringJPATable subclassesCondition) {
        this.id = (Integer) row[0];
        this.subclasses_id = (Integer) row[1];

        this.subclasses = subclassesCondition.stream()
                .filter(this::_subclassesFilter)
                .map(this::_subclassesMapper)
                .toList();
    }

    public Boolean _subclassesFilter(Object[] row) {
        return FirstDataClass._getId(row) == subclasses_id;
    }

    public FirstDataClass _subclassesMapper(Object[] row) {
        return new FirstDataClass(
                row,
                SpringDatabases._blanck, // no condition
                SpringDatabases._blanck,
                SpringDatabases._blanck,
                SpringDatabases._blanck,
                SpringDatabases._blanck
        );
    }

    // filter to FirstDataClass oneToManyAddTable field
    public Boolean _firstDataClassOneToManyAddTableFilter(Object[] row) {
        return row[1] == id;
    }
}
