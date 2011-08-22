package com.luciotbc.tagit.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.luciotbc.tagit.dao.gae.DAO;
import com.luciotbc.tagit.i18n.I18nMessages;
import com.luciotbc.tagit.model.Evaluation;
import com.luciotbc.tagit.model.User;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.caelum.vraptor.view.Results;

@Resource
public class HomeController {

	private DAO dao;
	private Result result;
	private Validator validator;
	private I18nMessages i18n;
	private HttpServletRequest req;

	public HomeController(DAO dao, Result result, Validator validator,
			I18nMessages i18n, HttpServletRequest request) {
		this.dao = dao;
		this.result = result;
		this.validator = validator;
		this.i18n = i18n;
		this.req = request;
	}

	@Path("/home")
	public void index() {
		if (getUser() == null) {
			String msg = i18n.i18n("error.user.is.not.logged");
			validator.add(new ValidationMessage(msg, "error"));
			validator.onErrorUse(Results.logic())
					.redirectTo(IndexController.class).index();
		} else {
			// TODO PAREI AQUI HOJE CONTINUE DAQUI TOPEIRA
			// BUSCAR AVALIA��ES POR EMAIL!!!
			List<Evaluation> evaluations = dao.findAllEvaluationsByModerator(getUser().getId());
			result.include("evaluations", evaluations);
			result.include("user", getUser());
		}
	}

	@Path("/logout")
	public void logout() {
		HttpSession session = req.getSession(true);
		session.setAttribute("user", null);
		String msg = i18n.i18n("flash.home.logout");
		result.include("msg", msg);
		this.result.use(Results.logic()).redirectTo(IndexController.class)
				.index();
	}

	private User getUser() {
		HttpSession session = req.getSession(false);
		if (session == null) {
			return null;
		}
		User customer = (User) session.getAttribute("user");
		return customer;
	}
}
