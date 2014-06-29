package de.anhquan.viem.core.dao;

import java.util.logging.Logger;

import com.googlecode.objectify.ObjectifyService;

import de.anhquan.viem.core.model.Page;

public class PageDao extends NameBasedDao<Page>{

	static{
		ObjectifyService.register(Page.class);
	}
	
	public static final Logger log = Logger.getLogger(PageDao.class.getName());
	
	protected PageDao() {
		super(Page.class);
	}

	public void addPage(String path, String template, String title, String description, String content){
		Page p = give(path);
		
		p.setTemplate(template);
		p.setTitle(title);
		p.setDescription(description);
		p.setContent(content);
		put(p);
	}
	
	public void createDefaultPages(){
		
		addPage("partyservice","default", "Partyservice","Partyservice", "<p>Gerne kochen wir auch für Ihre Feierlichkeiten zu günstigen Preisen. Lassen Sie sich von uns beraten und ein auf Ihre Bedürfnisse passendes Angebot machen.</p>");
		addPage("kontakt","default", "Kontakt","Kontakt", "<h3>Wir freuen uns von Ihnen zu hören!</h3><p>Wir freuen uns über alle Rückmeldungen. Falls Sie ein Problem mit einer Bestellung haben, geben Sie uns bitte so viele Details wie möglich an, damit wir diese Probleme beheben können.</p>");
		addPage("impressum","default","Impressum","Impressum","Add your impressum here");
		addPage("index","default","Über uns","Über uns","Liebe Gäste,herzlich willkommen bei Thai Express in Elversberg");
	}
}
