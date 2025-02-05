package generated.org.springframework.boot.databases;

import java.util.List;
import java.util.Set;

public class FirstDataClass {

    private Integer id; // id field

    // generated
    public Integer _getId() {
        return id;
    }

    private SecondDataClass oneToOne; // via additional field 'oneToOne_id'

    private List<SecondDataClass> oneToMany; // via additional field 'oneToMany_id'
    private List<SecondDataClass> oneToManyAddTable; // via additional table

    private SecondDataClass manyToOne; // via additional field 'manyToOne_id'
    private List<SecondDataClass> manyToMany; // via additional table

    private Integer oneToOne_id; // generated fields
    private Integer oneToMany_id;
    private Set<Integer> oneToManySet; // ids for oneToMany from join table
    private Integer manyToOne_id;
    private Set<Integer> manyToManySet;

    public FirstDataClass(
            Object[] row, // row from 'FirstDataClassTable' that sorted by fields name

            // conditions from queries to subclasses.
            // fully correct initialized objects for which all fields were filled in at previous stages
            ITable<SecondDataClass> oneToOneCondition,
            ITable<SecondDataClass> oneToManyCondition,
            ITable<SecondDataClass> oneToManyAddTableCondition,
            ITable<SecondDataClass> manyToOneCondition,
            ITable<SecondDataClass> manyToManyCondition
    ) {
        this.id = (Integer) row[0];
        this.manyToOne_id = (Integer) row[1];
        this.oneToMany_id = (Integer) row[2];
        this.oneToOne_id = (Integer) row[3];

        this.oneToOne = new FiltredTable<>(oneToOneCondition, this::_oneToOneFilter)
                .firstEnsure();

        this.oneToMany = new ListWrapper<>(
                new FiltredTable<>(
                        oneToManyCondition,
                        this::_oneToManyFilter
                )
        );

        this.oneToManySet = new SetWrapper<>(
                new MappedTable<>(
                        new FiltredTable<>(
                                SpringDatabases._blanckAdd,
                                this::_oneToManyAddTableFilter
                        ),
                        this::_oneToManyAddTableSelector,
                        Integer.class
                )
        );

        this.oneToManyAddTable = new ListWrapper<>(
                new FiltredTable<>(
                        oneToManyAddTableCondition,
                        this::_oneToManyAddTableFilter
                )
        );

        this.manyToOne = new FiltredTable<>(manyToOneCondition, this::_manyToOneFilter)
                .firstEnsure();

        this.manyToManySet = new SetWrapper<>(
                new MappedTable<>(
                        new FiltredTable<>(
                                SpringDatabases._blanckAdd,
                                this::_manyToManyAddTableFilter
                        ),
                        this::_manyToManyAddTableSelector,
                        Integer.class
                )
        );

        this.manyToMany = new ListWrapper<>(
                new FiltredTable<>(
                        manyToManyCondition,
                        this::_manyToManyFilter
                )
        );
    }

    public Boolean _oneToOneFilter(SecondDataClass subclass) {
        return subclass._getId() == oneToOne_id;
    }

    public Boolean _oneToManyFilter(SecondDataClass subclass) {
        return subclass._getId() == oneToMany_id;
    }

    public Boolean _oneToManyAddTableFilter(Object[] row) {
        return row[0] == id; // rows in between table that linked with this
    }

    // mapper to FirstDataClass oneToManyAddTable field
    //
    //  _additional table for join_
    // |  fdcId  |  ..  |  sdcId  | -> sdcId
    // |________|______|__________|
    public Integer _oneToManyAddTableSelector(Object[] row) {
        return (Integer) row[1];
    }

    public Boolean _oneToManyAddTableFilter(SecondDataClass subclass) {
        return oneToManySet.contains(subclass._getId());
    }

    public Boolean _manyToOneFilter(SecondDataClass subclass) {
        return subclass._getId() == manyToOne_id;
    }

    public Boolean _manyToManyAddTableFilter(Object[] row) {
        return row[0] == id;
    }

    public Integer _manyToManyAddTableSelector(Object[] row) {
        return (Integer) row[1];
    }

    public Boolean _manyToManyFilter(SecondDataClass subclass) {
        return manyToManySet.contains(subclass._getId());
    }

    public Object[] _serilizer() {
        Object[] row = new Object[4];
        row[0] = id;
        row[1] = oneToOne_id;
        row[2] = oneToMany_id;
        row[3] = manyToOne_id;
        return row;
    }
}
