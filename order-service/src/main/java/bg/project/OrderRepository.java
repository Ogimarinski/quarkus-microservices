package bg.project;

import jakarta.enterprise.context.ApplicationScoped;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class OrderRepository {

    private final DataSource dataSource;

    public OrderRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Order mapRow(ResultSet resultSet) throws SQLException {
        Order order = new Order(
                resultSet.getLong("id"),
                resultSet.getLong("product_id"),
                resultSet.getInt("quantity"),
                resultSet.getDouble("total_price")
        );

        return order;
    }

    public Order create(Long productId, int quantity, double totalPrice){
        String sql = "INSERT Into orders(product_id, quantity, total_price) values (?, ?, ?)";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"})){

            preparedStatement.setLong(1, productId);
            preparedStatement.setInt(2, quantity);
            preparedStatement.setDouble(3, totalPrice);

            preparedStatement.executeUpdate();

            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if(resultSet.next()){
                    long id = resultSet.getLong(1);
                    return new Order(id, productId, quantity, totalPrice);
                }
            }

            throw new SQLException("Failed to retrieve generated product ID");

        }catch (SQLException e){
            throw new RuntimeException("Failed to create a order", e);
        }
    }

    public List<Order> findAll(){
        String sql = "Select id, product_id, quantity, total_price from orders";

        List<Order> orders = new ArrayList<>();

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()){
                orders.add(mapRow(resultSet));
            }
        }catch (SQLException e){
            throw new RuntimeException("Failed to fetch orders", e);
        }

        return orders;
    }

    public Optional<Order> findById(Long id){
        String sql = "Select id, product_id, quantity, total_price from orders where id = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1,  id);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()){
                    return Optional.of(mapRow(resultSet));
                }

                return Optional.empty();
            }
        }catch (SQLException e){
            throw new RuntimeException("Failed to fetch a order with ID" + id, e);
        }
    }
}
