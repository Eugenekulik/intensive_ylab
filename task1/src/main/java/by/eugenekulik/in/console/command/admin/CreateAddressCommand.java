package by.eugenekulik.in.console.command.admin;

import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.Session;
import by.eugenekulik.in.console.command.Command;
import by.eugenekulik.model.Address;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.service.AddressService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The {@code CreateAddressCommand} class represents a command to create a new address.
 * It implements the {@link Command} interface.
 *
 * @author Eugene Kulik
 */
public class CreateAddressCommand implements Command {


    private final Set<String> allowedParams = new HashSet<>();


    private final AddressService addressService;

    /**
     * Constructs a {@code CreateAddressCommand} with the provided service for managing addresses.
     *
     * @param addressService The service responsible for managing addresses.
     */
    public CreateAddressCommand(AddressService addressService) {
        allowedParams.addAll(List.of("region", "district", "city", "street", "house", "apartment"));
        this.addressService = addressService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllowed(User user) {
        return user.getRole().equals(Role.ADMIN);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The {@code execute} method retrieves request parameters, validates them, creates an address,
     * and add information about the created address to the response data.
     * </p>
     */
    @Override
    public void execute() {
        RequestData requestData = Session.getRequestData();
        Address address = Address.builder()
            .region(requestData.getParam("region"))
            .district(requestData.getParam("district"))
            .city(requestData.getParam("city"))
            .street(requestData.getParam("street"))
            .house(requestData.getParam("house"))
            .apartment(requestData.getParam("apartment"))
            .build();
        address = addressService.create(address);
        Session.getResponceData().add("Creation successful!");
        Session.getResponceData().add(String.format("Address = {id: %s," +
                "region: %s, district: %s, city: %s, street: %s, house: %s, apartment: %s}",
            address.getId(), address.getRegion(), address.getDistrict(), address.getCity(),
            address.getStreet(), address.getHouse(), address.getApartment()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllowedParam(String name, String value) {
        return allowedParams.contains(name);
    }
}
