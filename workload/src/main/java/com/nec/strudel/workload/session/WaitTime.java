package com.nec.strudel.workload.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.concurrent.Immutable;

import com.nec.strudel.workload.util.TimeValue;


@Immutable
public class WaitTime {
	static final WaitTimer NO_WAIT = new ConstantWait(0);
	/**
	 * Builds a wait time specification. The default, generated
	 * by new Builds().build(), is "no wait."
	 * @author tatemura
	 *
	 */
	public static class Builder {
		private final Map<String, WaitTimer> prepareMap =
			new HashMap<String, WaitTime.WaitTimer>();
		private final Map<String, WaitTimer> thinkMap =
			new HashMap<String, WaitTime.WaitTimer>();
		private WaitTimer prepare = NO_WAIT;
		private WaitTimer think = NO_WAIT;

		public Builder prepareConstant(long msec) {
			prepare = new ConstantWait(msec);
			return this;
		}
		public Builder prepareExponential(long mean, long max) {
			prepare = new ExponentialWait(mean, max);
			return this;
		}
		public Builder prepare(WaitTimer timer) {
			prepare = timer;
			return this;
		}
		public Builder thinkConstant(long msec) {
			think = new ConstantWait(msec);
			return this;
		}
		public Builder thinkExponential(long mean, long max) {
			think = new ExponentialWait(mean, max);
			return this;
		}
		public Builder think(WaitTimer timer) {
			think = timer;
			return this;
		}
		public Builder prepareConstant(String name, long msec) {
			prepareMap.put(name, new ConstantWait(msec));
			return this;
		}
		public Builder prepareExponential(String name,
		        int mean, int max) {
			prepareMap.put(name, new ExponentialWait(mean, max));
			return this;
		}
		public Builder prepare(String name, WaitTimer timer) {
			prepareMap.put(name, timer);
			return this;
		}
		public Builder think(String name, WaitTimer timer) {
			thinkMap.put(name, timer);
			return this;
		}
		public Builder set(ThinkTime time) {
			prepare(time.getBeforeTimer());
			think(time.getAfterTimer());
			return this;
		}
		public Builder set(String name, ThinkTime time) {
			if (time.hasBefore()) {
				prepare(name, time.getBeforeTimer());
			}
			if (time.hasAfter()) {
				think(name, time.getAfterTimer());
			}
			return this;
		}
		public Builder thinkConstant(String name, long msec) {
			thinkMap.put(name, new ConstantWait(msec));
			return this;
		}
		public Builder thinkExponential(String name,
		        long mean, long max) {
			thinkMap.put(name, new ExponentialWait(mean, max));
			return this;
		}

		public WaitTime build() {
			return new WaitTime(prepareMap, prepare,
					thinkMap, think);
		}
	}
	public static WaitTimer createConstant(long msec) {
		return new ConstantWait(msec);
	}
	public static WaitTimer createConstant(TimeValue time) {
		return new ConstantWait(time);
	}
	public static WaitTimer createExponential(long mean, long max) {
		return new ExponentialWait(mean, max);
	}
	public static WaitTimer createExponential(TimeValue mean, TimeValue max) {
		return new ExponentialWait(mean, max);
	}

	private final Map<String, WaitTimer> prepareMap;
	private final Map<String, WaitTimer> thinkMap;

	private final WaitTimer prepare;
	private final WaitTimer think;
	public WaitTime(Map<String, WaitTimer> prepareMap,
			WaitTimer prepare, Map<String, WaitTimer> thinkMap,
			WaitTimer think) {
		this.prepareMap = prepareMap;
		this.prepare = prepare;
		this.thinkMap = thinkMap;
		this.think = think;
	}
	public long prepareTime(String name, Random rand) {
		WaitTimer w = prepareMap.get(name);
		if (w != null) {
			return w.getTime(rand);
		}
		return prepare.getTime(rand);
	}
	public long thinkTime(String name, Random rand) {
		WaitTimer w = thinkMap.get(name);
		if (w != null) {
			return w.getTime(rand);
		}
		return think.getTime(rand);
	}

	interface WaitTimer {
		long getTime(Random rand);
	}

	static class ConstantWait implements WaitTimer {
		private final long time;
		public ConstantWait(long time) {
			this.time = time;
		}
		public ConstantWait(TimeValue time) {
			this.time = time.toMillis();
		}
		@Override
		public long getTime(Random rand) {
			return time;
		}
	}
	static class ExponentialWait implements WaitTimer {
		private final long meanTime;
		private final long maxTime;
		public ExponentialWait(long meanTime, long maxTime) {
			this.meanTime = meanTime;
			this.maxTime = maxTime;
		}
		public ExponentialWait(TimeValue meanTime, TimeValue maxTime) {
			this.meanTime = meanTime.toMillis();
			this.maxTime = maxTime.toMillis();
		}
		@Override
		public long getTime(Random rand) {
			long time = -(long) (meanTime
			        * Math.log(rand.nextDouble()));
			if (maxTime > 0 && time > maxTime) {
				return maxTime;
			}
			return time;
		}
	}
}
