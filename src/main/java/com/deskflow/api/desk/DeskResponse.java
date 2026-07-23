package com.deskflow.api.desk;

public record DeskResponse(
        Long id,
        String code,
        Integer floor,
        boolean hasMonitor,
        boolean active) {

    public static DeskResponse from(Desk desk) {
        return new DeskResponse(
                desk.getId(),
                desk.getCode(),
                desk.getFloor(),
                desk.isHasMonitor(),
                desk.isActive());
    }
}