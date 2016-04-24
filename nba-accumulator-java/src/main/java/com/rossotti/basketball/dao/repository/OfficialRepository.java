package com.rossotti.basketball.dao.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rossotti.basketball.dao.model.Official;
import com.rossotti.basketball.dao.model.StatusCode;

@Repository
@Transactional
public class OfficialRepository {
	@Autowired
	private SessionFactory sessionFactory;

	public Official findOfficial(String lastName, String firstName, LocalDate asOfDate) {
		Official official = (Official)getSessionFactory().getCurrentSession().createCriteria(Official.class)
			.add(Restrictions.eq("lastName", lastName))
			.add(Restrictions.eq("firstName", firstName))
			.add(Restrictions.le("fromDate", asOfDate))
			.add(Restrictions.ge("toDate", asOfDate))
			.uniqueResult();
		if (official == null) {
			official = new Official(StatusCode.NotFound);
		}
		else {
			official.setStatusCode(StatusCode.Found);
		}
		return official;
	}

	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	public List<Official> findOfficials(LocalDate asOfDate) {
		List<Official> officials = getSessionFactory().getCurrentSession().createCriteria(Official.class)
			.add(Restrictions.le("fromDate", asOfDate))
			.add(Restrictions.ge("toDate", asOfDate))
			.list();
		if (officials == null) {
			officials = new ArrayList<Official>();
		}
		return officials;
	}

	public Official createOfficial(Official createOfficial) {
		Official official = findOfficial(createOfficial.getLastName(), createOfficial.getFirstName(), createOfficial.getFromDate());
		if (official.isNotFound()) {
			getSessionFactory().getCurrentSession().persist(createOfficial);
			createOfficial.setStatusCode(StatusCode.Created);
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
			official.setStatusCode(StatusCode.Updated);
			getSessionFactory().getCurrentSession().saveOrUpdate(official);
		}
		return official;
	}

	public Official deleteOfficial(String lastName, String firstName, LocalDate asOfDate) {
		Official official = findOfficial(lastName, firstName, asOfDate);
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
