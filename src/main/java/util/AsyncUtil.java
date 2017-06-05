package util;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncUtil {
	private static final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);

	public static void doTask(Task task, int interval, int times) {
		doTask(new PeriodicTask(task, interval, times));
	}

	private static void doTask(PeriodicTask task) {
		executor.schedule(task, task.interval, TimeUnit.SECONDS);
	}

	public synchronized static void setPoolSize(int size) {
		executor.setCorePoolSize(size);
	}

	private static class PeriodicTask implements Runnable {
		private Task task;
		private int interval;
		private int times;

		public PeriodicTask(Task task, int interval, int times) {
			super();
			this.task = task;
			this.interval = interval;
			this.times = times;
		}

		public void run() {
			times--;
			boolean needToRunAgain = task.task();
			if (needToRunAgain) {
				if (times > 0)
					AsyncUtil.doTask(this);
				else
					task.failureCallBack();
			}
		}

	}
}
