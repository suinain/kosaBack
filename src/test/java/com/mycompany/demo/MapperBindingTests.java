package com.mycompany.demo;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.util.Map;

import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mycompany.demo.database.dao.*;
import com.mycompany.demo.database.dto.Pager;

@SpringBootTest
class MapperBindingTests {
    @Autowired
    private SqlSessionFactory sessionFactory;

    @Test
    void everyDaoMethodHasAStatementAndBindableParameters() throws Exception {
        var configuration = sessionFactory.getConfiguration();
        for (Class<?> dao : new Class<?>[] {ReviewDao.class, MemberDao.class,
                OrdersDao.class, OrderItemDao.class, ProductDao.class, SellerDao.class}) {
            for (Method method : dao.getDeclaredMethods()) {
                String id = dao.getName() + "." + method.getName();
                assertTrue(configuration.hasStatement(id), id);
                // Exercise both filtered and unfiltered dynamic queries.
                for (boolean filtered : new boolean[] {false, true}) {
                    Object[] args = new Object[method.getParameterCount()];
                    Class<?>[] types = method.getParameterTypes();
                    for (int i = 0; i < types.length; i++) {
                        if (types[i] == Pager.class) {
                            args[i] = new Pager(10, 5, 100, 2);
                        } else if (types[i] == Map.class) {
                            args[i] = filtered ? Map.ofEntries(
                                    Map.entry("memberId", "user1"), Map.entry("memberName", "Member"),
                                    Map.entry("title", "review"), Map.entry("content", "sample"),
                                    Map.entry("writer", "user1"), Map.entry("productId", 1L),
                                    Map.entry("orderId", 1L), Map.entry("sellerId", 1L),
                                    Map.entry("productName", "product"), Map.entry("sellerName", "seller"),
                                    Map.entry("email", "example.com"), Map.entry("phone", "010")) : null;
                        } else if (types[i] == String.class) {
                            args[i] = method.getName().equals("selectPage") && !filtered ? null : "1";
                        } else if (types[i] == long.class) {
                            args[i] = 1L;
                        } else {
                            args[i] = types[i].getDeclaredConstructor().newInstance();
                        }
                    }
                    Object parameters = new ParamNameResolver(configuration, method).getNamedParams(args);
                    var statement = configuration.getMappedStatement(id);
                    var sql = statement.getBoundSql(parameters);
                    assertTrue(!sql.getSql().isBlank(), id);
                    configuration.newParameterHandler(statement, parameters, sql)
                            .setParameters(mock(PreparedStatement.class));
                }
            }
        }
    }
}
