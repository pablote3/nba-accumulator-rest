package com.rossotti.basketball.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rossotti.basketball.dao.OfficialDAO;
import com.rossotti.basketball.dao.exceptions.DuplicateEntityException;
import com.rossotti.basketball.dao.exceptions.NoSuchEntityException;
import com.rossotti.basketball.models.Official;

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
			throw new NoSuchEntityException();
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
		if (officials == null || officials.size() == 0) {
			throw new NoSuchEntityException();
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
		if (officials == null || officials.size() == 0) {
			throw new NoSuchEntityException();
		}
		return officials;
	}

	@Override
	public void createOfficial(Official createOfficial) {
		Official official = (Official)getSessionFactory().getCurrentSession().createCriteria(Official.class)
				.add(Restrictions.eq("lastName", createOfficial.getLastName()))
				.add(Restrictions.eq("firstName", createOfficial.getFirstName()))
				.add(Restrictions.le("fromDate", createOfficial.getFromDate()))
				.add(Restrictions.ge("toDate", createOfficial.getToDate()))
				.uniqueResult();
		if (official == null) {
			getSessionFactory().getCurrentSession().persist(createOfficial);
		}
		else {
			throw new DuplicateEntityException();
		}
	}

	@Override
	public void updateOfficial(Official updateOfficial) {
		Official official = findOfficial(updateOfficial.getLastName(), updateOfficial.getFirstName(), updateOfficial.getFromDate(), updateOfficial.getToDate());
		official.setLastName(updateOfficial.getLastName());
		official.setFirstName(updateOfficial.getFirstName());
		official.setFromDate(updateOfficial.getFromDate());
		official.setToDate(updateOfficial.getToDate());
		official.setNumber(updateOfficial.getNumber());
		getSessionFactory().getCurrentSession().persist(official);
	}

	@Override
	public void deleteOfficial(String lastName, String firstName, LocalDate fromDate, LocalDate toDate) {
		Official official = findOfficial(lastName, firstName, fromDate, toDate);
		getSessionFactory().getCurrentSession().delete(official);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
