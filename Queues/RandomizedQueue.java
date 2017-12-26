import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int count;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
        size = 1;
        count = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return count == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return count;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new java.lang.IllegalArgumentException();

        if (count == size) {
            resize(2 * size);
        }
        items[count++] = item;
    }

    private void resize(int newSize) {
        Item[] newItems = (Item[]) new Object[newSize];
        for (int i = 0; i < count; i++) {
            newItems[i] = items[i];
        }
        size = newSize;
        items = newItems;
    }

    // remove and return a random item
    public Item dequeue() {
        if (count == 0)
            throw new java.util.NoSuchElementException();

        int randomIndex = StdRandom.uniform(count);
        Item retItem = items[randomIndex];
        items[randomIndex] = items[count-1];
        items[count-1] = null;
        count--;

        if (count > 0 && count == size/4)
            resize(size/2);
        return retItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (count == 0)
            throw new java.util.NoSuchElementException();

        int randomIndex = StdRandom.uniform(count);
        return items[randomIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RQueueIterator();
    }

    private class RQueueIterator implements Iterator<Item> {
        private int index = 0;
        private int[] randomIndices = StdRandom.permutation(count);

        public boolean hasNext() {
            return index < count;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

        public Item next() {
            if (index >= count)
                throw new java.util.NoSuchElementException();

            Item retItem = items[randomIndices[index]];
            index++;
            return retItem;
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        // unit testing
    }
}