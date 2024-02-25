package by.eugenekulik.dto;

public record AgreementResponseDto(
    Long id,
    Long userId,
    Long addressId,
    Boolean active
){
    @Override
    public String toString() {
        return "AgreementResponseDto{" +
            "id=" + id +
            ", userId=" + userId +
            ", addressId=" + addressId +
            ", active=" + active +
            '}';
    }
}
