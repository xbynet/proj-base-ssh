package net.xby1993.common.util;

import java.sql.Timestamp;

import org.springframework.core.convert.converter.Converter;

public class TimestampConverter implements Converter<String, Timestamp>{

	@Override
	public Timestamp convert(String timeStr) {
		// TODO Auto-generated method stub
		Timestamp t=null;
		if(StringUtils.checkNotEmpty(timeStr)){
			long time=Long.valueOf(timeStr);
			t=new Timestamp(time);
		}
		return t;
	}

}
