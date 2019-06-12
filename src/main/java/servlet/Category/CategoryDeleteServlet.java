package servlet.Category;

import dao.AbstractDao;
import model.Category;
import model.Product;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CategoryDeleteServlet", value = "/category/delete")
public class CategoryDeleteServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(CategoryDeleteServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AbstractDao<Category> categoryAbstractDao = new AbstractDao<>(Category.class);
        AbstractDao<Product> productAbstractDao = new AbstractDao<>(Product.class);
        String id = request.getParameter("id");
        productAbstractDao.deleteByCriteria(id, "category");
        categoryAbstractDao.delete(id);
        logger.debug("Redirect to /category/view");
        response.sendRedirect("/category/view");
    }
}
