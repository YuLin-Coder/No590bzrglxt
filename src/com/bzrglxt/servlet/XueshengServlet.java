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
import com.bzrglxt.vo.TXuesheng;

public class XueshengServlet extends HttpServlet{
	public void service(HttpServletRequest req,HttpServletResponse res)throws ServletException, IOException 
	{
		String type=req.getParameter("type");
		if(type.endsWith("xueshengAdd"))
		{
			xueshengAdd(req, res);
		}
		if(type.endsWith("xueshengMana"))
		{
			xueshengMana(req, res);
		}
		if(type.endsWith("xueshengUpd"))
		{
			xueshengUpd(req, res);
		}
		if(type.endsWith("xueshengToUpd"))
		{
			xueshengToUpd(req, res);
		}
		if(type.endsWith("xueshengDele"))
		{
			xueshengDele(req, res);
		}
	}
	
	public void xueshengAdd(HttpServletRequest req,HttpServletResponse res){
		String xuehao = req.getParameter("xuehao");
		String xingming = req.getParameter("xingming");
		String xingbie = req.getParameter("xingbie");
		String ruxueriqi = req.getParameter("ruxueriqi");
		String zhuanye = req.getParameter("zhuanye");
		String dianhua = req.getParameter("dianhua");
		String jiguan = req.getParameter("jiguan");
		int del = 0;
		
		String sql="insert into t_xuesheng values(null,?,?,?,?,?,?,?,?)";
		Object[] params={xuehao,xingming,xingbie,ruxueriqi,zhuanye,dianhua,jiguan,del};
		DbUtil mydb=new DbUtil();
		mydb.doUpdate(sql, params);
		mydb.closed();
		
		req.setAttribute("message", "学生信息添加成功!");
		req.setAttribute("path", "xuesheng?type=xueshengMana");
		
        String targetURL = "/showMsg.jsp";
		dispatch(targetURL, req, res);
	}
	public void xueshengToUpd(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		int id = Integer.parseInt(req.getParameter("id"));
		
		TXuesheng xuesheng = new TXuesheng();
		
		String sql="select * from t_xuesheng where id = ?";
		Object[] params={id};
		DbUtil mydb=new DbUtil();
		try
		{
			mydb.doQuery(sql, params);
			ResultSet rs=mydb.getRs();
			while(rs.next())
			{
				xuesheng.setId(rs.getInt("id"));
				xuesheng.setXuehao(rs.getString("xuehao"));
				xuesheng.setXingming(rs.getString("xingming"));
				xuesheng.setXingbie(rs.getString("xingbie"));
				xuesheng.setRuxueriqi(rs.getString("ruxueriqi"));
				xuesheng.setZhuanye(rs.getString("zhuanye"));
				xuesheng.setDianhua(rs.getString("dianhua"));
				xuesheng.setJiguan(rs.getString("jiguan"));
			}
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		mydb.closed();
		
		req.setAttribute("xuesheng", xuesheng);
		req.getRequestDispatcher("/xuesheng/xueshengEdit.jsp").forward(req, res);
	}
	public void xueshengMana(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		List xueshengList = new ArrayList();
		String sql="select * from t_xuesheng where del = 0";
		Object[] params={};
		DbUtil mydb=new DbUtil();
		try
		{
			mydb.doQuery(sql, params);
			ResultSet rs=mydb.getRs();
			while(rs.next())
			{
				TXuesheng xuesheng = new TXuesheng();
				xuesheng.setId(rs.getInt("id"));
				xuesheng.setXuehao(rs.getString("xuehao"));
				xuesheng.setXingming(rs.getString("xingming"));
				xuesheng.setXingbie(rs.getString("xingbie"));
				xuesheng.setRuxueriqi(rs.getString("ruxueriqi"));
				xuesheng.setZhuanye(rs.getString("zhuanye"));
				xuesheng.setDianhua(rs.getString("dianhua"));
				xuesheng.setJiguan(rs.getString("jiguan"));
				
				xueshengList.add(xuesheng);
			}
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		mydb.closed();
		
		req.setAttribute("xueshengList", xueshengList);
		req.getRequestDispatcher("/xuesheng/xueshengMana.jsp").forward(req, res);
	}
	public void xueshengUpd(HttpServletRequest req,HttpServletResponse res){
		String xuehao = req.getParameter("xuehao");
		String xingming = req.getParameter("xingming");
		String xingbie = req.getParameter("xingbie");
		String ruxueriqi = req.getParameter("ruxueriqi");
		String zhuanye = req.getParameter("zhuanye");
		String dianhua = req.getParameter("dianhua");
		String jiguan = req.getParameter("jiguan");
		
		String id = req.getParameter("id");
		
		String sql="update t_xuesheng set xuehao=?,xingming=?,xingbie=?,ruxueriqi=?," +
				   "zhuanye=?,dianhua=?,jiguan=? where id=?";
		Object[] params={xuehao,xingming,xingbie,ruxueriqi,zhuanye,dianhua,jiguan,id};
		DbUtil mydb=new DbUtil();
		mydb.doUpdate(sql, params);
		mydb.closed();
		
		req.setAttribute("message", "学生信息修改成功!");
		req.setAttribute("path", "xuesheng?type=xueshengMana");
		
        String targetURL = "/showMsg.jsp";
		dispatch(targetURL, req, res);
	}
	
	public void xueshengDele(HttpServletRequest req,HttpServletResponse res){
		int id = Integer.parseInt(req.getParameter("id"));
		int del = 1;
		
		String sql="update t_xuesheng set del=? where id=?";
		
		Object[] params={del,id};
		DbUtil mydb=new DbUtil();
		mydb.doUpdate(sql, params);
		mydb.closed();
		
		req.setAttribute("message", "学生信息删除成功!");
		req.setAttribute("path", "xuesheng?type=xueshengMana");
		
		String targetURL = "/showMsg.jsp";
		dispatch(targetURL, req, res);
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
