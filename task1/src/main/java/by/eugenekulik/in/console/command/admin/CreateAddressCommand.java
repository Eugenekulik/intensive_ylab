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

public class CreateAddressCommand implements Command {


    private final Set<String> allowedParams = new HashSet<>();


    private final AddressService addressService;

    public CreateAddressCommand(AddressService addressService) {
        allowedParams.addAll(List.of("region", "district", "city", "street", "house", "apartment"));
        this.addressService = addressService;
    }

    @Override
    public boolean isAllowed(User user) {
        return user.getRole().equals(Role.ADMIN);
    }

    @Override
    public void execute() {
        RequestData requestData = Session.getRequestData();
        Address address = Address.builder()
            .region(requestData.getParams().get("region"))
            .district(requestData.getParams().get("district"))
            .city(requestData.getParams().get("city"))
            .street(requestData.getParams().get("street"))
            .houseNumber(requestData.getParams().get("house"))
            .apartmentNumber(requestData.getParams().get("apartment"))
            .build();
        address = addressService.create(address);
        Session.getResponceData().add("Creation successful!");
        Session.getResponceData().add(String.format("Address = {id: %s," +
            "region: %s, district: %s, city: %s, street: %s, house: %s, apartment: %s}",
            address.getId(), address.getRegion(), address.getDistrict(), address.getCity(),
            address.getStreet(), address.getHouseNumber(), address.getApartmentNumber()));
    }

    @Override
    public boolean isAllowedParam(String name, String value) {
        return allowedParams.contains(name);
    }
}
