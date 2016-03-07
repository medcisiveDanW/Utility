package com.medcisive.utility.sql2;

import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author vhapalchambj
 */
public abstract class DBCServlet extends HttpServlet {
    private final com.medcisive.utility.sql2.DBCUtil _dbcUtil = new DBCUtil();
    public com.medcisive.utility.sql2.DBC _dest;
    public com.medcisive.utility.sql2.DBC _src;
    private static final Gson _gson = new Gson();
    
    public DBCServlet() {
        _dest = _dbcUtil._dest;
        _src = _dbcUtil._src;
    }
    
    private synchronized void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.getWriter().write(_gson.toJson(process(request)));
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    public abstract Object process(HttpServletRequest request);
}
