package com.deskflow.api.desk;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeskRepository extends JpaRepository<Desk, Long> {

    List<Desk> findAllByFloor(Integer floor);

    List<Desk> findAllByHasMonitor(boolean hasMonitor);

    List<Desk> findAllByFloorAndHasMonitor(Integer floor, boolean hasMonitor);
}