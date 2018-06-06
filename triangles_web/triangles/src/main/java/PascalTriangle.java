/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import triangles.PascalTriangleParallelCols;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import triangles.*;

/**
 *
 * @author stijn
 */
@WebServlet(urlPatterns = {"/PascalTriangle"})
public class PascalTriangle extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet PascalTriangle</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PascalTriangle at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        int size = Integer.parseInt(request.getParameter("size"));
        String version = request.getParameter("type");
        
        int[][] triangle = new int[size][];
        
        switch(version){
            case "sequential": triangle = new triangles.PascalTriangle(size).getPoints(); break;
            case "parallel": triangle = new PascalTriangleParallelCols(size).getPoints(); break;
            case "parallelStreams": triangle = new PascalTriangleParallelStreamsRowsCols(size).getPoints(); break;          
        }
        String json = new JSONArray(triangle).toString();
        response.getWriter().write(json);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
/*
<servlet>
    <servlet-name>pascaltriangle</servlet-name>
    <servlet-class>PascalTriangle</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>pascaltriangle</servlet-name>
    <url-pattern>/pascaltriangle</url-pattern>
</servlet-mapping>

*/