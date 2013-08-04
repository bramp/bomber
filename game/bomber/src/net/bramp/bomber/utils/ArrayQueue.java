package net.bramp.bomber.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * Array backed Queue using a bounded array to avoid array copies when pushing/poping
 * 
 * @author bramp
 * 
 */
public class ArrayQueue<E> implements Iterable<E> {
	private E[] q;
	public int size = 0;
	private int first = 0;
	private int last = 0;

	// cast needed since no generic array creation in Java
	public ArrayQueue() {
		this(16);
	}

	public ArrayQueue (int capacity) {
		q = newArray(capacity);
	}

	@SuppressWarnings("unchecked")
	private static <T> T[] newArray(int size) {
		return (T[])new Object[size];
	}

	public boolean isEmpty() {
		return size == 0;
	}
	
	private void resize(int max) {
		assert max >= size;
		E[] temp = newArray(max);
		for (int i = 0; i < size; i++) {
			temp[i] = q[(first + i) % q.length];
		}
		q = temp;
		first = 0;
		last = size;
	}

	public void ensureCapacity (int maxCapacity) {
		if (maxCapacity > q.length) resize(Math.max(8, maxCapacity));
	}

	public void shrink() {
		resize(size);
	}

	public void push(E item) {
		// double size of array if necessary and recopy to front of array
		if (size == q.length)
			resize(Math.max(8, (int)(size * 1.75f)));
		q[last++] = item; // add item
		if (last == q.length)
			last = 0; // wrap-around
		size++;
	}

	public E pop() {
		if (size == 0)
			return null;

		E item = q[first];
		q[first] = null;
		size--;
		first++;
		if (first == q.length)
			first = 0; // wrap-around

		return item;
	}

	public Iterator<E> iterator() {
		return new ArrayIterator();
	}

	private class ArrayIterator implements Iterator<E> {
		private int i = 0;

		public boolean hasNext() {
			return i < size;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public E next() {
			if (!hasNext())
				throw new NoSuchElementException();
			E item = q[(i + first) % q.length];
			i++;
			return item;
		}
	}
}
