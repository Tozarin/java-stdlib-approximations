package stub.spring;

import generated.org.springframework.boot.databases.ITable;
import generated.org.springframework.boot.databases.wrappers.IListWrapper;
import generated.org.springframework.boot.databases.wrappers.IWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListWrapper<T> implements List<T>, IWrapper<T>, IListWrapper<T> {

    public ListWrapper(ITable<T> table) {
        throw new LinkageError();
    }

    @Override
    public ITable<T> unwrap() {
        throw new LinkageError();
    }

    @Override
    public int size() {
        throw new LinkageError();
    }

    @Override
    public boolean isEmpty() {
        throw new LinkageError();
    }

    @Override
    public boolean contains(Object o) {
        throw new LinkageError();
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        throw new LinkageError();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        throw new LinkageError();
    }

    @NotNull
    @Override
    public <T1> T1[] toArray(@NotNull T1[] a) {
        throw new LinkageError();
    }

    @Override
    public boolean add(T t) {
        throw new LinkageError();
    }

    @Override
    public boolean remove(Object o) {
        throw new LinkageError();
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        throw new LinkageError();
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        throw new LinkageError();
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        throw new LinkageError();
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        throw new LinkageError();
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        throw new LinkageError();
    }

    @Override
    public void clear() {
        throw new LinkageError();
    }

    @Override
    public T get(int index) {
        throw new LinkageError();
    }

    @Override
    public T set(int index, T element) {
        throw new LinkageError();
    }

    @Override
    public void add(int index, T element) {
        throw new LinkageError();
    }

    @Override
    public T remove(int index) {
        throw new LinkageError();
    }

    @Override
    public int indexOf(Object o) {
        throw new LinkageError();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new LinkageError();
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        throw new LinkageError();
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int index) {
        throw new LinkageError();
    }

    @NotNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new LinkageError();
    }

    @Override
    public int getSizeOfCache() {
        throw new LinkageError();
    }

    @Override
    public int getModCount() {
        throw new LinkageError();
    }

    @Override
    public int getWrpStartIx() {
        throw new LinkageError();
    }

    @Override
    public int getWrpEndIx() {
        throw new LinkageError();
    }

    @Override
    public T getFromCache(int ix) {
        throw new LinkageError();
    }

    @Override
    public T cacheNext() {
        throw new LinkageError();
    }

    @Override
    public void cacheUntilIx(int ix) {
        throw new LinkageError();
    }
}
