package com.bzrglxt.servlet;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bzrglxt.utils.DbUtil;
import com.bzrglxt.vo.User;

public class LoginServletr extends HttpServlet{
	public void service(HttpServletRequest req,HttpServletResponse res)throws ServletException, IOException{
		String type=req.getParameter("type");
		if(type.endsWith("login"))
		{
			login(req, res);
		}
		if(type.endsWith("changePass"))
		{
			changePass(req, res);
		} 
	}
	
	public void changePass(HttpServletRequest req,HttpServletResponse res)throws ServletException, IOException{
		String oldPass = req.getParameter("oldPass");
		String newPass = req.getParameter("newPass");
		
		User user = (User)req.getSession().getAttribute("user");
		if(user.getLoginPass().equals(oldPass)){
			String sql = "";
			if(user.getType()==0){
				sql = "update t_admin set userPw = '"+newPass+"' ";
			}else{
				sql = "update t_jsxx set loginPass = '"+newPass+"' where id = "+user.getId();
			}
			DbUtil mydb=new DbUtil();
			mydb.doUpdate(sql, null);
			mydb.closed();
			
			user.setLoginPass(newPass);
			req.getSession().setAttribute("user", user);
			
			req.setAttribute("message", "操作成功");
			req.setAttribute("path", "changePw.jsp");
			
	        String targetURL = "/showMsg.jsp";
			dispatch(targetURL, req, res);

		}else{
			req.setAttribute("message", "旧密码错误!");
			req.setAttribute("path", "changePw.jsp");
			
	        String targetURL = "/showMsg.jsp";
			dispatch(targetURL, req, res);
		}
	}
	
	public void login(HttpServletRequest req,HttpServletResponse res)throws ServletException, IOException{
		String dlsf = req.getParameter("dlsf");
		
		boolean result = false;
		if("1".equals(dlsf)){
			//教职工登录
			result = jsLogin(req);
		}else{
			//管理员登录
			result = adminLogin(req);
		}
		if(result){
			req.getRequestDispatcher("/index.jsp").forward(req, res);
		}else{
			req.setAttribute("message", "用户名或密码错误!");
			req.setAttribute("path", "login.jsp");
			
	        String targetURL = "/showMsg.jsp";
			dispatch(targetURL, req, res);
		}
	}
	
	//教师登录方法
	private boolean jsLogin(HttpServletRequest req){
		boolean result = false;
		
		String loginName = req.getParameter("name");
		String loginPass = req.getParameter("pass");
		
		String sql = "select id,name,loginName,loginPass from t_jsxx where loginName = ? and loginPass = ?";
		
		Object[] params={loginName,loginPass};
		DbUtil mydb=new DbUtil();
		try
		{
			mydb.doQuery(sql, params);
			ResultSet rs=mydb.getRs();
			if(rs.next())
			{
				result = true;
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setLoginName(rs.getString("loginName"));
				user.setLoginPass(rs.getString("loginPass"));
				user.setType(1);
				
				req.getSession().setAttribute("user", user);
			}
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		mydb.closed();
		
		return result;
	}
	
	//管理员登录方法
	private boolean adminLogin(HttpServletRequest req){
		boolean result = false;
		
		String loginName = req.getParameter("name");
		String loginPass = req.getParameter("pass");

		String sql = "select userId,userName,userPw from t_admin where userName = ? and userPw = ?";
		
		Object[] params={loginName,loginPass};
		DbUtil mydb=new DbUtil();
		try
		{
			mydb.doQuery(sql, params);
			ResultSet rs=mydb.getRs();
			if(rs.next())
			{
				result = true;
				User user = new User();
				user.setId(rs.getInt("userId"));
				user.setName("管理员");
				user.setLoginName(rs.getString("userName"));
				user.setLoginPass(rs.getString("userPw"));
				user.setType(0);
				
				req.getSession().setAttribute("user", user);
			}
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		mydb.closed();		
		
		return result;
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
