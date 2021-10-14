package fr.insy2s.web.rest.vm;

import fr.insy2s.domain.Booking;

import java.util.List;

public class ManageBookingEmailVM {

    private Long userHostId;

    private List<Long> usersGuestIds;

    private Long reservationId;

    public ManageBookingEmailVM() {
    }

    public ManageBookingEmailVM(Long userHostId, List<Long> usersGuestIds, Long reservationId) {
        this.userHostId=userHostId;
        this.usersGuestIds=usersGuestIds;
        this.reservationId=reservationId;
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

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
}
