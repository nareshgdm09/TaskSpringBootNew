package com.task.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.task.entity.Player;

public class PlayerRepositoryCustomImpl implements PlayerRepositoryCustom {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Player> findAll(Player playerobj) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Player> query = builder.createQuery(Player.class);
		Root<Player> player = query.from(Player.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		// Adding predicates in case of parameter not being null
		if (playerobj.getId() != 0) {
			predicates.add(builder.equal(player.get("id"), playerobj.getId()));
		}
		if (playerobj.getName() != null) {
			predicates.add(builder.equal(player.get("name"), playerobj.getName()));
		}

		query.select(player).where(predicates.toArray(new Predicate[] {}));

		List<Player> allPlayers = entityManager.createQuery(query).getResultList();
		System.out.println(allPlayers);
		return allPlayers;
	}

}
