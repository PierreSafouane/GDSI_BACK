package fr.insy2s.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.insy2s.service.dto.BookingDTO;

import java.util.List;

public class ManageBookingVM {


    private BookingDTO bookingDTO;

    private Long userHostId;

    private List<Long> usersGuestIds;

    public ManageBookingVM() {
    }

    public ManageBookingVM(BookingDTO bookingDTO, Long userHostId, List<Long> usersGuestIds) {
        this.bookingDTO = bookingDTO;
        this.userHostId = userHostId;
        this.usersGuestIds = usersGuestIds;
    }

    public BookingDTO getBookingDTO() {
        return bookingDTO;
    }

    public void setBookingDTO(BookingDTO bookingDTO) {
        this.bookingDTO = bookingDTO;
    }

    public Long getUserHostId() {
        return userHostId;
    }

    public void setUserHostId(Long userHostId) {
        this.userHostId = userHostId;
    }

    public List<Long> getUsersGuestIds() {
        return usersGuestIds;
    }

    public void setUsersGuestIds(List<Long> usersGuestIds) {
        this.usersGuestIds = usersGuestIds;
    }
}
