package by.eugenekulik.in.console.filter;

import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.ResponseData;
import lombok.Setter;

/**
 * An abstract class representing a filter in a chain of responsibility for processing requests.
 * Subclasses must implement the {@code service} method to define the specific behavior of the filter.
 * The filters are linked together through the 'next' property to form a chain.
 * The {@code doFilter} method is used to initiate the processing chain.
 *
 * @author Eugene Kulik
 */
public abstract class Filter {

    /**
     * Setter for the next filter in the chain.
     */
    @Setter
    private Filter next;

    /**
     * Abstract method to be implemented by subclasses to define the specific behavior of the filter.
     *
     * @param requestData  The request data to be processed by the filter.
     * @param responseData The response data to be modified by the filter.
     * @param next         The next filter in the chain.
     */
    abstract void service(RequestData requestData, ResponseData responseData, Filter next);

    /**
     * Initiates the processing chain by invoking the 'service' method of the current filter.
     *
     * @param requestData  The request data to be processed by the filter.
     * @param responseData The response data to be modified by the filter.
     */
    public void doFilter(RequestData requestData, ResponseData responseData) {
        service(requestData, responseData, next);
    }
}
