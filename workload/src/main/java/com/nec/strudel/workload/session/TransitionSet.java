package com.nec.strudel.workload.session;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.nec.congenio.annotation.MapOf;

@MapOf(TransitionSet.TransitionConfig.class)
public class TransitionSet {
	public static TransitionSet empty() {
		return new TransitionSet();
	}

	private final Map<String, TransitionConfig> trans;

	public TransitionSet(Map<String, TransitionConfig> trans) {
		this.trans = trans;
	}

	public TransitionSet() {
		this.trans = Collections.emptyMap();
	}
	public boolean isEmpty() {
		return trans.isEmpty();
	}
	public Set<String> names() {
		return trans.keySet();
	}
	public Map<String, Double> nexts(String name) {
		TransitionConfig tr = trans.get(name);
		return tr.nexts();
	}
	@MapOf(NextStep.class)
	public static class TransitionConfig {
		private Map<String, NextStep> steps;
		public TransitionConfig(Map<String, NextStep> steps) {
			this.steps = steps;
		}
		public Map<String, Double> nexts() {
			Map<String, Double> nexts =
					new HashMap<String, Double>();
			for (Map.Entry<String, NextStep> n : steps.entrySet()) {
				String next = n.getKey();
				double prob = n.getValue().getProb();
				nexts.put(next, prob);
			}
			return nexts;
		}
	}
	public static class NextStep {
		private double prob = 1;
		public double getProb() {
			return prob;
		}
		public void setProb(double prob) {
			this.prob = prob;
		}
	}
}