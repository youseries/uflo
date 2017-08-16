package com.bstek.uflo.utils;

/**
 * @author Jacky.gao
 * @since 2017年8月16日
 */
public class Splash {
	public void print(){
		StringBuilder sb=new StringBuilder();
		sb.append("_____  __________________ _______ ______ ");
		sb.append("\n");
		sb.append("__  / / /___  ____/___  / __  __ \\__|__ \\");
		sb.append("\n");
		sb.append("_  / / / __  /_    __  /  _  / / /____/ /");
		sb.append("\n");
		sb.append("/ /_/ /  _  __/    _  /___/ /_/ / _  __/ ");
		sb.append("\n");
		sb.append("\\____/   /_/       /_____/\\____/  /____/ ");
		sb.append("\n");
		sb.append(".....................................................................................................");
		sb.append("\n");
		sb.append(".  uFlo, is a Chinese style process engine");
		sb.append(" licensed under the Apache License 2.0,                   .");
		sb.append("\n");
		sb.append(".  which is opensource, free of charge, easy to use,");
		sb.append("high-performance, with browser-based-designer.  .");
		sb.append("\n");
		sb.append(".....................................................................................................");
		sb.append("\n");
		System.out.println(sb.toString());
	}
	public static void main(String[] args) {
		Splash s=new Splash();
		s.print();
	}
}
