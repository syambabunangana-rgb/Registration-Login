package registrationlogin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Login extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name=req.getParameter("name");
		String pwd=req.getParameter("pass");
		PrintWriter pw=resp.getWriter();
		try {
		    Class.forName("com.mysql.cj.jdbc.Driver");
		    Connection con = DriverManager.getConnection(
		        "jdbc:mysql://localhost:3306/demoservlet?user=root&password=root");

		    // Trim inputs to avoid empty spaces
		    String trimmedName = name.trim();
		    String trimmedPwd = pwd.trim();

		    if (trimmedName.isEmpty() || trimmedPwd.isEmpty()) {
		        // If user enters only spaces or nothing
		        pw.println("<html><body bgcolor='palegoldenrod'><h1>Invalid Credentials (Empty spaces not allowed)</h1></body></html>");
		        RequestDispatcher rd = req.getRequestDispatcher("signin.html");
		        rd.include(req, resp);

		    } else {
		        PreparedStatement ps = con.prepareStatement(
		            "select * from registration where name=? AND password=?");
		        ps.setString(1, trimmedName);
		        ps.setString(2, trimmedPwd);
		        ResultSet rs = ps.executeQuery();

		        if (rs.next()) {
		            RequestDispatcher rd = req.getRequestDispatcher("home.html");
		            rd.forward(req, resp);
		        } else {
		            pw.println("<html><body bgcolor='palegoldenrod'><h1>Incorrect Username and Password</h1></body></html>");
		            RequestDispatcher rd = req.getRequestDispatcher("signin.html");
		            rd.include(req, resp);
		        }
		    }
		} catch (ClassNotFoundException | SQLException e) {
		    e.printStackTrace();
		}

	}

}
