/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.sql.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author adam
 */
@WebServlet(name = "ServletAssn", urlPatterns = {"/ServletAssn"})
public class ServletAssn extends HttpServlet {
    
    private static final String CONTENT_TYPE = "text/html";
    private PreparedStatement pstmt1;
    private PreparedStatement pstmt2;
    private PreparedStatement pstmt3;
    private PrintWriter out;

    
                
    /** Initialize variables */
    public void init() throws ServletException {
      initializeJdbc();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    /** Process the HTTP Get request */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
                       
        out.println("<h3>Find title, author(s), and pages: </h3>");
        out.println("<form method=\"POST\" action=\"/ServletAssn/ServletAssn\">");
        out.println("<p>Enter isbn <font color = \"#FF0000\">*</font>\n" +
                    "<input type = \"text\" name = \"ISBN\">&nbsp;</p>");
        out.println("<p><input type=\"submit\" value=\"Submit\" >");


        out.close(); // Close stream
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
   // Process the HTTP Post request
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        init();
        
        // Obtain parameters from the client
        String ISBN = request.getParameter("ISBN");
        
        try {
            pstmt1.setString(1, ISBN);
            pstmt2.setString(1, ISBN);
            pstmt3.setString(1, ISBN);
            ResultSet rset1 = pstmt1.executeQuery();
            ResultSet rset2 = pstmt2.executeQuery();
            ResultSet rset3 = pstmt3.executeQuery();
            
            out.println("ISBN entered: <br/>" + ISBN + "<br/>");
            
            out.println("<br/>Title: <br/>");
            while (rset1.next()) {
                String title = rset1.getString("Title");
                out.println(title + "<br/>");
                break;
            }
            
            out.println("<br/>Author(s): <br/>");
            while (rset2.next()) {
                String authors = rset2.getString("AuthorName");
                out.println(authors + "<br/>");

            }
            
            out.println("<br/>Pages: <br/>");
            while (rset3.next()) {
                String pages = rset3.getString("Pages");
                out.println(pages + "<br/>");
                break;
            }
            
            }
        catch(Exception ex) {
            out.println("Error: " + ex.getMessage());
        }
        finally {
            out.close(); // Close stream
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void initializeJdbc() {

        try {

            String connectionString = "jdbc:derby://localhost:1527/javabook";

            // Establish a connection
            Connection conn = DriverManager.getConnection
              (connectionString, "scott", "tiger");
            System.out.println("Database connected");

            // Create a Statement
            pstmt1 = conn.prepareStatement("select books.Title " +
                "from Books, Authors, BookAuthors " + "where Books.ISBN = ? " + 
                "and BookAuthors.ISBN = Books.ISBN " +
                "and BookAuthors.AuthorID = Authors.AuthorID");
            
            pstmt2 = conn.prepareStatement("select authors.AuthorName " +
                "from Books, Authors, BookAuthors " + "where Books.ISBN = ? " + 
                "and BookAuthors.ISBN = Books.ISBN " +
                "and BookAuthors.AuthorID = Authors.AuthorID");
            
            pstmt3 = conn.prepareStatement("select books.Pages " +
                "from Books, Authors, BookAuthors " + "where Books.ISBN = ? " + 
                "and BookAuthors.ISBN = Books.ISBN " +
                "and BookAuthors.AuthorID = Authors.AuthorID");
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
  }
}
