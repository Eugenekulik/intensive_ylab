package by.eugenekulik.config;

import by.eugenekulik.in.console.ConsoleMessageConverter;
import by.eugenekulik.in.console.Controller;
import by.eugenekulik.in.console.View;
import by.eugenekulik.in.console.command.*;
import by.eugenekulik.in.console.command.admin.*;
import by.eugenekulik.in.console.command.client.CreateMetersDataCommand;
import by.eugenekulik.in.console.filter.ExceptionHandlerFilter;
import by.eugenekulik.in.console.filter.Filter;
import by.eugenekulik.in.console.filter.SecurityFilter;
import by.eugenekulik.in.console.filter.ValidationFilter;
import by.eugenekulik.model.MetersType;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.*;
import by.eugenekulik.out.dao.inmemory.*;
import by.eugenekulik.service.*;
import by.eugenekulik.utils.IncrementSequence;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Configuration {

    private final HashMap<String, Object> context = new HashMap<>();


    public Controller controller(){
        return new Controller(view(), converter(),filterChain());
    }

    public ConsoleMessageConverter converter(){
        return new ConsoleMessageConverter();
    }


    public Filter filterChain(){
        Filter securityFilter = new SecurityFilter();
        Filter validationFilter = new ValidationFilter(commands());
        Filter exHandlerFilter = new ExceptionHandlerFilter();
        exHandlerFilter.setNext(validationFilter);
        validationFilter.setNext(securityFilter);
        return exHandlerFilter;
    }


    public View view(){
        return new View();
    }


    public UserService userService(){
        if(!context.containsKey("userService"))
            context.put("userService", new UserService(userRepository()));
        return (UserService) context.get("userService");
    }

    public UserRepository userRepository() {
        if(context.containsKey("userRepository"))
            return (UserRepository) context.get("userRepository");
        UserRepository userRepository = new InMemoryUserRepository(new IncrementSequence(1L,1L));
        userRepository.save(User.builder().username("admin")
            .password("admin")
            .email("admin@mail.ru")
            .role(Role.ADMIN)
            .build());
        context.put("userRepository", userRepository);
        return userRepository;
    }

    public AgreementRepository agreementRepository(){
        if(context.containsKey("agreementRepository"))
            return (AgreementRepository) context.get("agreementRepository");
        AgreementRepository agreementRepository =
            new InMemoryAgreementRepository(new IncrementSequence(1L, 1L));
        context.put("agreementRepository", agreementRepository);
        return agreementRepository;
    }

    public AddressRepository addressRepository(){
        if(context.containsKey("addressRepository"))
            return (AddressRepository) context.get("addressRepository");
        AddressRepository addressRepository =
            new InMemoryAddressRepository(new IncrementSequence(1L, 1L));
        context.put("addressRepository", addressRepository);
        return addressRepository;
    }
    public MetersDataRepository metersDataRepository(){
        if(context.containsKey("metersDataRepository"))
            return (MetersDataRepository) context.get("metersDataRepository");
        MetersDataRepository metersDataRepository =
            new InMemoryMetersDataRepository(new IncrementSequence(1L, 1L));
        context.put("metersDataRepository", metersDataRepository);
        return metersDataRepository;
    }


    public MetersTypeRepository metersTypeRepository(){
        if(context.containsKey("metersTypeRepository"))
            return (MetersTypeRepository) context.get("metersTypeRepository");
        MetersTypeRepository metersTypeRepository =
            new InMemoryMetersTypeRepository(new IncrementSequence(1L, 1L));
        metersTypeRepository.save(new MetersType(1L, "warm_water"));
        metersTypeRepository.save(new MetersType(2L, "cold_water"));
        metersTypeRepository.save(new MetersType(3L, "heating"));
        context.put("metersTypeRepository", metersTypeRepository);
        return metersTypeRepository;
    }

    public AgreementService agreementService(){
        if(context.containsKey("agreementService"))
            return (AgreementService) context.get("agreementService");
        AgreementService agreementService =
            new AgreementService(agreementRepository(),addressRepository(), userRepository());
        context.put("agreementService", agreementService);
        return agreementService;
    }

    public Map<String, Command> commands(){
        HashMap<String, Command> commands = new HashMap<>();
        commands.put("registration", new RegistrationCommand(userService()));
        commands.put("authentication", new AuthenticationCommand(userService()));
        commands.put("create-agreement", new CreateAgreementCommand(agreementService()));
        commands.put("create-address", new CreateAddressCommand(addressService()));
        commands.put("create-metersdata",
            new CreateMetersDataCommand(metersDataService(), metersTypeService()));
        commands.put("create-meterstype", new CreateMetersTypeCommand(metersTypeService()));
        commands.put("show-users", new ShowUserCommand(userService()));
        commands.put("show-agreements", new ShowAgreementCommand(agreementService()));
        commands.put("show-metersdata",  new ShowMetersDataCommand(metersDataService(), metersTypeService()));
        ResourceBundle resourceBundle = ResourceBundle.getBundle("text");
        commands.put("help", new HelpCommand(commands, resourceBundle));
        commands.put("logout", new LogoutCommand());
        commands.put("exit", new ExitCommand());
        return commands;
    }

    private MetersTypeService metersTypeService() {
        if(context.containsKey("metersTypeService"))
            return (MetersTypeService) context.get("metersTypeService");
        MetersTypeService metersTypeService = new MetersTypeService(metersTypeRepository());
        context.put("metersTypeService", metersTypeService);
        return metersTypeService;
    }

    private MetersDataService metersDataService() {
        if(context.containsKey("metersDataService"))
            return (MetersDataService) context.get("metersDataService");
        MetersDataService metersDataService = new MetersDataService(metersDataRepository());
        context.put("metersDataService", metersDataService);
        return metersDataService;
    }

    private AddressService addressService() {
        if(context.containsKey("addressService"))
            return (AddressService) context.get("addressService");
        AddressService addressService =
            new AddressService(addressRepository());
        context.put("addressService", addressService);
        return addressService;
    }


}
