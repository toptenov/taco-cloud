package tacos.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.extern.slf4j.Slf4j;
import tacos.TacoOrder;
import tacos.User;
import tacos.data.OrderRepository;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {

    private OrderRepository orderRepo;
    private OrderProps props;
    
    public OrderController(OrderRepository orderRepo, OrderProps props) {
        this.orderRepo = orderRepo;
        this.props = props;
    }

    @GetMapping("/current")
    @PostAuthorize("hasRole('USER')")
    public String orderForm() {
        return "orderForm";
    }

    @PostMapping
    @PostAuthorize("hasRole('USER')")
    public String processOrder(@Valid TacoOrder order, Errors errors,
        SessionStatus sessionStatus, @AuthenticationPrincipal User user) {

        if (errors.hasErrors()) {
            return "orderForm";
        }

        order.setUser(user);
        orderRepo.save(order);
        log.info("Processing order: {}", order);
        sessionStatus.setComplete();

        return "redirect:/";

    }

    @GetMapping
    @PostAuthorize("hasRole('USER')")
    public String ordersForUser(@AuthenticationPrincipal User user, Model model) {

        Pageable pageable = PageRequest.of(0, props.getPageSize());
        model.addAttribute("user", user);

        List<TacoOrder> tacoOrders = orderRepo.findByUserOrderByPlacedAtDesc(user, pageable);
        model.addAttribute("orders", tacoOrders);

        return "orderList";

    }

}