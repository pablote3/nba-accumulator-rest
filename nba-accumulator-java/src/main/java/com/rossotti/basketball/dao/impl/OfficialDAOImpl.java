package com.rossotti.basketball.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rossotti.basketball.dao.OfficialDAO;
import com.rossotti.basketball.dao.exceptions.DuplicateEntityException;
import com.rossotti.basketball.models.Official;
import com.rossotti.basketball.models.StatusCode;

@Repository
@Transactional
public class OfficialDAOImpl implements OfficialDAO {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Official findOfficial(String lastName, String firstName, LocalDate fromDate, LocalDate toDate) {
		Official official = (Official)getSessionFactory().getCurrentSession().createCriteria(Official.class)
			.add(Restrictions.eq("lastName", lastName))
			.add(Restrictions.eq("firstName", firstName))
			.add(Restrictions.le("fromDate", fromDate))
			.add(Restrictions.ge("toDate", toDate))
			.uniqueResult();
		if (official == null) {
			official = new Official(StatusCode.NotFound);
		}
		return official;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Official> findOfficials(LocalDate fromDate, LocalDate toDate) {
		List<Official> officials = getSessionFactory().getCurrentSession().createCriteria(Official.class)
			.add(Restrictions.le("fromDate", fromDate))
			.add(Restrictions.ge("toDate", toDate))
			.list();
		if (officials == null) {
			officials = new ArrayList<Official>();
		}
		return officials;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Official> findOfficials(String lastName, String firstName) {
		List<Official> officials = getSessionFactory().getCurrentSession().createCriteria(Official.class)
			.add(Restrictions.eq("lastName", lastName))
			.add(Restrictions.eq("firstName", firstName))
			.list();
		if (officials == null) {
			officials = new ArrayList<Official>();
		}
		return officials;
	}

	@Override
	public Official createOfficial(Official createOfficial) {
		Official official = findOfficial(createOfficial.getLastName(), createOfficial.getFirstName(), createOfficial.getFromDate(), createOfficial.getToDate());
		if (official.isNotFound()) {
			getSessionFactory().getCurrentSession().persist(createOfficial);
			createOfficial.setStatusCode(StatusCode.Created);
		}
		else {
			throw new DuplicateEntityException();
		}
		return createOfficial;
	}

	@Override
	public Official updateOfficial(Official updateOfficial) {
		Official official = findOfficial(updateOfficial.getLastName(), updateOfficial.getFirstName(), updateOfficial.getFromDate(), updateOfficial.getToDate());
		if (official.isFound()) {
			official.setLastName(updateOfficial.getLastName());
			official.setFirstName(updateOfficial.getFirstName());
			official.setFromDate(updateOfficial.getFromDate());
			official.setToDate(updateOfficial.getToDate());
			official.setNumber(updateOfficial.getNumber());
			official.setStatusCode(StatusCode.Updated);
			getSessionFactory().getCurrentSession().persist(official);
		}
		return official;
	}

	@Override
	public Official deleteOfficial(String lastName, String firstName, LocalDate fromDate, LocalDate toDate) {
		Official official = findOfficial(lastName, firstName, fromDate, toDate);
		if (official.isFound()) {
			getSessionFactory().getCurrentSession().delete(official);
			official = new Official(StatusCode.Deleted);
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