package by.eugenekulik.dto;

public record AgreementResponseDto(
    Long id,
    Long userId,
    Long addressId
){
    @Override
    public String toString() {
        return "AgreementResponseDto{" +
            "id=" + id +
            ", userId=" + userId +
            ", addressId=" + addressId +
            '}';
    }
}
