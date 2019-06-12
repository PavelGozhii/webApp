import dao.AbstractDao;
import model.Category;
import model.Product;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class AbstractDaoTest extends Assert {

    @Rule
    public final Timeout timeout = new Timeout(2000);
    private static final AbstractDao<Product> productAbstractDao = new AbstractDao<>(Product.class);
    private static final AbstractDao<Category> categoryAbstractDao = new AbstractDao<>(Category.class);

    @Before
    public void beforeAbstractDaoTest() {
        Category category = new Category("Category", "Category");
        categoryAbstractDao.save(category);
        category = categoryAbstractDao.getByCriteria("Category", "name").get(0);
        Product product = new Product("Product", category.getId(), 100);
        productAbstractDao.save(product);
    }

    @After
    public void afterAbstractDaoTest() {
        categoryAbstractDao.deleteByCriteria("Category", "name");
        productAbstractDao.deleteByCriteria("Product", "name");
    }

    @Test
    public void saveAbstractDaoTest() {
        assertNotEquals(categoryAbstractDao.getByCriteria("Category", "name").size(), 0);
        assertNotEquals(productAbstractDao.getByCriteria("Product", "name").size(), 0);
    }

    @Test
    public void updateAbstractDaoTest() {
        Category category = categoryAbstractDao.getByCriteria("Category", "name").get(0);
        category.setName("123");
        Product product = productAbstractDao.getByCriteria("Product", "name").get(0);
        product.setName("321");
        productAbstractDao.update(product);
        categoryAbstractDao.update(category);
        assertNotEquals(category.getName(), "Category");
        assertEquals(category.getName(), "123");
        assertNotEquals(product.getName(), "Product");
        assertEquals(product.getName(), "321");
    }

    @Test
    public void getAbstractDaoTest() {
        Category category = categoryAbstractDao.getByCriteria("Category", "name").get(0);
        Product product = productAbstractDao.getByCriteria("Product", "name").get(0);
        assertEquals(category.getName(), "Category");
        assertEquals(product.getName(), "Product");
    }
}
