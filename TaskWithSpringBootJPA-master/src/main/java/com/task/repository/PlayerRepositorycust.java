package com.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.entity.Player;

interface PlayerRepositorycust extends JpaRepository<Player, Long>, PlayerRepositoryCustom {
}