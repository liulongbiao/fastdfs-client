/**
 * 
 */
package cn.strong.fastdfs.utils;

import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * 种子
 * 
 * @author liulongbiao
 *
 */
public interface Seed<T> {
	
	int PICK_ROUND_ROBIN = 0; // 顺序选择
	int PICK_RANDOM = 1; // 随机选择
	int PICK_FIRST = 2; // 选择第一个

	/**
	 * 创建种子
	 * 
	 * @param list
	 * @param strategy
	 * @return
	 */
	public static <E> Seed<E> create(List<E> list, int strategy) {
		Objects.requireNonNull(list);
		if(list.size() == 1) {
			return new FirstSeed<>(list);
		}
		switch (strategy) {
		case PICK_ROUND_ROBIN:
			return new RoundRobinSeed<>(list);
		case PICK_RANDOM:
			return new RandomSeed<>(list);
		case PICK_FIRST:
			return new FirstSeed<>(list);
		default:
			return new RoundRobinSeed<>(list);
		}
	}

	/**
	 * 选择一项
	 * 
	 * @return
	 */
	T pick();

	/**
	 * 顺序选择种子
	 * 
	 * @author liulongbiao
	 *
	 * @param <E>
	 */
	static class RoundRobinSeed<E> implements Seed<E> {

		final List<E> list;
		private int idx;

		public RoundRobinSeed(List<E> list) {
			this.list = Objects.requireNonNull(list);
		}

		@Override
		public E pick() {
			idx %= list.size();
			return list.get(idx++);
		}

	}

	/**
	 * 随机选择种子
	 * 
	 * @author liulongbiao
	 *
	 * @param <E>
	 */
	static class RandomSeed<E> implements Seed<E> {

		final List<E> list;
		private Random rdm = new Random();

		public RandomSeed(List<E> list) {
			this.list = Objects.requireNonNull(list);
		}

		@Override
		public E pick() {
			return list.get(rdm.nextInt(list.size()));
		}

	}

	/**
	 * 始终返回第一个种子
	 * 
	 * @author liulongbiao
	 *
	 * @param <E>
	 */
	static class FirstSeed<E> implements Seed<E> {

		final List<E> list;

		public FirstSeed(List<E> list) {
			this.list = Objects.requireNonNull(list);
		}

		@Override
		public E pick() {
			return list.get(0);
		}

	}
}
