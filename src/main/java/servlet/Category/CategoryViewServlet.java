package servlet.Category;

import dao.AbstractDao;
import model.Category;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CategoryViewServlet", value = "/category/view")
public class CategoryViewServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(CategoryViewServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AbstractDao<Category> abstractDao = new AbstractDao<>(Category.class);
        List<Category> categories = abstractDao.getAll();
        request.setAttribute("categories", categories);
        RequestDispatcher dispatcher = request.getRequestDispatcher("CategoryPage.jsp");
        logger.debug("CategoryPage.jsp");
        dispatcher.forward(request, response);
    }
}
