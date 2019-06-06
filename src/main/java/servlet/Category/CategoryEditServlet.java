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

@WebServlet(name = "CategoryEditServlet", value = "/category/edit")
public class CategoryEditServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(CategoryEditServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AbstractDao<Category> categoryAbstractDao = new AbstractDao<>(Category.class);
        Integer id = Integer.valueOf(request.getParameter("id"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        Category category = new Category(id, name, description);
        categoryAbstractDao.update(category);
        logger.debug("Redirect to /category/view");
        response.sendRedirect("/category/view");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AbstractDao<Category> categoryAbstractDao = new AbstractDao<>(Category.class);
        String id = request.getParameter("id");
        Category category = categoryAbstractDao.getById(id);
        request.setAttribute("category", category);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/category/CategoryForm.jsp");
        logger.debug("Forward to /category/CategoryForm.jsp");
        dispatcher.forward(request, response);
    }
}
