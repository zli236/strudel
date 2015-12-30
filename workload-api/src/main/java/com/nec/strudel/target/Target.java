package com.nec.strudel.target;

import com.nec.strudel.Closeable;
import com.nec.strudel.instrument.Instrumented;
import com.nec.strudel.instrument.ProfilerService;

/**
 * A class that provides access for the workload
 * to the target (e.g. a database).
 *
 * @param <T> an implementation specific access method
 * to the target.
 */
public interface Target<T> extends Closeable {

	/**
	 * Creates an access to the load target.
	 * @return am access handling object (e.g., connection).
	 */
	T open();

	Instrumented<T> open(ProfilerService profs);

	/**
	 * Prepare to use the target. In a workload task,
	 * it is often the case that an opened target is
	 * kept used for long time to execute multiple
	 * sub tasks (e.g. sessions, transactions), each of
	 * which is independent from the others. This method
	 * is called when the workload task begins a new
	 * independent sub task.
	 * <p>
	 * The motivation of this method is to let the implementation
	 * have chance to clear some resources. For example,
	 * EntityManager (JPA) maintains the attached entities as
	 * its internal state. If the target is EntityManager
	 * (Target&lt;EntityManager&gt;), keeping one opened target
	 * and reusing it for long time may end up with memory
	 * leak - i.e., EntityManager preserves entities of past
	 * tasks that are no longer used.
	 * @param target
	 */
	void beginUse(T target);
	/**
	 * End to use the target. In a workload task,
	 * it is often the case that an opened target is
	 * kept used for long time to execute multiple
	 * sub tasks (e.g. sessions, transactions), each of
	 * which is independent from the others. This method
	 * is called when the workload task ends the current
	 * independent sub task that is using this target.
	 */
	void endUse(T target);
}
