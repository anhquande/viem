import java.util.ArrayList;
import java.util.List;

import de.anhquan.viem.core.model.NavigationItem;


public class TestMe {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String sidebar = "/speisekarte.html;Speisekarte|/speisekarte/vorspeisen.html;Vorspeisen|/speisekarte/huhnerfleisch.html;Hühnerfleisch|/speisekarte/panierter-huhnerbrust.html;Panierter Hühnerbrust|/speisekarte/knuspriges-hahnchen.html;Knuspriges Hähnchen|/speisekarte/knusprige-ente.html;Knusprige Ente|/speisekarte/rindfleisch.html;Rindfleisch|/speisekarte/fisch.html;Fisch|/speisekarte/hummerkrabben.html;Hummerkrabben|/speisekarte/spezialitaten.html;Spezialitäten|/speisekarte/vegetarisch.html;Vegetarisch|/speisekarte/kleine-portionen.html;Kleine Portionen|/speisekarte/beilagen.html;Beilagen|/speisekarte/desserts.html;Desserts";
		List<NavigationItem> sidebarList = NavigationItem.createList(sidebar);

		for (NavigationItem navigationItem : sidebarList) {
			System.out.println(navigationItem.getUrl() + " " + navigationItem.getTitle());
		}
	}

}
