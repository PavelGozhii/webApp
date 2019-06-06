package servlet.Product;

import dao.AbstractDao;
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

@WebServlet(name = "ProductViewServlet", value = "/product/view")
public class ProductViewServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ProductViewServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        AbstractDao<Product> productAbstractDao = new AbstractDao<>(Product.class);
        List<Product> products;
        if(id != null){
            products = productAbstractDao.getByCriteria(id, "category");
        }else{
            products = productAbstractDao.getAll();
        }
        request.setAttribute("products", products);
        RequestDispatcher dispatcher = request.getRequestDispatcher("ProductPage.jsp");
        logger.debug("Forward to ProductPage.jsp");
        dispatcher.forward(request, response);
    }
}
