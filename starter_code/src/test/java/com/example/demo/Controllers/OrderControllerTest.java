package com.example.demo.Controllers;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.example.demo.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {
    @InjectMocks
    private OrderController orderController;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Before
    public void setup(){
        when(userRepository.findByUsername("trung")).thenReturn(CreateUser());
        when(orderRepository.findByUser(any())).thenReturn(CreateOrders());
    }
    @Test
    public void SubmitOrder(){
        ResponseEntity<UserOrder> orderResponseEntity = orderController.submit("trung");
        assertNotNull(orderResponseEntity);
        assertEquals(200, orderResponseEntity.getStatusCodeValue());
        UserOrder order = orderResponseEntity.getBody();
        assertNotNull(order);
        assertEquals(CreateItems(),order.getItems());
        assertEquals(CreateUser().getId(), order.getUser().getId());
        assertEquals(CreateUser().getUsername(), order.getUser().getUsername());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void HistoryOrder(){
        ResponseEntity<List<UserOrder>> orderResponseEntity = orderController.getOrdersForUser("trung");
        assertNotNull(orderResponseEntity);
        assertEquals(200,orderResponseEntity.getStatusCodeValue());
        List<UserOrder> orderList = orderResponseEntity.getBody();
        assertNotNull(orderList);
        assertEquals(CreateOrders().size(), orderList.size());
    }
}
