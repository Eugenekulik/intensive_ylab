package by.eugenekulik.in.console.command.admin;

import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.Session;
import by.eugenekulik.in.console.TextColor;
import by.eugenekulik.in.console.command.Command;
import by.eugenekulik.model.Address;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.service.AddressService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The {@code ShowAddressCommand} class represents a command to display a list of addresses.
 * It implements the {@link Command} interface.
 *
 * @author Eugene Kulik
 */
public class ShowAddressCommand implements Command {

    private final AddressService addressService;
    private final Set<String> allowedParams = new HashSet<>();

    /**
     * Constructs a {@code ShowAddressCommand} with the provided service for managing addresses.
     *
     * @param addressService The service responsible for managing addresses.
     */
    public ShowAddressCommand(AddressService addressService) {
        this.addressService = addressService;
        allowedParams.addAll(List.of("page", "count"));
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
     * The {@code execute} method retrieves request parameters, validates them, fetches a page
     * of addresses, and adds information about each address to the response data.
     * </p>
     */
    @Override
    public void execute() {
        RequestData requestData = Session.getRequestData();
        int count = Optional.ofNullable(requestData.getParam("count"))
            .map(e -> {
                try {
                    return Integer.parseInt(e);
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Wrong param value: count should be a integer number", ex);
                }
            })
            .orElse(5);
        int page = Optional.ofNullable(requestData.getParam("page"))
            .map(e -> {
                try {
                    return Integer.parseInt(e);
                } catch (NumberFormatException ex){
                    throw new IllegalArgumentException("Wrong param value: page should be a integer number", ex);
                }
            })
            .orElse(0);
        List<Address> addresses = addressService.getPage(page, count);
        Session.getResponceData().add(TextColor.ANSI_GREEN.changeColor("Result:"));
        addresses.forEach(address -> Session.getResponceData().add(String.format(
            "id: %s, region: %s, district: %s, city: %s, street: %s, house: %s, apartment: %s",
            address.getId(), address.getRegion(), address.getDistrict(), address.getCity(),
            address.getStreet(), address.getHouseNumber(), address.getApartmentNumber()
        )));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllowedParam(String name, String value) {
        return allowedParams.contains(name);
    }
}
