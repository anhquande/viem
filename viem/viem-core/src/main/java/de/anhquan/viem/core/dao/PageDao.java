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
		
		addPage("partyservice","fullpage", "Partyservice","Partyservice", "<p>Gerne kochen wir auch für Ihre Feierlichkeiten zu günstigen Preisen. Lassen Sie sich von uns beraten und ein auf Ihre Bedürfnisse passendes Angebot machen.</p>");
		addPage("kontakt","fullpage", "Kontakt","Kontakt", "<h3>Wir freuen uns von Ihnen zu hören!</h3><p>Wir freuen uns über alle Rückmeldungen. Falls Sie ein Problem mit einer Bestellung haben, geben Sie uns bitte so viele Details wie möglich an, damit wir diese Probleme beheben können.</p>");
		addPage("impressum","fullpage","Impressum","Impressum","Add your impressum here");
		addPage("index","fullpage","Über uns","Über uns","Liebe Gäste,herzlich willkommen bei Thai Express in Elversberg");
		addPage("speisekarte","default","Speisekarte","speisekarte","<h3>Wir verwenden kein Glutamat!</h3><h3>Alle Gerichte zum Mitnehmen</h3><h3>Einfach vorstellen und abholen</h3>");
		addPage("speisekarte/vorspeisen","default","Vorspeisen","Vorspeisen","");
		addPage("speisekarte/huhnerfleisch","default","Hühnerfleisch","Hühnerfleisch","add content here");
		addPage("speisekarte/panierter-huhnerbrust","default","Panierter Hühnerbrust","Panierter Hühnerbrust","add content here");
		addPage("speisekarte/knuspriges-hahnchen","default","Knuspriges Hähnchen","Knuspriges Hähnchen","add content here");
		addPage("speisekarte/knusprige-ente","default","Knusprige Ente","Knusprige Ente","add content here");
		addPage("speisekarte/rindfleisch","default","Rindfleisch","Rindfleisch","add content here");
		addPage("speisekarte/fisch","default","Fisch","Fisch","add content here");
		addPage("speisekarte/hummerkrabben","default","Hummerkrabben","Hummerkrabben","add content here");
		addPage("speisekarte/spezialitaten","default","Spezialitäten","Spezialitäten","add content here");
		addPage("speisekarte/vegetarisch","default","Vegetarisch","Vegetarisch","add content here");
		addPage("speisekarte/kleine-portionen","default","Kleine Portionen","Kleine Portionen","add content here");
		addPage("speisekarte/beilagen","default","Beilagen","Beilagen","add content here");
		addPage("speisekarte/desserts","default","Desserts","Desserts","add content here");
	}
}
