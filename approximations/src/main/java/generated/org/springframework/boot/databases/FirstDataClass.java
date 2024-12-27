package generated.org.springframework.boot.databases;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class FirstDataClass {

    private Integer id; // id field

    // genered
    public static Integer _getId(Object[] row) {
        return (Integer) row[0];
    }

    private SecondDataClass oneToOne; // via additional field 'oneToOne_id'

    private List<SecondDataClass> oneToMany; // via additional field 'oneToMany_id'
    private List<SecondDataClass> oneToManyAddTable; // via additional table

    private SecondDataClass manyToOne; // via additional field 'manyToOne_id'
    private List<SecondDataClass> manyToMany; // via additional table

    private Integer oneToOne_id; // generated fields
    private Integer oneToMany_id;
    private Integer manyToMany_id;

    public FirstDataClass(
            Object[] row, // row from 'FirstDataClassTable' that sorted by fields name
            ASpringJPATable oneToOneCondition, // conditions from queries to subclasses
            ASpringJPATable oneToManyCondition,
            ASpringJPATable oneToManyAddTableCondition,
            ASpringJPATable manyToOneCondition,
            ASpringJPATable manyToManyCondition
    ) {
        this.id = (Integer) row[0];
        this.manyToMany_id = (Integer) row[1];
        this.oneToMany_id = (Integer) row[2];
        this.oneToOne_id = (Integer) row[3];

        this.oneToOne = new SecondDataClass(
                oneToOneCondition.stream()
                    .filter(this::_oneToOneFilter)
                    .findFirst()
                    .orElseThrow(),
                SpringDatabases._blanck
        );

        this.oneToMany = oneToManyCondition.stream()
                .filter(this::_oneToManyFilter)
                .map(this::_oneToManyMapper)
                .toList(); // TODO: not lazy??

        Stream<Object[]> _oneToManyAddTableBetween = SpringDatabases._blanck.stream()
                .filter(this::_oneToManyAddTableBetweenFilter);

        this.oneToManyAddTable = oneToManyAddTableCondition.stream()
                .flatMap(r -> {

                            SecondDataClass rr = new SecondDataClass(r, SpringDatabases._blanck);

                            return _oneToManyAddTableBetween
                                    .filter(rr::_firstDataClassOneToManyAddTableFilter)
                                    .map(x -> rr)
                                    .distinct();
                        }
                )
                .toList();

        /*this.joinedParents = joinedParents.stream()
                .filter((r) -> SpringDatabases._blanck.stream() // table between childs and parents
                        .anyMatch((jr) -> jr[0] == r[0] && jr[1] == row[5]))
                .map((r) -> new SecondDataClass(r, SpringDatabases._blanck))
                .toList();*/
    }

    public Boolean _oneToOneFilter(Object[] row) {
        return SecondDataClass._getId(row) == oneToOne_id;
    }

    public Boolean _oneToManyFilter(Object[] row) {
        return SecondDataClass._getId(row) == oneToMany_id;
    }

    public SecondDataClass _oneToManyMapper(Object[] row) {
        return new SecondDataClass(row, SpringDatabases._blanck);
    }

    public Boolean _oneToManyAddTableBetweenFilter(Object[] row) {
        return row[0] == id; // rows in between table that linked with this
    }

    public Stream<SecondDataClass> _oneToManyAddTableFlatMap(Object[] row) {
        SecondDataClass dataClass = new SecondDataClass(r, SpringDatabases._blanck);

        return _oneToManyAddTableBetween
                .filter(rr::_firstDataClassOneToManyAddTableFilter)
                .map(x -> rr)
                .distinct();
    }
}
