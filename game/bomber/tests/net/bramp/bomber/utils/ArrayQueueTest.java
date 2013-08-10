package net.bramp.bomber.utils;

import junit.framework.Assert;

import org.junit.Test;

import com.badlogic.gdx.utils.Array;

public class ArrayQueueTest {

	final int MAX = 128;

	@Test
	public void testPushPop() {
		ArrayQueue<Integer> q = new ArrayQueue<Integer>(16);
		Assert.assertEquals(q.size, 0);
		Assert.assertNull(q.pop());

		for (int i = 0; i < MAX; i++) {
			q.push(i);
		}

		Assert.assertEquals(MAX, q.size);

		for (int i = 0; i < MAX; i++) {
			Assert.assertEquals(i, (int)q.pop());
		}
		Assert.assertNull(q.pop());
		Assert.assertNull(q.pop());
		Assert.assertEquals(0, q.size);
	}

	public void testPushMany() {
		ArrayQueue<Integer> q = new ArrayQueue<Integer>(16);
		Array<Integer> list = new Array<Integer>(2);
		list.add(0);
		list.add(1);

		for (int i = 0; i < MAX; i++) {
			q.push(list);
		}

		for (int i = 0; i < MAX; i++) {
			Assert.assertEquals(0, (int)q.pop());
			Assert.assertEquals(1, (int)q.pop());
		}
	}
}
