package com.nec.strudel.workload.session;

import javax.annotation.Nullable;

import com.nec.strudel.session.impl.State;



public abstract class SessionBaseImpl<T> implements Session<T> {
	private int count;
	private long startTime = 0;
	private final int maxCount;
	private final long maxTime;
	private UserAction<T> current;
	/**
	 * @param maxCount the max number of interactions in
	 * a session. A non-positive number indicates that there
	 * is no max limit.
	 * @param maxTime The max time to execute a session (in msec).
	 * A non-positive number indicates that there is no max limit.
	 */
	public SessionBaseImpl(int maxCount, long maxTime) {
		this.maxCount = maxCount;
		this.maxTime = maxTime;
	}

	public int getMaxCount() {
		return maxCount;
	}
	public long getMaxTime() {
		return maxTime;
	}
	@Override
	public UserAction<T> next(State state) {
		if (current == null) {
			startTime = System.currentTimeMillis();
			count = 1;
		} else {
			if (maxCount > 0 && count >= maxCount) {
				return null;
			}
			count++;
			if (maxTime > 0) {
				long time = System.currentTimeMillis()
				            - startTime;
				if (time >= maxTime) {
					return null;
				}
			}
		}
		if (current == null) {
			current = getFirst(state);
		} else {
			current = getNext(current, state);
		}
		return current;
	}

	/**
	 * The time when the first interaction
	 * is generated.
	 * @return 0 if it has generated none.
	 */
	protected long startTime() {
		return this.startTime;
	}
	/**
	 * Gets the first interaction to execute
	 * @param state the current state of the session
	 * @return null if the session ends
	 */
	@Nullable
	protected abstract UserAction<T> getFirst(State state);
	/**
	 * Gets the next interaction to execute
	 * @param current the last interaction executed
	 * @param state the current state of the session
	 * @return null if the session ends
	 */
	@Nullable
	protected abstract UserAction<T> getNext(
	        UserAction<T> current, State state);
}
