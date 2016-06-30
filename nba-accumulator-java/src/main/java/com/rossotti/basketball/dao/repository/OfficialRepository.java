package com.rossotti.basketball.dao.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rossotti.basketball.dao.model.Official;
import com.rossotti.basketball.dao.model.StatusCodeDAO;

@Repository
@Transactional
public class OfficialRepository {
	@Autowired
	private SessionFactory sessionFactory;

	public Official findOfficial(String lastName, String firstName, LocalDate asOfDate) {
		String sql = 	"from Official " +
						"where lastName = :lastName " +
						"and firstName = :firstName " +
						"and fromDate <= :asOfDate " +
						"and toDate >= :asOfDate";
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		query.setParameter("lastName", lastName);
		query.setParameter("firstName", firstName);
		query.setParameter("asOfDate", asOfDate);

		Official official = (Official)query.uniqueResult();
		if (official == null) {
			official = new Official(StatusCodeDAO.NotFound);
		}
		else {
			official.setStatusCode(StatusCodeDAO.Found);
		}
		return official;
	}

	@SuppressWarnings("unchecked")
	public List<Official> findOfficials(String lastName, String firstName) {
		String sql = 	"from Official " +
						"where lastName = :lastName " +
						"and firstName = :firstName";
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		query.setParameter("lastName", lastName);
		query.setParameter("firstName", firstName);

		List<Official> officials = query.list();
		if (officials == null) {
			officials = new ArrayList<Official>();
		}
		return officials;
	}

	@SuppressWarnings("unchecked")
	public List<Official> findOfficials(LocalDate asOfDate) {
		String sql = 	"from Official " +
						"where fromDate <= :asOfDate " +
						"and toDate >= :asOfDate";
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		query.setParameter("asOfDate", asOfDate);

		List<Official> officials = query.list();
		if (officials == null) {
			officials = new ArrayList<Official>();
		}
		return officials;
	}

	public Official createOfficial(Official createOfficial) {
		Official official = findOfficial(createOfficial.getLastName(), createOfficial.getFirstName(), createOfficial.getFromDate());
		if (official.isNotFound()) {
			getSessionFactory().getCurrentSession().persist(createOfficial);
			createOfficial.setStatusCode(StatusCodeDAO.Created);
		}
		else {
			return official;
		}
		return createOfficial;
	}

	public Official updateOfficial(Official updateOfficial) {
		Official official = findOfficial(updateOfficial.getLastName(), updateOfficial.getFirstName(), updateOfficial.getFromDate());
		if (official.isFound()) {
			official.setLastName(updateOfficial.getLastName());
			official.setFirstName(updateOfficial.getFirstName());
			official.setFromDate(updateOfficial.getFromDate());
			official.setToDate(updateOfficial.getToDate());
			official.setNumber(updateOfficial.getNumber());
			official.setStatusCode(StatusCodeDAO.Updated);
			getSessionFactory().getCurrentSession().saveOrUpdate(official);
		}
		return official;
	}

	public Official deleteOfficial(String lastName, String firstName, LocalDate asOfDate) {
		Official official = findOfficial(lastName, firstName, asOfDate);
		if (official.isFound()) {
			getSessionFactory().getCurrentSession().delete(official);
			official = new Official(StatusCodeDAO.Deleted);
		}
		return official;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
