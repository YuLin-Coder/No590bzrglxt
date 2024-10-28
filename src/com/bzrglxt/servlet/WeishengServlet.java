package com.bzrglxt.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bzrglxt.utils.DbUtil;
import com.bzrglxt.vo.TWeisheng;

public class WeishengServlet extends HttpServlet{
	public void service(HttpServletRequest req,HttpServletResponse res)throws ServletException, IOException 
	{
		String type=req.getParameter("type");
		if(type.endsWith("weishengAdd"))
		{
			weishengAdd(req, res);
		}
		if(type.endsWith("weishengUpd"))
		{
			weishengUpd(req, res);
		}
		if(type.endsWith("weishengDel"))
		{
			weishengDel(req, res);
		}
		if(type.endsWith("weishengMana"))
		{
			weishengMana(req, res);
		}
	}
	
	public void weishengAdd(HttpServletRequest req,HttpServletResponse res){
		String sushe = req.getParameter("sushe");
		String riqi = req.getParameter("riqi");
		String fenshu = req.getParameter("fenshu");
		String shuoming = req.getParameter("shuoming");
		int del = 0;
		
		String sql="insert into t_weisheng values(null,?,?,?,?,?)";
		Object[] params={sushe,riqi,fenshu,shuoming,del};
		DbUtil mydb=new DbUtil();
		mydb.doUpdate(sql, params);
		mydb.closed();
		
		req.setAttribute("message", "卫生检查信息保存成功!");
		req.setAttribute("path", "weisheng?type=weishengMana");
		
        String targetURL = "/showMsg.jsp";
		dispatch(targetURL, req, res);
	}
	public void weishengUpd(HttpServletRequest req,HttpServletResponse res){
		int id = Integer.parseInt(req.getParameter("id"));
		String sushe = req.getParameter("sushe");
		String riqi = req.getParameter("riqi");
		String fenshu = req.getParameter("fenshu");
		String shuoming = req.getParameter("shuoming");
		
		String sql = "update t_weisheng set sushe=?,riqi=?,fenshu=?,shuoming=? where id=?";
		
		Object[] params={sushe,riqi,fenshu,shuoming,id};
		DbUtil mydb=new DbUtil();
		mydb.doUpdate(sql, params);
		mydb.closed();
		
		req.setAttribute("message", "卫生检查信息修改成功!");
		req.setAttribute("path", "weisheng?type=weishengMana");
		
        String targetURL = "/showMsg.jsp";
		dispatch(targetURL, req, res);
	}
	public void weishengDel(HttpServletRequest req,HttpServletResponse res){
		int id = Integer.parseInt(req.getParameter("id"));
		int del = 1;
		
		String sql = "update t_weisheng set del=? where id=?";
		
		Object[] params={del,id};
		DbUtil mydb=new DbUtil();
		mydb.doUpdate(sql, params);
		mydb.closed();
		
		req.setAttribute("message", "卫生检查信息删除成功!");
		req.setAttribute("path", "weisheng?type=weishengMana");
		
        String targetURL = "/showMsg.jsp";
		dispatch(targetURL, req, res);
		
	}
	public void weishengMana(HttpServletRequest req,HttpServletResponse res)throws ServletException, IOException{
		List weishengList = new ArrayList();
		
		String sql = "select * from t_weisheng where del=?";
		
		Object[] params={0};
		DbUtil mydb=new DbUtil();
		try
		{
			mydb.doQuery(sql, params);
			ResultSet rs=mydb.getRs();
			while(rs.next())
			{
				TWeisheng leixing=new TWeisheng();
				leixing.setId(rs.getInt("id"));
				leixing.setSushe(rs.getString("sushe"));
				leixing.setRiqi(rs.getString("riqi"));
				leixing.setFenshu(rs.getString("fenshu"));
				leixing.setShuoming(rs.getString("shuoming"));
				
				weishengList.add(leixing);
			}
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		mydb.closed();
		
		req.setAttribute("weishengList", weishengList);
		req.getRequestDispatcher("/weisheng/weishengMana.jsp").forward(req, res);
	}
	
	public void dispatch(String targetURI,HttpServletRequest request,HttpServletResponse response) 
	{
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher(targetURI);
		try 
		{
		    dispatch.forward(request, response);
		    return;
		} 
		catch (ServletException e) 
		{
                    e.printStackTrace();
		} 
		catch (IOException e) 
		{
			
		    e.printStackTrace();
		}
	}
}
