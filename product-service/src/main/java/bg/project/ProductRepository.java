package bg.project;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductRepository {

    private final DataSource dataSource;

    public ProductRepository(DataSource dataSource){
        this.dataSource = dataSource;
    }

    private Product mapRow(ResultSet resultSet) throws SQLException{
        Product product = new Product(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDouble("price")
        );

        return product;
    }

    public List<Product> findAll(){
        String sql = "Select id, name, price from products";

        List<Product> products = new ArrayList<>();

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()){

            while (resultSet.next()){
                products.add(mapRow(resultSet));
            }
        }catch (SQLException e){
            throw new RuntimeException("Failed to fetch products", e);
        }

        return products;
    }

    public Optional<Product> findById(Long id){
        String sql = "Select id, name, price from products where id = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){

                preparedStatement.setLong(1, id);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    return Optional.of(mapRow(resultSet));
                }

                return Optional.empty();
            }

        }catch (SQLException e){
            throw new RuntimeException("Failed to fetch a product with id" + id, e);
        }
    }

    public Product create(Product product){
        String sql = "INSERT INTO products (name, price) values (?, ?)";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"})){
                preparedStatement.setString(1, product.name());
                preparedStatement.setDouble(2, product.price());

                preparedStatement.executeUpdate();

                try(ResultSet resultSet = preparedStatement.getGeneratedKeys()){
                    if (resultSet.next()){
                        long id = resultSet.getLong(1);
                        return new Product(id, product.name(), product.price());
                    }
                }

                throw new SQLException("Failed to retrieve generated product ID");

        }catch (SQLException e){
            throw new RuntimeException("Failed to create a product", e);
        }
    }

    public Optional<Product> update(Long id, Product product){
        String sql = "Update products SET name = ?, price = ? WHERE id = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, product.name());
            preparedStatement.setDouble(2, product.price());
            preparedStatement.setLong(3, id);

            int affectedRows = preparedStatement.executeUpdate();

            if(affectedRows == 0){
                return Optional.empty();
            }
            return Optional.of(new Product(id, product.name(), product.price()));

        }catch (SQLException e){
            throw new RuntimeException("Failed to update a product with ID" + id, e);
        }
    }

    public boolean delete(Long id){
        String sql = "DELETE FROM products Where id = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setLong(1, id);

            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows > 0;

        }catch (SQLException e){
            throw new RuntimeException("Failed to delete product with ID" + id, e);
        }
    }
}
