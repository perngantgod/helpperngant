package com.jun.pregnancy.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EstimateModel {

	/** caculate the day of Ovulation day */
	public static long CaculateOvulationBy14(int period, int cycles,
			long startlastcoming) {
		return startlastcoming + ((long) cycles) * 24 * 60 * 60 * 1000
				- (long) 14 * 24 * 60 * 60 * 1000;

	}

	public static long CaculateOvulationBy14AndTemp(long esitimateday,
			float[] tempvalues) {

		return 0;
	}

	public static long CaculateOvulationBy14AndTempAndLH(long esitimateday,
			float[] lhvalues) {

		return 0;
	}

	public static long CaculateOvulationBy14AndLH(long esitimateday,
			float[] lhvalues) {

		return 0;
	}

	/**
	 * 
	 * @param period
	 * @param cycles
	 * @param startlastcoming
	 * @return
	 */
	public static long[] CaculateMilestonesBy14(int period, int cycles,
			long startlastcoming) {
		long[] results = new long[5];
		results[0] = startlastcoming + ((long) cycles) * 24 * 60 * 60 * 1000; // next
																				// date
		results[1] = results[0] - (long) 14 * 24 * 60 * 60 * 1000; // ova
		results[2] = startlastcoming + ((long) period) * 24 * 60 * 60 * 1000; // end
																				// menstruation
		results[3] = results[1] - (long) 5 * 24 * 60 * 60 * 1000; // start easy
																	// for
																	// prengancy
		results[4] = results[1] + (long) 4 * 24 * 60 * 60 * 1000; // end of easy
																	// for
																	// prengancy
		results[5] = startlastcoming;

		return results;
	}

	/** Caculate the period state of today */
	/**
	 * 
	 * @param period_states
	 * @return 1: startDay 2: menstruation days 3: safe day 4: easy to be
	 *         pregnancy 5:Ovulation day 6: nextDay
	 */

	public static int CaculatePeriodOfDay(long[] period_states, long currentdate) {
		int currentstate = 0;
		if (currentdate == period_states[5])
			currentstate = 1; // start day
		else if (currentdate > period_states[5]
				&& currentdate < period_states[2]) {
			currentstate = 2;
		} else if (currentdate >= period_states[2]
				&& currentdate < period_states[3]) {
			currentstate = 3;
		} else if (currentdate >= period_states[3]
				&& currentdate < period_states[4]) {
			if (currentdate >= period_states[1])
				currentstate = 5;
			else
				currentstate = 4;
		} else if (currentdate >= period_states[4]
				&& currentdate < period_states[0]) {
			currentstate = 3;
		} else if (currentdate == period_states[0]) {
			currentstate = 6;
		}
		return currentstate;

	}

	public static long getCurrentDate() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.parse(sdf.format(new Date())).getTime();
	}
}
