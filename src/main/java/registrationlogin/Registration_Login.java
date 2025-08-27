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

public class Registration_Login extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name=req.getParameter("name");
		String email=req.getParameter("email");
		String pwd=req.getParameter("pwd");
		PrintWriter pw=resp.getWriter();
		
        resp.setContentType("text/html");

        try {
            // Check if input fields are empty
            if (name == null || name.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                pwd == null || pwd.trim().isEmpty()) {

                pw.println("<html><body bgcolor='powderblue'><h1>Invalid Credentials! All fields are required.</h1></body></html>");
                RequestDispatcher rd = req.getRequestDispatcher("signup.html");
                rd.include(req, resp);
                return; // stop further execution
            }

            // DB Connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demoservlet?user=root&password=root");

            // Check if email already exists
            PreparedStatement ps = con.prepareStatement("select * from registration where email=?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                pw.println("<html><body bgcolor='powderblue'><h1>Email already exists!</h1></body></html>");
                RequestDispatcher rd = req.getRequestDispatcher("signup.html");
                rd.include(req, resp);
            } else {
                // Insert new user
                ps = con.prepareStatement("insert into registration values(?,?,?)");
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, pwd);
                ps.executeUpdate();

                pw.println("<html><body bgcolor='powderblue'><h1>Account Created! Please Login</h1></body></html>");
                RequestDispatcher rd = req.getRequestDispatcher("signin.html");
                rd.include(req, resp);
            }

            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            pw.println("<html><body bgcolor='powderblue'><h1>Something went wrong! Try again later.</h1></body></html>");
        }


	}
}

