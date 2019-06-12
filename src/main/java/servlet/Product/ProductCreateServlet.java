package servlet.Product;

import dao.AbstractDao;
import model.Category;
import model.Product;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductCreateServlet", value = "/product/create")
public class ProductCreateServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ProductCreateServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AbstractDao<Category> categoryAbstractDao = new AbstractDao<>(Category.class);
        AbstractDao<Product> productAbstractDao = new AbstractDao<>(Product.class);
        String name = request.getParameter("name");
        double price = Double.valueOf(request.getParameter("price"));
        Category category = categoryAbstractDao.getByCriteria(request.getParameter("category"), "name").get(0);
        Integer categoryId = category.getId();
        Product product = new Product(name, categoryId, price);
        productAbstractDao.save(product);
        logger.debug("Redirect to /product/view");
        response.sendRedirect("/product/view");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AbstractDao<Category> abstractDao = new AbstractDao<>(Category.class);
        List<Category> categoryList = abstractDao.getAll();
        request.setAttribute("categories", categoryList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("ProductForm.jsp");
        logger.debug("Forward to ProductForm.jsp");
        dispatcher.forward(request, response);
    }
}
