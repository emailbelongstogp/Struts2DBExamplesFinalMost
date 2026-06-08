package com.example.struts2.actions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import com.opensymphony.xwork2.ActionSupport;

@Action(value = "login", results = { @Result(name = "success", location = "/hello.jsp"),
		@Result(name = "error", location = "/fault.jsp") })
@Namespace("/")
public class LoginAction extends ActionSupport {
	public String execute() throws Exception {
		String returnString = ERROR;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			System.out.println("Connecting to the mysql database");
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/samplestruts", "root", "admin");
			System.out.println("Connection is Established");
			preparedStatement = conn.prepareStatement("SELECT * from bankuser where name=? and password=?");
			System.out.println("preparedStatement is " + preparedStatement);
			System.out.println("Name: " + getName());
			System.out.println("Password: " + getPwd());
			preparedStatement.setString(1, getName());
			preparedStatement.setString(2, getPwd());
			resultSet = preparedStatement.executeQuery();
			System.out.println("resultSet is " + resultSet);
			if (resultSet.next()) {
				System.out.println("user found and address is " + resultSet.getString("address"));
				setAddress(resultSet.getString("address"));
				returnString = SUCCESS;
			} else {
				returnString = ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnString = ERROR;

		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return returnString;
	}

	private String name;
	private String pwd;
	private String address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
