package br.com.cofagra.bi.controladores;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("session")
public class LocaleControlador implements Serializable {

	private static final long serialVersionUID = -621576245308219774L;

	private Locale locale = new Locale("pt", "BR");

	public void localePortugues() {
		UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
		locale = new Locale("pt", "BR");
		viewRoot.setLocale(locale);
	}

	public void localeIngles() {
		UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
		locale = new Locale("en", "US");
		viewRoot.setLocale(locale);
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}