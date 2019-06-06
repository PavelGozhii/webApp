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

@WebServlet(name = "ProductEditServlet", value = "/product/edit")
public class ProductEditServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ProductEditServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AbstractDao<Category> categoryAbstractDao = new AbstractDao<>(Category.class);
        AbstractDao<Product> productAbstractDao = new AbstractDao<>(Product.class);
        Integer id = Integer.valueOf(request.getParameter("id"));
        String name = request.getParameter("name");
        double price = Double.valueOf(request.getParameter("price"));
        Category category = categoryAbstractDao.getByCriteria(request.getParameter("category"), "name").get(0);
        Integer categoryId = category.getId();
        Product product = new Product(id, name, categoryId, price);
        productAbstractDao.update(product);
        logger.debug("Redirect to /product/view");
        response.sendRedirect("/product/view");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AbstractDao<Product> productAbstractDao = new AbstractDao<>(Product.class);
        AbstractDao<Category> abstractDao = new AbstractDao<>(Category.class);
        List<Category> categoryList = abstractDao.getAll();
        request.setAttribute("categories", categoryList);
        request.setAttribute("product", productAbstractDao.getById(request.getParameter("id")));
        RequestDispatcher dispatcher = request.getRequestDispatcher("/product/ProductForm.jsp");
        logger.debug("Forward to /product/ProductForm.jsp");
        dispatcher.forward(request, response);
    }
}
