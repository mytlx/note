# java迭代器



Iterable源码

```java
public interface Iterable<T> {

    Iterator<T> iterator();

    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }

    default Spliterator<T> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}
```



Iterator源码：

```java
public interface Iterator<E> {

    boolean hasNext();

    E next();

    default void remove() {
        throw new UnsupportedOperationException("remove");
    }

    default void forEachRemaining(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        while (hasNext())
            action.accept(next());
    }
}
```

迭代器是一个对象，用来遍历并选择序列中的对象。

轻量级对象：创建它的代价小

Java的Iterator只能单向移动，这个Iterator只能用来：

- 使用方法iterator（）要求容器返回一个Iterator，Iterator将准备好返回序列的第一个元素
- 使用next（）获得序列中的下一个元素
- 使用hasNext（）检查序列中是否还有元素
- 使用remove（）将迭代器新近返回的元素删除



特点：

* 能够将遍历序列的操作与序列底层的结构分离

* 元素被访问的顺序取决于集合类型

通过反复调用next方法，可以逐个访问集合中的每个元素。

如果到达了集合的末尾，next方法将抛出一个NoSuchElementException，因此，**在调用next前需要先调用hasNext方法**

java se 8中，可以调用forEachRemaining方法并提供一个lambda表达式，将对迭代器的每一个元素调用这个lambda表达式，直到没有元素为止

```java
iterator.forEachRemaining(element -> do something with element);
```



java迭代器查找操作与位置变更是紧密相连的，查找一个元素的唯一方法就是调用next，而在执行查找操作的同时，迭代器的位置随之向前移动

java迭代器位于两个元素之间，当调用next时，迭代器就越过下一个元素，并返回刚刚越过的那个元素的引用

![向前移动迭代器.png](https://github.com/mytlx/note/blob/f16855e5c6255bbedbe558f4eda530900bf07628/note/java/img/%E5%90%91%E5%89%8D%E7%A7%BB%E5%8A%A8%E8%BF%AD%E4%BB%A3%E5%99%A8.png?raw=true)



Iterator接口的remove方法将会删除上次调用next方法时返回的元素，并且如果调用remove之前没有调用next将是不合法的，抛出IllegalStateException异常

```java
it.next();
it.remove();

it.next();
it.remove();
```



### foreach与迭代器

foreach可以用于数组和任何实现了Iterable接口的类

java se 5引入了Iterable接口，该接口包含了一个能够产生Iterator的iterator（）方法，并且Iterable接口被foreach用来在序列中移动，所以任何实现了Iterable接口的类，都可以将它用于foreach语句中



foreach语句可以用于数组或其他任何Iterable，但是这并不意味着数组肯定也是一个Iterable，而任何自动包装也不会自动发生：

```java
public class ArrayIsNotIterable {
    static <T> void test(Iterable<T> ib) {
        for (T t : ib) {
            System.out.print(t + " ");
        }
    }

    public static void main(String[] args) {
        test(Arrays.asList(1, 2, 3));
        String[] strings = {"A", "B", "C"};
        // An array works in foreach, but it's not Iterable
        // test(strings);
        // You must explicitly convert it to an Iterable
        test(Arrays.asList(strings));
    }
}
// output: 1 2 3 A B C
```





## ListIterator

ListIterator的源码：

```java
public interface ListIterator<E> extends Iterator<E> {

    boolean hasNext();

    E next();

    boolean hasPrevious();

    E previous();

    int nextIndex();

    int previousIndex();

    void remove();

    void set(E e);

    void add(E e);
}
```

ListIterator是一个更加强大的Iterator的子类型，**只能用于各种List类的访问**

* 可以**双向移动**
  * previous和hasPrevious用来反向遍历链表，pervious也是返回越过的对象。
* 可以产生相对于迭代器在列表中指向的当前位置的前一个元素和后一个元素的索引
* 可以使用set方法替换它访问过的最后一个元素

#### add方法

add方法在迭代器位置之前添加一个新对象（越过的对象之后）

与Collection.add不同，这个add方法不返回boolean类型的值，假定添加操作总会改变链表。

如果链表有n个元素，有n+1个位置可以添加新元素，与迭代器的n+1个可能的位置相对应。例如，三个元素ABC有下列四个位置：

$\mid ABC$ 

$A\mid BC$ 

$AB\mid C$

$ABC \mid$

add方法只依赖于迭代器的**位置**，而remove方法依赖于迭代器的**状态**。

#### set方法和并发修改问题

set方法用一个新元素取代调用next或previous方法返回的上一个元素（越过的元素）。

如果迭代器发现它的集合被另一个迭代器修改了，或是被该集合自身的方法修改了，就会抛出一个ConcurrentModificationException异常。

为避免并发修改的异常：

* 可以根据需要给容器附加许多的迭代器，但是这些迭代器只能读取列表，另外，再单独附加一个既能读又能写的迭代器。

一种简单的方法可以检测到并发修改的问题：

* 集合可以跟踪改写操作（诸如添加或删除元素）的次数。每个迭代器都维护一个独立的计数值。在每个迭代器方法的开始处检查**自己改写操作的计数值**是否与**集合的改写操作计数值**一致，如果不一致，抛出一个ConcurrentModificationException异常 

链表只负责跟踪对列表的**结构性修改**，例如，添加元素、删除元素。set方法不被视为结构性修改。可以将多个迭代器附加给一个链表，所有的迭代器都调用set方法对现有结点的内容进行修改。



## 源码分析



AbstractList中实现iterable的源码：

```java
	public Iterator<E> iterator() {
        return new Itr();
    }

	private class Itr implements Iterator<E> {
        /**
         * Index of element to be returned by subsequent call to next.
         */
        int cursor = 0;

        /**
         * Index of element returned by most recent call to next or
         * previous.  Reset to -1 if this element is deleted by a call
         * to remove.
         */
        int lastRet = -1;

        /**
         * The modCount value that the iterator believes that the backing
         * List should have.  If this expectation is violated, the iterator
         * has detected concurrent modification.
         */
        int expectedModCount = modCount;

        public boolean hasNext() {
            return cursor != size();
        }

        public E next() {
            checkForComodification();
            try {
                int i = cursor;
                E next = get(i);
                lastRet = i;
                cursor = i + 1;
                return next;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                AbstractList.this.remove(lastRet);
                if (lastRet < cursor)
                    cursor--;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }
```





ArrayList中实现Iterable的部分

```java
    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * <p>The returned iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @return an iterator over the elements in this list in proper sequence
     */
    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * An optimized version of AbstractList.Itr
     */
    private class Itr implements Iterator<E> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = modCount;

        // prevent creating a synthetic constructor
        Itr() {}

        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            checkForComodification();
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0)	// 未调用next，直接调用remove抛出异常
                throw new IllegalStateException();
            checkForComodification();

            try {
                ArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;	// 移除后置-1，下次调用前仍需调用next
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            final int size = ArrayList.this.size;
            int i = cursor;
            if (i < size) {
                final Object[] es = elementData;
                if (i >= es.length)
                    throw new ConcurrentModificationException();
                for (; i < size && modCount == expectedModCount; i++)
                    action.accept(elementAt(es, i));
                // update once at end to reduce heap write traffic
                cursor = i;
                lastRet = i - 1;
                checkForComodification();
            }
        }
        
		// modCount:The number of times this list has been structurally modified.
        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }
```









