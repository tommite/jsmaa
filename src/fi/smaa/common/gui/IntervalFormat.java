package fi.smaa.common.gui;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

import fi.smaa.common.Interval;


public class IntervalFormat extends Format {

	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo,
			FieldPosition pos) {
		Interval interval = (Interval) obj;
		if (interval != null) {
			toAppendTo.append(interval.toString());
		}
		// TODO fix to be "correct"
		return toAppendTo;
	}

	@Override
	public Object parseObject(String source, ParsePosition pos) {
		// TODO implement
		return null;
	}

}
