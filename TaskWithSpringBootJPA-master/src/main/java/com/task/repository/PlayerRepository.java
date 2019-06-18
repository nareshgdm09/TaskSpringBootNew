package com.task.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.task.entity.Player;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {
}