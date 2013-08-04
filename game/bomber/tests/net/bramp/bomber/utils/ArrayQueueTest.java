package net.bramp.bomber.utils;

import junit.framework.Assert;

import org.junit.Test;

public class ArrayQueueTest {

	final int MAX = 128;

	@Test
	public void test() {
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

}
