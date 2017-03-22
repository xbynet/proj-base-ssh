package net.xby1993.common.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class StringUtils {
	public static final boolean checkNotEmpty(String str){
		if(str!=null&&!str.trim().equals("")){
			return true;
		}
		return false;
	}
	public static final boolean checkStringArray(String[] strs){
		for(String str:strs){
			if(!checkNotEmpty(str)){
				return false;
			}
		}
		return true;
	}
	public static final boolean checkStrings(String... strs){
		return checkStringArray(strs);
	}
	public static final String join(String[] strs,String split){
		String tmp="";
		for(String str:strs){
			if(tmp.equals("")){
				tmp=str;
			}else{
				tmp+=split+str;
			}
		}
		return tmp;
	}
	/**
	 * 为了免去考虑null的烦恼
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static final boolean checkEquals(String str1,String str2){
		if(str1==null&&str2==null) return true;
		if(str1!=null){
			return str1.equals(str2);
		}
		return false;
	}
	public static final boolean checkObjectArray(Object[] objs){
		if(objs!=null&&objs.length!=0){
			return true;
		}
		return false;
	}
	public static final boolean  checkCollection(Collection col){
		if(col!=null&&col.size()!=0){
			return true;
		}
		return false;
	}
	public static final boolean  checkMap(Map map){
		if(map!=null&&(!map.isEmpty())){
			return true;
		}
		return false;
	}
	/**将集合转变了逗号分隔的字符串*/
	public static final String toCommaString(Collection<String> c){
		String commaStr="";
		for(String str:c){
			commaStr=commaStr+str+",";
		}
		commaStr=commaStr.substring(0, commaStr.length()-1);
		return commaStr;
	}
	/**将数组转变了逗号分隔的字符串*/
	public static final String toCommaString(String[] c){
		String commaStr="";
		for(int i=0;i<c.length;i++){
			commaStr=commaStr+c[i]+",";
		}
		commaStr=commaStr.substring(0, commaStr.length()-1);
		return commaStr;
	}

	/**
	 * 根据指定格式获取日期字符串
	 * @param format
	 * @return
	 */
	public static final String getNowDate(String format){
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		Date date=new Date();
		return sdf.format(date);
	}

	/**
	 * 获取pre天之前的日期
	 * @param format
	 * @param pre
	 * @return
	 */
	public static final String getPreDate(String format,int pre){
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		Date d=new Date();
		//获取pre天之前的日期
		Date date=new Date(d.getTime()-pre*24*60*60*1000);
		return sdf.format(date);
	}
	/**
	 * 数据库下划线列名转驼峰
	 * @param name
	 * @return
	 */
	public static final String columnToFieldName(String name){
		if(name.contains("_")&&(name.lastIndexOf("_")+1)!=name.length()){
			String[] n1=name.toLowerCase().split("_");
			StringBuilder stb=new StringBuilder();
			for(int i=0;i<n1.length;i++){
				if(i==0){
					stb.append(n1[i]);
				}else{
					if(n1.length>1){
						String cap=n1[i].substring(0, 1).toUpperCase();
						String str=n1[i].substring(1);
						stb.append(cap+str);
					}else{
						stb.append(n1[i].toUpperCase());
					}
				}
				
			}
			return stb.toString();
		}
		return name;
	}
	public static void main(String[] args) {
		System.out.println(columnToFieldName("is_aasAasd"));
		System.out.println(toCommaString(new String[]{"aaa","bbb","ccc"}));
		List<String> list=new ArrayList<>();
		list.add("aaa");
		list.add("bbb");
		list.add("c");
		System.out.println(toCommaString(list));
		System.out.println("aaa".split(",")[0]);
	}
}
