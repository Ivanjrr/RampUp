//package com.RampUp.EJAUNIV.config;
//
//import java.time.Instant;
//import java.util.Arrays;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//import com.RampUp.EJAUNIV.entities.Characteristic;
//import com.RampUp.EJAUNIV.entities.CreditCardPayment;
//import com.RampUp.EJAUNIV.entities.Customer;
//import com.RampUp.EJAUNIV.entities.Order;
//import com.RampUp.EJAUNIV.entities.OrderItem;
//import com.RampUp.EJAUNIV.entities.Payment;
//import com.RampUp.EJAUNIV.entities.ProductOffering;
//import com.RampUp.EJAUNIV.entities.TimePeriod;
//import com.RampUp.EJAUNIV.entities.User;
//import com.RampUp.EJAUNIV.entities.enums.CharacteristicType;
//import com.RampUp.EJAUNIV.entities.enums.CostumerType;
//import com.RampUp.EJAUNIV.entities.enums.POState;
//import com.RampUp.EJAUNIV.entities.enums.PaymentState;
//import com.RampUp.EJAUNIV.repositories.AddressRepository;
//import com.RampUp.EJAUNIV.repositories.CharacteristicRepository;
//import com.RampUp.EJAUNIV.repositories.CustomerRepository;
//import com.RampUp.EJAUNIV.repositories.OrderItemRepository;
//import com.RampUp.EJAUNIV.repositories.OrderRepository;
//import com.RampUp.EJAUNIV.repositories.ProductOfferingRepository;
//import com.RampUp.EJAUNIV.repositories.RoleRepository;
//import com.RampUp.EJAUNIV.repositories.UserRepository;
//
//
//@Configuration
//@Profile("test")
//public class TestConfig implements CommandLineRunner {
//
//	@Autowired
//	private UserRepository userRepository;
//	
//	@Autowired
//	private AddressRepository AddressRepository;
//	
//	@Autowired
//	private CharacteristicRepository characteristicRepository;
//
//	@Autowired
//	private OrderRepository orderRepository;
//
//	@Autowired
//	private CustomerRepository customerRepository;
//	
//	@Autowired
//	private ProductOfferingRepository productOfferingRepository;
//
//	@Autowired
//	private OrderItemRepository orderItemRepository;
//	
//	@Autowired
//	private RoleRepository roleRepository;
//	
//	
//	@Override
//	public void run(String... args) throws Exception {
//
//		Characteristic char1 = new Characteristic(null,"Eletronics", "3", CharacteristicType.User,new TimePeriod() );
//		Characteristic char2 = new Characteristic(null,"Television", "4", CharacteristicType.User,new TimePeriod() );
//		Characteristic char3 = new Characteristic(null,"Computer", "2", CharacteristicType.User,new TimePeriod() );
//		
//		ProductOffering p1 = new ProductOffering(null, "Ipad", true , new TimePeriod(), POState.Technincal);
//		ProductOffering p2 = new ProductOffering(null, "LG QLED", true , new TimePeriod(), POState.Technincal);
//		ProductOffering p3 = new ProductOffering(null, "SAMSUNG QLED", true , new TimePeriod(), POState.Technincal);
//		ProductOffering p4 = new ProductOffering(null, "Dell G15", true , new TimePeriod(), POState.Active);
//		ProductOffering p5 = new ProductOffering(null, "Helios", true , new TimePeriod(), POState.Definition);
//
//		characteristicRepository.saveAll(Arrays.asList(char1, char2, char3));
//		productOfferingRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));
//
//		p1.getCharacteristic().add(char2);
//		p2.getCharacteristic().add(char1);
//		p2.getCharacteristic().add(char3);
//		p3.getCharacteristic().add(char3);
//		p4.getCharacteristic().add(char3);
//		p5.getCharacteristic().add(char2);
//
//		productOfferingRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));
//
//		User u1 = new User(null, "maria@gmail.com", "123456");
//		User u2 = new User(null, "alex@gmail.com", "123456");
//
//		Order o1 = new Order(null, Instant.parse("2019-06-20T19:53:07"),new Customer(null, "Carlos", "RG", "Active", "13000", "123456", CostumerType.NaturalPerson));
//		Order o2 = new Order(null, Instant.parse("2019-06-20T17:53:07"),new Customer(null, "Souza", "RG", "Active", "11560", "123456", CostumerType.LegalPerson));
//		Order o3 = new Order(null, Instant.parse("2019-06-20T20:53:07"),new Customer(null, "Carlos", "CPF", "Active", "13000", "123456", CostumerType.Technical));
//
//		userRepository.saveAll(Arrays.asList(u1, u2));
//		orderRepository.saveAll(Arrays.asList(o1, o2, o3));
//		
//		OrderItem oi1 = new OrderItem(p1, o1, 100.00, 4, 1250.00);
//		OrderItem oi2 = new OrderItem(p2, o2, 500.00, 3, 1111.00);
//		OrderItem oi3 = new OrderItem(p3, o3, 200.00, 6, 1908.00);
//		OrderItem oi4 = new OrderItem(p5, o1, 100.00, 2, 1680.00);
//
//		orderItemRepository.saveAll(Arrays.asList(oi1, oi2, oi3, oi4));	
//		
//		Payment pay1 = new CreditCardPayment(null, PaymentState.Paid, o1, 5);
//		o1.setPayment(pay1);
//
//		orderRepository.save(o1);
//	}
//}
