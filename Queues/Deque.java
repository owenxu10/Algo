import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private final Node head;
    private Node tail;
    private int count;

    private class Node
    {
        Item item;
        Node prev;
        Node next;
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = head.next;
        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

        public Item next() {
            if (current == null)
                throw new java.util.NoSuchElementException();

            Node retNode = current;
            current = current.next;
            return retNode.item;
        }
    }

    // construct an empty deque
    public Deque() {
        this.count = 0;
        this.head = new Node(); // head sentinel
        this.tail = null;
        head.next = tail;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return this.count == 0;
    }

    // return the number of items on the deque
    public int size() {
        return count;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new java.lang.IllegalArgumentException();

        Node newNode = new Node();
        Node nextNode = head.next;
        newNode.item = item;

        if (nextNode != null)
            nextNode.prev = newNode;
        newNode.next = nextNode;

        newNode.prev = head;
        head.next = newNode;

        if (this.tail == null)
            this.tail = newNode;

        count++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null)
            throw new java.lang.IllegalArgumentException();

        Node newNode = new Node();
        newNode.item = item;

        if (this.tail != null) {
            this.tail.next = newNode;
            newNode.prev = this.tail;
        } else {
            head.next = newNode;
            newNode.prev = head;
        }

        this.tail = newNode;

        count++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (count == 0)
            throw new java.util.NoSuchElementException();

        if (count == 1)
            tail = null;

        Node retNode = head.next;
        Node nextNode = retNode.next;

        head.next = nextNode;
        if (nextNode != null)
            nextNode.prev = head;

        count--;

        return retNode.item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (count == 0)
            throw new java.util.NoSuchElementException();

        Node retNode = tail;
        Node prevNode = tail.prev;

        prevNode.next = null;
        retNode.prev = null;

        tail = prevNode;

        if (count == 1)
            tail = null;

        count--;

        return retNode.item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (optional)
    public static void main(String[] args) {
        // unit testing
        Deque<Integer> deque = new Deque<Integer>();
        deque.isEmpty();
        deque.addFirst(1);
        deque.removeLast();
        deque.addFirst(3);
        deque.addFirst(4);
        deque.addFirst(5);
        System.out.println(deque.removeLast());
    }
}