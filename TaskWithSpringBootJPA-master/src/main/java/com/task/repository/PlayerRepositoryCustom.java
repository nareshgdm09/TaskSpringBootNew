package com.task.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.task.entity.Player;
@Repository
public interface PlayerRepositoryCustom {
	List<Player> findAll(Player player);
}