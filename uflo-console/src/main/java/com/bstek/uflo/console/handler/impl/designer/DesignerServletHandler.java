package com.bstek.uflo.console.handler.impl.designer;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.bstek.uflo.console.handler.impl.RenderPageServletHandler;
import com.bstek.uflo.console.provider.ProcessFile;
import com.bstek.uflo.console.provider.ProcessProvider;
import com.bstek.uflo.console.provider.ProcessProviderUtils;
import com.bstek.uflo.deploy.parse.impl.ProcessParser;
import com.bstek.uflo.env.AdditionalInfoProvider;
import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.service.ProcessService;

/**
 * @author Jacky.gao
 * @since 2017年7月5日
 */
public class DesignerServletHandler extends RenderPageServletHandler {
	private ProcessService processService;
	private AdditionalInfoProvider additionalInfoProvider;
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method=retriveMethod(req);
		if(method!=null){
			invokeMethod(method, req, resp);
		}else{
			VelocityContext context = new VelocityContext();
			context.put("contextPath", req.getContextPath());
			if(additionalInfoProvider!=null) {
				List<String> categories=additionalInfoProvider.categories();
				if(categories!=null && categories.size()>0) {
					StringBuilder sb=new StringBuilder();
					for(String category:categories) {
						if(sb.length()>0) {
							sb.append(",");
						}
						sb.append("\""+category+"\"");
					}
					context.put("categories", sb.toString());
				}
			}else {
				context.put("categories", "");
			}
			resp.setContentType("text/html");
			resp.setCharacterEncoding("utf-8");
			Template template=ve.getTemplate("uflo-html/designer.html","utf-8");
			PrintWriter writer=resp.getWriter();
			template.merge(context, writer);
			writer.close();
		}
	}
	
	public void deploy(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String content=req.getParameter("content");
		content=decode(content);
		InputStream inputStream=IOUtils.toInputStream(content, "utf-8");
		processService.deployProcess(inputStream);
		IOUtils.closeQuietly(inputStream);
	}
	
	public void openFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name=req.getParameter("name");
		name=decode(name);
		ProcessProvider targetProvider=ProcessProviderUtils.getProcessProvider(name);
		if(targetProvider==null){
			throw new RuntimeException("Unsupport file : "+name);
		}
		InputStream inputStream=targetProvider.loadProcess(name);
		try{
			byte[] bytes=IOUtils.toByteArray(inputStream);
			ProcessDefinition process=ProcessParser.parseProcess(bytes, 0, true);
			writeObjectToJson(resp, process);
		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}finally{
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	public void deleteFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName=req.getParameter("fileName");
		fileName=decode(fileName);
		ProcessProvider provider=ProcessProviderUtils.getProcessProvider(fileName);
		provider.deleteProcess(fileName);
	}
	public void saveFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName=req.getParameter("fileName");
		String content=req.getParameter("content");
		content=decode(content);
		ProcessProvider provider=ProcessProviderUtils.getProcessProvider(fileName);
		provider.saveProcess(fileName, content);
	}
	
	public void loadProcessProviders(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<ProcessProvider> providers=ProcessProviderUtils.getProviders();
		writeObjectToJson(resp, providers);
	}
	
	public void loadProcessProviderFiles(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String providerName=req.getParameter("name");
		if(StringUtils.isBlank(providerName)){
			throw new RuntimeException("Process provider name can not be null.");
		}
		ProcessProvider targetProcessProvider=ProcessProviderUtils.getProcessProviderByName(providerName);
		List<ProcessFile> files=targetProcessProvider.loadAllProcesses();
		writeObjectToJson(resp, files);
	}
	
	public void setProcessService(ProcessService processService) {
		this.processService = processService;
	}
	
	private String decode(String str){
		if(str==null)return str;
		try {
			str=URLDecoder.decode(str,"utf-8");
			str=URLDecoder.decode(str,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		super.setApplicationContext(applicationContext);
		Collection<AdditionalInfoProvider> coll=applicationContext.getBeansOfType(AdditionalInfoProvider.class).values();
		if(coll.size()>0) {
			additionalInfoProvider=coll.iterator().next();
		}
	}

	@Override
	public String url() {
		return "/designer";
	}
}
