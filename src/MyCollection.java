import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyCollection<E> implements Collection<E> {
    private transient int size = 0;

    private transient Entry<E> head;
    private transient Entry<E> last;

    public MyCollection() {
    }

    public MyCollection(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    public int indexOf(Object checkingItem) {
        Itr iterator = iterator();
        if (checkingItem == null) {
            for (Object item = iterator.next(); iterator.hasNext(); item = iterator.next()) {
                if (item == null)
                    return iterator.nextIndex;
            }
        } else {
            for (Object item = iterator.next(); iterator.hasNext(); item = iterator.next()) {
                if (checkingItem.equals(item))
                    return iterator.nextIndex;
            }
        }
        return -1;
    }

    public E get(int index) {
        return findEntryByIndex(index).getItem();
    }

    public E getLast() {
        return last.getItem();
    }

    public E getHead() {
        return head.getItem();
    }

    @Override
    public Itr iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[this.size()];
        if(head != null && last != null){
            Itr iterator = iterator();
            for (Object currentItem = iterator.next(); iterator.hasNext(); currentItem = iterator.next()) {
                array[iterator.nextIndex - 1] = currentItem;
            }
        }
        return array;
    }

    @Override
    public boolean add(Object item) {
        final Entry<E> lst = last;
        final Entry<E> newEntry = new Entry<>((E) item, lst, null);
        last = newEntry;
        if (lst == null)
            head = newEntry;
        else {
            lst.nextElement = newEntry;
        }
        size++;
        return true;
    }


    public void addFirst(E newElement) {
        final Entry<E> first = head;
        final Entry<E> newEntry = new Entry<>(newElement, null, first);
        head = newEntry;
        if (first == null) {
            last = newEntry;
        } else {
            first.previousElement = newEntry;
        }

        size++;
    }

    private Entry<E> findEntryByIndex(int index) {
        Entry<E> startEntry;
        if (index > size - 1)
            throw new IllegalArgumentException("Wrong index");
        if (index < (size >> 1)) { //проверка больше ли index половины размера, чтобы перебор был не по всем элементам
            startEntry = head;
            for (int i = 0; i < index; i++)
                startEntry = startEntry.nextElement;
        } else {
            startEntry = last;
            for (int i = size - 1; i > index; i--)
                startEntry = startEntry.previousElement;
        }
        return startEntry;
    }

    private Entry<E> findEntryByItem(Object item) {
        if (head.getItem().equals(item)) {
            return head;
        }
        if (last.getItem().equals(item)) {
            return last;
        }
        Itr iterator = iterator();
        for (Object currentItem = iterator.next(); iterator.hasNext(); currentItem = iterator.next()) {
            if (currentItem.equals(item))
                return iterator.lastReturned;
        }
        throw new NoSuchElementException("Element not exist");
    }


    @Override
    public boolean remove(Object o) {
        Entry<E> nodeToRemove = findEntryByItem(o);
        if (nodeToRemove == null)
            throw new NoSuchElementException();
        try {
            deleteNode(nodeToRemove);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    private void deleteNode(Entry<E> nodeToRemove) {
        final Entry<E> nextElement = nodeToRemove.nextElement;
        final Entry<E> prevElement = nodeToRemove.previousElement;
        if (prevElement == null) {
            head = nextElement;
        } else {
            prevElement.nextElement = nextElement;
            nodeToRemove.previousElement = null;
        }

        if (nextElement == null) {
            last = prevElement;
        } else {
            nextElement.previousElement = prevElement;
            nodeToRemove.nextElement = null;
        }

        size--;
    }

    @Override
    public boolean addAll(Collection c) {
        int sizeBefore = size();
        Object[] arrayFromC = c.toArray();

        for (Object i : arrayFromC)
            add(i);
        return sizeBefore != size();
    }

    @Override
    public void clear() {
        Entry<E> currentEntry = head;
        while (head != null) {
            Entry<E> nextElem = currentEntry.nextElement;
            deleteNode(currentEntry);
            currentEntry = nextElem;
        }
    }

    @Override
    public boolean retainAll(Collection c) {
        Object[] arrayFromC = c.toArray();
        MyCollection<E> thisAfterRetain = new MyCollection<>();
        for (Object i : arrayFromC) {
            if (this.contains(i)) {
                thisAfterRetain.add(i);
            }
        }
        this.clear();
        this.addAll(thisAfterRetain);
        return !this.isEmpty();
    }

    @Override
    public boolean removeAll(Collection c) {
        Object[] arrayFromC = c.toArray();
        for (Object i : arrayFromC) {
            deleteNode(findEntryByItem(i));
        }
        return true;
    }

    @Override
    public boolean containsAll(Collection c) {
        Object[] arrayFromC = c.toArray();
        for (Object i : arrayFromC) {
            if (indexOf(i) == -1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object[] toArray(Object[] destArray) {
        if (destArray.length == size()) {
            return toArray();
        } else {
            Itr iterator = iterator();
            for (Object currentItem = iterator.next();
                 iterator.hasNext() &&
                         iterator.nextIndex <= destArray.length;
                 currentItem = iterator.next()) {
                destArray[iterator.nextIndex - 1] = currentItem;
            }
        }
        return destArray;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyCollection)) return false;
        MyCollection<?> that = (MyCollection<?>) o;
        Itr thisIterator = iterator();
        MyCollection<?>.Itr thatIterator = that.iterator();
        while (thisIterator.hasNext() && thatIterator.hasNext()) {
            Object thisObj = thisIterator.next();
            Object thatObj = thatIterator.next();
            if (!thisObj.equals(thatObj))
                return false;
        }
        return size == that.size;
    }

    @Override
    public int hashCode() {
        Itr thisIterator = iterator();
        int result = 1;
        if(head != null){
            for (Object currentItem = thisIterator.next(); thisIterator.hasNext(); currentItem = thisIterator.next()) {
                result = 31 * result + currentItem.hashCode();
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }

    private static class Entry<E> {
        private final E item;
        private Entry<E> previousElement;
        private Entry<E> nextElement;


        public Entry(E currentElement, Entry<E> previousElement, Entry<E> nextElement) {
            this.item = currentElement;
            this.previousElement = previousElement;
            this.nextElement = nextElement;
        }

        public E getItem() {
            return item;
        }
    }

    private class Itr implements Iterator<E> {
        private Entry<E> lastReturned;
        private Entry<E> next = head;
        private int nextIndex = 0;

        @Override
        public boolean hasNext() {
            return nextIndex <= size();
        }

        @Override
        public E next() {
            if (!hasNext())
                throw new NoSuchElementException();
            lastReturned = next;
            if (next.nextElement == null) {
                nextIndex++;
                return lastReturned.getItem();
            }
            next = next.nextElement;
            nextIndex++;
            return lastReturned.getItem();
        }
    }
}
