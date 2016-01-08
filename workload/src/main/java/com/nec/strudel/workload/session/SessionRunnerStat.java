package com.nec.strudel.workload.session;

import java.util.List;

import com.nec.strudel.management.resource.Getter;
import com.nec.strudel.management.resource.ManagedResource;
import com.nec.strudel.workload.session.runner.RunnerStat;
import com.nec.strudel.workload.session.runner.SessionStatMonitor;

@ManagedResource(description = "monitors the state of session runners")
public class SessionRunnerStat {
	private final RunnerStat stat;
	private final int numOfThreads;
	private final SessionStatMonitor mon;
	public SessionRunnerStat(List<RunnerStat> runners,
			SessionStatMonitor mon) {
		this.stat = new MultiThreadRunnerStat(runners);
		this.numOfThreads = runners.size();
		this.mon = mon;
	}
	public SessionRunnerStat(int sessionConcurrency,
			int numOfThreads,
			SessionStatMonitor mon) {
		this.stat = new FixedRunnerStat(
				sessionConcurrency);
		this.numOfThreads = numOfThreads;
		this.mon = mon;
	}
	@Getter
	public int getNumOfThreads() {
		return numOfThreads;
	}

	@Getter
	public double getAvgSessionConcurrency() {
		int len = stat.getSessionConcurrency();
		return ((double) len) / numOfThreads;
	}

	@Getter
	public double getSuccessRatio() {
		return mon.getSuccessRatio();
	}

	@Getter
	public double getAvgInteractionTime() {
		return mon.getAverageInteractionTime();
	}

	@Getter
	public double getInteractionsPerSec() {
		return mon.getInteractionsPerSec();
	}

	@Getter
	public double getNewSessionsPerSec() {
		return mon.getNewSessionsPerSec();
	}
	static class FixedRunnerStat implements RunnerStat {
		private final int sessionConcurrency;
		public FixedRunnerStat(int sessionConcurrency) {
			this.sessionConcurrency = sessionConcurrency;
		}
		@Override
		public int getSessionConcurrency() {
			return sessionConcurrency;
		}
		
	}
	static class MultiThreadRunnerStat implements RunnerStat {
		private final List<RunnerStat> runners;
		public MultiThreadRunnerStat(List<RunnerStat> runners) {
			this.runners = runners;
		}
		@Override
		public int getSessionConcurrency() {
			int len = 0;
			for (RunnerStat r : runners) {
				len += r.getSessionConcurrency();
			}
			return len;
		}
		
	}
}
