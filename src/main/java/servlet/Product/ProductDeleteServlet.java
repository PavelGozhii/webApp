package servlet.Product;

import dao.AbstractDao;
import model.Product;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ProductDeleteServlet", value = "/product/delete")
public class ProductDeleteServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ProductDeleteServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AbstractDao<Product> productAbstractDao = new AbstractDao<>(Product.class);
        String id = request.getParameter("id");
        productAbstractDao.delete(id);
        logger.debug("Redirect to /product/view");
        response.sendRedirect("/product/view");
    }
}
