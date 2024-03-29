package com.luciotbc.tagit.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.caelum.vraptor.view.Results;

import com.luciotbc.tagit.dao.gae.DAO;
import com.luciotbc.tagit.i18n.I18nMessages;
import com.luciotbc.tagit.intercepts.Restrict;
import com.luciotbc.tagit.model.User;
import com.luciotbc.tagit.util.Encrypt;
import com.luciotbc.tagit.util.Validators;

@Resource
public class UserController {
	private DAO dao;
	private Result result;
	private Validator validator;
	private I18nMessages i18n;
	private HttpServletRequest req;

	public UserController(DAO dao, Result result, Validator validator,	I18nMessages i18n, HttpServletRequest request) {
		this.dao = dao;
		this.result = result;
		this.validator = validator;
		this.i18n = i18n;
		this.req = request;
	}

	@Restrict
	@Path("/user/show/{id}")
	public void show(Long id) {
		User user = dao.findById(User.class, id);

		if(user == null){
			String msg = i18n.i18n("error.user.null");
			validator.add(new ValidationMessage(msg, "error"));
		}
		// redireciona p�gina
		if (validator.hasErrors()) {
			validator.onErrorUse(Results.logic()).redirectTo(IndexController.class).index();
		} else {
			result.include("user", user);
		}
	}
	
	@Restrict
	@Path({"/user/show","/profile"})
	public void show() {
		User user = getUser();
		if(user == null){
			String msg = i18n.i18n("error.user.null");
			validator.add(new ValidationMessage(msg, "error"));
		}
		// redireciona p�gina
		if (validator.hasErrors()) {
			validator.onErrorUse(Results.logic()).redirectTo(IndexController.class).index();
			//validator.onErrorSendBadRequest();
		} else {
			result.include("isEdited", true);
			result.include("user", user);
		}
	}
	
	@Restrict
	@Path({"/user/edit", "/profile/edit"})
	public void edit() {
		User user = getUser();

		if(user == null){
			String msg = i18n.i18n("error.user.null");
			validator.add(new ValidationMessage(msg, "error"));
		}
		// redireciona p�gina
		if (validator.hasErrors()) {
			validator.onErrorUse(Results.logic()).redirectTo(IndexController.class).index();
		} else {
			result.include("user", user);
		}
	}
	
	@Restrict
	@Path("/user/save")
	public void save(User user, String password) {

		if (user.getName().length() < 3) {
			String msg = i18n.i18n("error.register.user.name.small");
			validator.add(new ValidationMessage(msg, "error"));
		}
		if(!Validators.isEmail(user.getEmail())) {
			String msg = i18n.i18n("error.register.user.email.invalid");
			validator.add(new ValidationMessage(msg, "error"));
		}	
		if((!user.getEmail().equals(getUser().getEmail())) && (dao.findUserByEmail(user.getEmail()) != null)){
			String msg = i18n.i18n("error.register.user.already.exists");
			validator.add(new ValidationMessage(msg, "error"));
		}
		//pass
		if(!user.getPassword().equals(Encrypt.encrypt(password))){
			String msg = i18n
					.i18n("error.register.user.password.does.not.match");
			validator.add(new ValidationMessage(msg, "error"));
		}
		if (password.length() < 6) {
			String msg = i18n.i18n("error.register.user.password.small");
			validator.add(new ValidationMessage(msg, "error"));
		}
		// redireciona p�gina
		if (validator.hasErrors()) {
			validator.onErrorRedirectTo(this.getClass()).edit();
		} else {
			User currentUser = dao.findById(User.class, getUser().getId());
			currentUser.setEmail(user.getEmail());
			currentUser.setName(user.getName());
			currentUser.setPassword(password);
			dao.update(currentUser);
			storeUser(currentUser);
			ArrayList<ValidationMessage> flash = new ArrayList<ValidationMessage>();
			flash.add(new ValidationMessage(i18n.i18n("flash.user.updaded"),"flash"));
			result.include("flash", flash);
			result.use(Results.logic()).redirectTo(HomeController.class).index();
		}
	}
	
	private User getUser() {
		HttpSession session = req.getSession(false);
		if( session == null ) {
			return null;
		}
		User customer = (User) session.getAttribute("user");
		return customer;
	}

	private void storeUser(User user) {
		HttpSession session = req.getSession(true);
		session.setAttribute("user", user);
	}
}
