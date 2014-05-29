import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import de.anhquan.viem.frontend.model.Page;
import de.anhquan.viem.frontend.util.PageExporter;


public class PageExporterTest {

	@Test
	public void testExportToJSON(){
		PageExporter exporter = new PageExporter();
		List<Page> pages = new ArrayList<Page>();
		
		for(int i=0;i<10;i++){
			Page p = new Page();
			p.setName("page"+i);
			p.setTitle("Page Title "+i);
			p.setDescription("Page Description"+i);
			p.setContent("Page Content "+i);
			p.setTemplate("default"+i);
			p.setId(new Long(i));
			pages.add(p);
		}
		String jsonStr = exporter.exportJSONString(pages );
		System.out.println(jsonStr);
		
		//////// Test Import ////////////
		
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(jsonStr);
			JSONArray json = (JSONArray) obj;
			
			for(int i=0;i<json.size();i++){
				JSONObject item = (JSONObject) json.get(i);
				Page p = new Page();
				p.fromJSON(item);
				
				System.out.println(p.getTitle());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ClassCastException e){
			
		}
		
	}
}
