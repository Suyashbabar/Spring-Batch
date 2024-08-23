package in.suyash.Config;

import in.suyash.entity.Customer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CustomerProcessoor implements ItemProcessor<Customer , Customer> {
    @Override
    public Customer process(Customer customer) throws Exception {




        return customer;
    }
}
