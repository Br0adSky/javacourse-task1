import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        MyCollection<Object> collection = new MyCollection<>(createNewList());
        printCollection(collection);
        MyCollection<String> collection1 = new MyCollection<>();
        hashCodeCompare(collection1, collection);
        collection1.addAll(createNewList());
        equalsAndHashCode(collection1, collection);
        addFirst(collection, "New First Element");
        List<?> listToRetain = createNewList();
        listToRetain.remove("Third string");
        retainCollection(collection, listToRetain);
        get(collection, 1);
        removeAndClear(collection);

    }

    public static List<?> createNewList() {
        List<String> testList = new ArrayList<>();
        testList.add("Seems like add is working...");
        testList.add("Second string");
        testList.add("Third string");
        return testList;
    }

    public static void printCollection(MyCollection<?> collection) {
        System.out.println(collection);
    }

    public static void hashCodeCompare(MyCollection<?> collection1, MyCollection<?> collection2) {
        System.out.printf("Сравнение hashCode одной коллекции: %s%n", collection1.hashCode() == collection1.hashCode());
        System.out.printf("Сравнение hashCode двух коллекций: %s%n", collection1.hashCode() == collection2.hashCode());
    }

    public static void retainCollection(MyCollection<?> collection, Collection<?> whatToRetain) {
        System.out.printf("Коллекция до retainAll: %s%n", collection);
        collection.retainAll(whatToRetain);
        System.out.printf("Что необходимо оставить: %s%n", whatToRetain);
        System.out.printf("Коллекция после retainAll: %s%n", collection);
    }

    public static void get(MyCollection<?> collection, int index) {
        System.out.printf("Получение элемента по индексу: номер = %s%n", index);
        System.out.printf("Получение элемента по индексу: значение = %s%n", collection.get(index));
    }

    public static void equalsAndHashCode(MyCollection<?> collection1, MyCollection<?> collection2) {
        System.out.printf("Сравнение с помощью hashCode коллекций: %s%n", collection1.hashCode() == collection1.hashCode());
        System.out.printf("Сравнение с помощью equals двух коллекций: %s%n", collection1.equals(collection2));
    }

    public static void removeAndClear(MyCollection<?> collection) {
        collection.remove("Second string");
        System.out.printf("Удаление 1 элемента из списка: %s%n", collection);
        collection.add("Second string");
        collection.clear();
        System.out.printf("Очистка всего списка: %s%n", collection);
    }

    public static void addFirst(MyCollection<Object> collection, Object elem) {
        System.out.printf("Добавление элемента в начало: %s%n", elem);
        collection.addFirst(elem);
        System.out.printf("Список после добавления: %s%n", collection);
    }
}
